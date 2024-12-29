package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.Card;
import org.poo.utils.Utils;

import java.util.List;

/**
 * Command implementation for creating a new card for a user's account.
 */
public class CreateCard implements Command {

	/**
	 * The email of the user for whom the card is to be created.
	 */
	private final String email;

	/**
	 * The IBAN of the account to which the card will be linked.
	 */
	private final String account;

	/**
	 * The timestamp of the card creation.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the CreateCard command.
	 *
	 * @param email     the email of the user
	 * @param account   the IBAN of the account
	 * @param timestamp the timestamp of the operation
	 */
	public CreateCard(final String email, final String account, final int timestamp) {
		this.email = email;
		this.account = account;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the CreateCard command, adding a new card to the specified user's account.
	 *
	 * @param users  the list of users to search for the target user
	 * @param output the JSON array node to which the command's output can be added
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				var accountOptional = user.getAccounts().stream()
						.filter(acc -> acc.getIban().equals(account))
						.findFirst();

				if (accountOptional.isPresent()) {
					var account = accountOptional.get();

					String newCardNumber = Utils.generateCardNumber();

					boolean cardExists = account.getCards().stream()
							.anyMatch(card -> card.getCardNumber().equals(newCardNumber));

					if (cardExists) {
						return;
					}

					Card newCard = new Card();
					newCard.setCardNumber(newCardNumber);
					newCard.setStatus("active");
					newCard.setMinimumBalance(0.0);

					account.addCard(newCard);

					Transactions newTransaction = new Transactions(
							"createCard",
							"New card created",
							0.0,
							account.getIban(),
							null,
							timestamp,
							null,
							newCard.getCardNumber(),
							user.getEmail(),
							null,
							null
					);
					account.getTransactions().add(newTransaction);

					return;
				} else {
					return;
				}
			}
		}
	}
}
