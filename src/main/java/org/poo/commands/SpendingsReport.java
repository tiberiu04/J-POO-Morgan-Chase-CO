/**
 * Package containing command classes for managing user operations.
 */
package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.SavingsAccount;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.Transactions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command to generate a spending report for a specific account within a time range.
 */
public class SpendingsReport implements Command {
	/**
	 * The starting timestamp for filtering transactions.
	 */
	private final int startTimestamp;

	/**
	 * The ending timestamp for filtering transactions.
	 */
	private final int endTimestamp;

	/**
	 * The IBAN of the target account for the spending report.
	 */
	private final String accountIban;

	/**
	 * The timestamp for this command's execution.
	 */
	private final int timestamp;

	/**
	 * Constructor to initialize the SpendingReport command.
	 *
	 * @param startTimestamp The start timestamp for the report.
	 * @param endTimestamp   The end timestamp for the report.
	 * @param accountIban    The IBAN of the account.
	 * @param timestamp      The command execution timestamp.
	 */
	public SpendingsReport(final int startTimestamp, final int endTimestamp, final String accountIban, final int timestamp) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.accountIban = accountIban;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the spending report command, filtering transactions for the specified account and timeframe.
	 *
	 * @param users  The list of users containing their accounts.
	 * @param output The output ArrayNode to store the result in JSON format.
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode commandNode = mapper.createObjectNode();
		ObjectNode outputNode = mapper.createObjectNode();

		Account targetAccount = findAccountByIban(users, accountIban);

		// Prepare the command field in response
		commandNode.put("command", "spendingsReport");
		commandNode.put("timestamp", timestamp);

		if (targetAccount == null) {
			// Account not found case
			outputNode.put("description", "Account not found");
			outputNode.put("timestamp", timestamp);
			commandNode.set("output", outputNode);
			output.add(commandNode);
			return;
		}

		// Check if the account is a savings account
		if (targetAccount instanceof SavingsAccount) {
			// If it is a savings account, return the specified error message
			ObjectNode errorOutput = mapper.createObjectNode();
			errorOutput.put("error", "This kind of report is not supported for a saving account");
			commandNode.set("output", errorOutput);
			output.add(commandNode);
			return;
		}

		// Filter transactions: exclude "createCard" and "deleteCard" commands
		List<Transactions> filteredTransactions = targetAccount.getTransactions().stream()
				.filter(t -> t.getTimestamp() >= startTimestamp && t.getTimestamp() <= endTimestamp)
				.filter(t -> t.getCommerciant() != null && !t.getCommerciant().isEmpty())
				.filter(t -> !("createCard".equals(t.getCommand()) || "deleteCard".equals(t.getCommand()))) // Exclude commands
				.collect(Collectors.toList());

		Map<String, Double> spendingByMerchant = filteredTransactions.stream()
				.collect(Collectors.groupingBy(
						Transactions::getCommerciant,
						Collectors.summingDouble(Transactions::getAmount)
				));

		List<Map.Entry<String, Double>> sortedMerchants = spendingByMerchant.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toList());

		ArrayNode merchantsArray = mapper.createArrayNode();
		for (Map.Entry<String, Double> entry : sortedMerchants) {
			ObjectNode merchantNode = mapper.createObjectNode();
			merchantNode.put("total", entry.getValue());
			merchantNode.put("commerciant", entry.getKey());
			merchantsArray.add(merchantNode);
		}

		ArrayNode transactionsArray = mapper.createArrayNode();
		for (Transactions transaction : filteredTransactions) {
			ObjectNode transactionNode = mapper.createObjectNode();
			transactionNode.put("amount", transaction.getAmount());
			transactionNode.put("commerciant", transaction.getCommerciant());
			transactionNode.put("description", transaction.getDescription());
			transactionNode.put("timestamp", transaction.getTimestamp());
			transactionsArray.add(transactionNode);
		}

		outputNode.put("balance", targetAccount.getBalance());
		outputNode.set("commerciants", merchantsArray);
		outputNode.put("currency", targetAccount.getCurrency());
		outputNode.put("IBAN", targetAccount.getIban());
		outputNode.set("transactions", transactionsArray);

		commandNode.set("output", outputNode);

		output.add(commandNode);
	}


	/**
	 * Finds an account by its IBAN.
	 *
	 * @param users The list of users to search through.
	 * @param iban  The IBAN of the account to find.
	 * @return The matching account, or null if not found.
	 */
	private Account findAccountByIban(final List<User> users, final String iban) {
		for (User user : users) {
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(iban)) {
					return account;
				}
			}
		}
		return null;
	}
}
