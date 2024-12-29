package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.commands.*;
import org.poo.entities.User;
import org.poo.entities.ValutarCourse;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);

        ArrayNode output = objectMapper.createArrayNode();

        List<User> users = new ArrayList<>();
        for (UserInput userInput : inputData.getUsers()) {
            User user = new User();
            user.setFirstName(userInput.getFirstName());
            user.setLastName(userInput.getLastName());
            user.setEmail(userInput.getEmail());
            user.setOccupation(userInput.getOccupation());
            user.setDateOfBirth(userInput.getBirthDate());
            users.add(user);
        }

        ArrayList<ValutarCourse> exchangeRates = new ArrayList<>();
        if (inputData.getExchangeRates() != null) {
            for (ExchangeInput exchangeInput : inputData.getExchangeRates()) {
                ValutarCourse exchange = new ValutarCourse();
                exchange.setFrom(exchangeInput.getFrom());
                exchange.setTo(exchangeInput.getTo());
                exchange.setRate(exchangeInput.getRate());
                exchangeRates.add(exchange);
            }
        }

        CommandInvoker invoker = new CommandInvoker();

        for (CommandInput commandInput : inputData.getCommands()) {
            try {
                Command command = CommandFactory.getCommand(commandInput, exchangeRates);
                invoker.addCommand(command);
            } catch (IllegalArgumentException e) {
                ObjectNode error = objectMapper.createObjectNode();
                error.put("command", commandInput.getCommand());
                error.put("status", "error");
                error.put("message", "Unknown command: " + commandInput.getCommand());
                output.add(error);
            }
        }

        invoker.executeCommands(users, output);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        return Integer.parseInt(
                file.getName()
                        .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR)
        );
    }
}
