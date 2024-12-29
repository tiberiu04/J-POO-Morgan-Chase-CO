package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.ValutarCourse;

import java.util.*;

/**
 * Command implementation for printing the transactions of a user.
 */
public class PrintTransactions implements Command {

	/**
	 * The email of the user whose transactions are to be printed.
	 */
	private final String email;

	/**
	 * The timestamp of the command execution.
	 */
	private final int timestamp;

	/**
	 * The list of exchange rates for handling transactions in multiple currencies.
	 */
	private final List<ValutarCourse> exchangeRates;

	/**
	 * Constructor for initializing the PrintTransactions command.
	 *
	 * @param email         the email of the user
	 * @param timestamp     the timestamp of the command execution
	 * @param exchangeRates the list of exchange rates
	 */
	public PrintTransactions(String email, int timestamp, List<ValutarCourse> exchangeRates) {
		this.email = email;
		this.timestamp = timestamp;
		this.exchangeRates = exchangeRates;
	}

	/**
	 * Executes the PrintTransactions command, fetching and displaying all transactions of the specified user.
	 *
	 * @param users  the list of users to search for the target user
	 * @param output the JSON array node to which the command's output can be added
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandOutput = objectMapper.createObjectNode();
		commandOutput.put("command", "printTransactions");

		User targetUser = findUserByEmail(users);
		if (targetUser == null) {
			handleUserNotFound(objectMapper, commandOutput, output);
			return;
		}

		List<Transactions> allTransactions = collectAllTransactions(targetUser);

		ArrayNode transactionsArray = objectMapper.createArrayNode();
		for (Transactions transaction : allTransactions) {
			transactionsArray.add(createTransactionNode(transaction, objectMapper, targetUser));
		}

		if (transactionsArray.size() > 0) {
			commandOutput.set("output", transactionsArray);
			commandOutput.put("timestamp", timestamp);
			output.add(commandOutput);
		}
	}

	/**
	 * Finds a user by their email.
	 *
	 * @param users the list of users to search
	 * @return the User object if found, or null otherwise
	 */
	private User findUserByEmail(List<User> users) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Collects all transactions from the user's accounts.
	 *
	 * @param user the user whose transactions are to be collected
	 * @return a sorted list of all transactions
	 */
	private List<Transactions> collectAllTransactions(User user) {
		List<Transactions> allTransactions = new ArrayList<>();
		for (Account account : user.getAccounts()) {
			allTransactions.addAll(account.getTransactions());
		}
		allTransactions.sort(Comparator.comparingInt(Transactions::getTimestamp));
		return allTransactions;
	}

	/**
	 * Creates a JSON node representing a transaction.
	 *
	 * @param transaction the transaction to be represented
	 * @param objectMapper the JSON object mapper
	 * @param user the user associated with the transaction
	 * @return the created JSON node
	 */
	private ObjectNode createTransactionNode(Transactions transaction, ObjectMapper objectMapper, User user) {
		ObjectNode transactionNode = objectMapper.createObjectNode();
		if (transaction.getTimestamp() != 0) {
			transactionNode.put("timestamp", transaction.getTimestamp());
		}

		switch (transaction.getCommand()) {
			case "addAccount":
				transactionNode.put("description", "New account created");
				break;
			case "sendMoney":
				transactionNode.put("description", transaction.getDescription());
				if (transaction.getSenderIban() != null) {
					transactionNode.put("senderIBAN", transaction.getSenderIban());
				}
				if (transaction.getReceiverIban() != null) {
					transactionNode.put("receiverIBAN", transaction.getReceiverIban());
				}
				if (transaction.getAmount() != 0) {
					transactionNode.put("amount", transaction.getAmount() + " " + user.getAccounts().get(0).getCurrency());
				}
				if (transaction.getTransferType() != null) {
					transactionNode.put("transferType", transaction.getTransferType());
				}
				break;
			case "payOnline":
				transactionNode.put("description", transaction.getDescription());
				if (transaction.getAmount() != 0) {
					transactionNode.put("amount", transaction.getAmount());
				}
				if (transaction.getCommerciant() != null) {
					transactionNode.put("commerciant", transaction.getCommerciant());
				}
				break;
			case "createCard":
			case "createOneTimeCard":
				transactionNode.put("description", "New card created");
				transactionNode.put("card", transaction.getCard());
				transactionNode.put("cardHolder", transaction.getCardHolder());
				transactionNode.put("account", transaction.getReceiverIban());
				break;
			case "deleteCard":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("card", transaction.getCard());
				transactionNode.put("cardHolder", transaction.getCardHolder());
				transactionNode.put("account", transaction.getCommerciant());
				break;
			case "deleteAccount":
				transactionNode.put("description", transaction.getDescription());
				break;
			case "checkCardStatus":
				if (transaction.getDescription() != null) {
					transactionNode.put("description", transaction.getDescription());
				}
				break;
			case "splitPayment":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("amount", transaction.getAmount());
				transactionNode.put("currency", transaction.getTransferType());
				ArrayNode accountsArray = transactionNode.putArray("involvedAccounts");
				for (String account1 : transaction.getAccountsForSplit()) {
					accountsArray.add(account1);
				}
				if (transaction.getCardHolder() != null) {
					transactionNode.put("error", "Account " + transaction.getCardHolder() + " has insufficient funds for a split payment.");
				}
				break;
			case "addInterest":
			case "changeInterestRate":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			case "withdrawSavings":
				transactionNode.put("description", transaction.getDescription());
				break;
			case "upgradePlan":
				transactionNode.put("accountIBAN", transaction.getCardHolder());
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("newPlanType", transaction.getSenderIBAN());
				break;
			case "cashWithdrawal":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("amount", transaction.getAmount());
				break;
		}
		return transactionNode;
	}

	/**
	 * Handles the case where the user is not found.
	 *
	 * @param objectMapper the JSON object mapper
	 * @param commandOutput the root command output node
	 * @param output the JSON array node to which the command's output can be added
	 */
	private void handleUserNotFound(ObjectMapper objectMapper, ObjectNode commandOutput, ArrayNode output) {
		ObjectNode errorOutput = objectMapper.createObjectNode();
		errorOutput.put("timestamp", timestamp);
		errorOutput.put("description", "User not found");
		commandOutput.set("output", errorOutput);
		output.add(commandOutput);
	}
}
