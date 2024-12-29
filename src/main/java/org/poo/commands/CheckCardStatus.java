package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.Card;

import java.util.List;

/**
 * Command to check the status of a card and update its state if necessary.
 */
public class CheckCardStatus implements Command {

	/**
	 * The card number to check.
	 */
	private final String cardNumberToCheck;

	/**
	 * The timestamp of the command execution.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the CheckCardStatus command.
	 *
	 * @param cardNumberToCheck the card number to verify
	 * @param timestamp         the timestamp of the command execution
	 */
	public CheckCardStatus(String cardNumberToCheck, int timestamp) {
		this.cardNumberToCheck = cardNumberToCheck;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the CheckCardStatus command, checking the status of a card and potentially freezing it if conditions are met.
	 *
	 * @param users  the list of users containing accounts and cards
	 * @param output the JSON array node to store the output
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode resultNode = mapper.createObjectNode();
		ObjectNode outputNode = mapper.createObjectNode();

		boolean cardFound = false;

		for (User user : users) {
			for (Account account : user.getAccounts()) {
				for (Card card : account.getCards()) {
					if (card.getCardNumber().equals(cardNumberToCheck)) {
						cardFound = true;

						handleCardStatus(account, card);
						return;
					}
				}
			}
		}

		if (!cardFound) {
			handleCardNotFound(output, resultNode, outputNode);
		}
	}

	/**
	 * Handles the status of a card, freezing it if the account balance is below the required threshold.
	 *
	 * @param account the account associated with the card
	 * @param card    the card to check and potentially update
	 */
	private void handleCardStatus(Account account, Card card) {
		double balance = account.getBalance();
		double minimumBalance = account.getMinimumBalance();

		Transactions transaction = new Transactions();
		transaction.setCommand("checkCardStatus");
		transaction.setTimestamp(0);

		if ((balance - minimumBalance) <= 30) {
			transaction.setTimestamp(timestamp);
			transaction.setDescription("You have reached the minimum amount of funds, the card will be frozen");
			card.setStatus("frozen");
		}

		if (transaction.getDescription() != null && transaction.getTimestamp() != 0) {
			account.getTransactions().add(transaction);
		}
	}

	/**
	 * Handles the case where the card is not found among the users' accounts.
	 *
	 * @param output      the JSON array node to store the result
	 * @param resultNode  the root JSON node of the command result
	 * @param outputNode  the JSON node containing the error details
	 */
	private void handleCardNotFound(ArrayNode output, ObjectNode resultNode, ObjectNode outputNode) {
		outputNode.put("timestamp", timestamp);
		outputNode.put("description", "Card not found");

		resultNode.put("command", "checkCardStatus");
		resultNode.set("output", outputNode);
		resultNode.put("timestamp", timestamp);

		output.add(resultNode);
	}
}
