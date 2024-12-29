package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.ValutarCourse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Command implementation for generating a financial report of transactions within a time range.
 */
public class Report implements Command {

	/**
	 * The start timestamp of the report range.
	 */
	private final int startTimestamp;

	/**
	 * The end timestamp of the report range.
	 */
	private final int endTimestamp;

	/**
	 * The IBAN of the account for which the report is generated.
	 */
	private final String accountIban;

	/**
	 * The timestamp of the report generation.
	 */
	private final int timestamp;

	/**
	 * The list of exchange rates for handling transactions in multiple currencies.
	 */
	private final List<ValutarCourse> exchangeRates;

	/**
	 * Constructor for initializing the Report command.
	 *
	 * @param startTimestamp the start timestamp of the report range
	 * @param endTimestamp   the end timestamp of the report range
	 * @param accountIban    the IBAN of the account for the report
	 * @param timestamp      the timestamp of the command execution
	 * @param exchangeRates  the list of exchange rates
	 */
	public Report(int startTimestamp, int endTimestamp, String accountIban, int timestamp, List<ValutarCourse> exchangeRates) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.accountIban = accountIban;
		this.timestamp = timestamp;
		this.exchangeRates = exchangeRates;
	}

	/**
	 * Executes the Report command, generating a financial report of transactions within the specified time range.
	 *
	 * @param users  the list of users to search for the target account
	 * @param output the JSON array node to which the report's output can be added
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode commandNode = mapper.createObjectNode();
		ObjectNode outputNode = mapper.createObjectNode();

		Account targetAccount = findAccountByIban(users, accountIban);

		if (targetAccount != null) {
			List<Transactions> filteredTransactions = targetAccount.getTransactions().stream()
					.filter(t -> t.getTimestamp() >= startTimestamp && t.getTimestamp() <= endTimestamp)
					.collect(Collectors.toList());

			outputNode.put("balance", targetAccount.getBalance());
			outputNode.put("currency", targetAccount.getCurrency());
			outputNode.put("IBAN", targetAccount.getIban());

			ArrayNode transactionsArray = mapper.createArrayNode();

			for (Transactions t : filteredTransactions) {
				transactionsArray.add(createTransactionNode(mapper, t));
			}

			outputNode.set("transactions", transactionsArray);

			commandNode.put("command", "report");
			commandNode.set("output", outputNode);
			commandNode.put("timestamp", timestamp);
			output.add(commandNode);
		} else {
			handleAccountNotFound(commandNode, outputNode, output);
		}
	}

	/**
	 * Finds an account by its IBAN.
	 *
	 * @param users the list of users to search
	 * @param iban  the IBAN of the account
	 * @return the Account object if found, or null otherwise
	 */
	private Account findAccountByIban(List<User> users, String iban) {
		for (User user : users) {
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(iban)) {
					return account;
				}
			}
		}
		return null;
	}

	/**
	 * Creates a JSON node for a transaction.
	 *
	 * @param mapper      the JSON object mapper
	 * @param transaction the transaction to represent
	 * @return the JSON node representing the transaction
	 */
	private ObjectNode createTransactionNode(ObjectMapper mapper, Transactions transaction) {
		ObjectNode transactionNode = mapper.createObjectNode();

		switch (transaction.getCommand()) {
			case "addAccount":
				transactionNode.put("description", "New account created");
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			case "payOnline":
				transactionNode.put("amount", transaction.getAmount());
				transactionNode.put("commerciant", transaction.getCommerciant());
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			case "splitPayment":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("amount", transaction.getAmount());
				transactionNode.put("currency", transaction.getTransferType());
				if (transaction.getCardHolder() != null) {
					transactionNode.put("error", "Account " + transaction.getCardHolder() + " has insufficient funds for a split payment.");
				}
				ArrayNode accountsArray = transactionNode.putArray("involvedAccounts");
				for (String account1 : transaction.getAccountsForSplit()) {
					accountsArray.add(account1);
				}
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			case "sendMoney":
				if (transaction.getCurrency() != null) {
					transactionNode.put("amount", transaction.getAmount() + " " + transaction.getCurrency());
				}
				transactionNode.put("description", transaction.getDescription());
				if (transaction.getSenderIBAN() != null) {
					transactionNode.put("senderIBAN", transaction.getSenderIBAN());
				}
				if (transaction.getReceiverIBAN() != null) {
					transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
				}
				transactionNode.put("timestamp", transaction.getTimestamp());
				if (transaction.getTransferType() != null) {
					transactionNode.put("transferType", transaction.getTransferType());
				}
				break;
			case "createCard":
				transactionNode.put("account", transaction.getReceiverIban());
				transactionNode.put("card", transaction.getCard());
				transactionNode.put("cardHolder", transaction.getCardHolder());
				transactionNode.put("description", "New card created");
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			case "deleteAccount":
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
			default:
				transactionNode.put("description", transaction.getDescription());
				transactionNode.put("timestamp", transaction.getTimestamp());
				break;
		}

		return transactionNode;
	}

	/**
	 * Handles the case where the target account is not found.
	 *
	 * @param commandNode the root command output node
	 * @param outputNode  the JSON node containing error details
	 * @param output      the JSON array node to which the error is added
	 */
	private void handleAccountNotFound(ObjectNode commandNode, ObjectNode outputNode, ArrayNode output) {
		commandNode.put("command", "report");
		outputNode.put("description", "Account not found");
		outputNode.put("timestamp", timestamp);
		commandNode.set("output", outputNode);
		commandNode.put("timestamp", timestamp);
		output.add(commandNode);
	}
}
