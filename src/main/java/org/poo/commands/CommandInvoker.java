package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.User;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandInvoker is responsible for managing and executing a queue of commands.
 */
public class CommandInvoker {

    /**
     * The queue of commands to be executed.
     */
    private final List<Command> commandQueue = new ArrayList<>();

    /**
     * Adds a command to the queue.
     *
     * @param command the command to be added to the queue
     */
    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    /**
     * Executes all commands in the queue and clears the queue afterward.
     *
     * @param users  the list of users to be operated on by the commands
     * @param output the JSON array node to store the output of the commands
     */
    public void executeCommands(List<User> users, ArrayNode output) {
        for (Command command : commandQueue) {
            command.execute(users, output);
        }
        Utils.resetRandom();
        commandQueue.clear();
    }
}
