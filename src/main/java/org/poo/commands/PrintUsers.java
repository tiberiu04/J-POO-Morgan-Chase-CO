package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.User;

import java.util.List;

/**
 * Command implementation for printing user details.
 * This command formats user data, including accounts and associated cards,
 * and outputs it as a JSON structure.
 */
public class PrintUsers implements Command {

	/**
	 * Timestamp of when the command was executed.
	 */
	private final int timestamp;

	/**
	 * Constructor to initialize the PrintUsers command with a timestamp.
	 *
	 * @param timestamp the timestamp of the command execution
	 */
	public PrintUsers(final int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Executes the PrintUsers command, serializing user details to JSON and adding it to the output array.
	 *
	 * @param users  the list of users to process
	 * @param output the JSON array node to which the command's output will be added
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandNode = objectMapper.createObjectNode();
		commandNode.put("command", "printUsers");

		ArrayNode usersArray = objectMapper.createArrayNode();
		for (User user : users) {
			ObjectNode userNode = objectMapper.createObjectNode();
			userNode.put("firstName", user.getFirstName());
			userNode.put("lastName", user.getLastName());
			userNode.put("email", user.getEmail());

			ArrayNode accountsArray = objectMapper.createArrayNode();
			user.getAccounts().forEach(account -> {
				ObjectNode accountNode = objectMapper.createObjectNode();
				accountNode.put("IBAN", account.getIban());
				accountNode.put("balance", account.getBalance());
				accountNode.put("currency", account.getCurrency());
				accountNode.put("type", account.getType());

				ArrayNode cardsArray = objectMapper.createArrayNode();
				account.getCards().forEach(card -> {
					ObjectNode cardNode = objectMapper.createObjectNode();
					cardNode.put("cardNumber", card.getCardNumber());
					cardNode.put("status", card.getStatus());
					cardsArray.add(cardNode);
				});

				accountNode.set("cards", cardsArray);
				accountsArray.add(accountNode);
			});

			userNode.set("accounts", accountsArray);
			usersArray.add(userNode);
		}

		commandNode.set("output", usersArray);
		commandNode.put("timestamp", timestamp);
		output.add(commandNode);
	}
}
