package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.User;
import org.poo.entities.OneTimeCard;
import org.poo.entities.Transactions;
import org.poo.utils.Utils;

import java.util.List;

/**
 * Command implementation for creating a one-time card for a user's account.
 */
public class CreateOneTimeCard implements Command {

	/**
	 * The email of the user for whom the card is to be created.
	 */
	private final String email;

	/**
	 * The IBAN of the account to which the card will be linked.
	 */
	private final String accountIban;

	/**
	 * The timestamp of the card creation.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the CreateOneTimeCard command.
	 *
	 * @param email       the email of the user
	 * @param accountIban the IBAN of the account
	 * @param timestamp   the timestamp of the operation
	 */
	public CreateOneTimeCard(final String email, final String accountIban, final int timestamp) {
		this.email = email;
		this.accountIban = accountIban;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the CreateOneTimeCard command, adding a one-time card to the specified user's account.
	 *
	 * @param users  the list of users to search for the target user
	 * @param output the JSON array node to which the command's output can be added (currently unused)
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				var accountOptional = user.getAccounts().stream()
						.filter(acc -> acc.getIban().equals(accountIban))
						.findFirst();

				if (accountOptional.isPresent()) {
					var account = accountOptional.get();

					String newCardNumber = Utils.generateCardNumber();

					boolean cardExists = account.getCards().stream()
							.anyMatch(card -> card.getCardNumber().equals(newCardNumber));

					if (cardExists) {
						return;
					}

					OneTimeCard newCard = new OneTimeCard();
					newCard.setCardNumber(newCardNumber);
					newCard.setStatus("active");
					newCard.setTimestamp(timestamp);

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
