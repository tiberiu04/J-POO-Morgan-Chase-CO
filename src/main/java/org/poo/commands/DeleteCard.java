package org.poo.commands;

import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.Card;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class DeleteCard implements Command {
	private String cardNumber;
	private int timestamp;

	public DeleteCard(String cardNumber, int timestamp) {
		this.cardNumber = cardNumber;
		this.timestamp = timestamp;
	}

	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandOutput = objectMapper.createObjectNode();
		commandOutput.put("command", "deleteCard");

		for (User user : users) {
			for (Account account : user.getAccounts()) {
				var cardOptional = account.getCards().stream()
						.filter(card -> card.getCardNumber().equals(cardNumber))
						.findFirst();

				if (cardOptional.isPresent()) {
					Card card = cardOptional.get();
					account.getCards().remove(card);

					Transactions transaction = new Transactions(
							"deleteCard",
							"The card has been destroyed",
							0.0,
							null,
							null,
							timestamp,
							null,
							cardNumber,
							user.getEmail(),
							account.getIban(),
							null
					);

					account.getTransactions().add(transaction);
					return;
				}
			}
		}
	}
}
