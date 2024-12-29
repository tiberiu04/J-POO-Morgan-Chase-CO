package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.User;

import java.util.List;

/**
 * Command implementation for adding funds to a user's account.
 */
public class AddFunds implements Command {

	/**
	 * The IBAN of the account to which funds will be added.
	 */
	private final String accountIban;

	/**
	 * The amount of funds to add to the account.
	 */
	private final double amount;

	/**
	 * Constructor for initializing the AddFunds command.
	 *
	 * @param accountIban the IBAN of the account
	 * @param amount      the amount of funds to add
	 */
	public AddFunds(final String accountIban, final double amount) {
		this.accountIban = accountIban;
		this.amount = amount;
	}

	/**
	 * Executes the AddFunds command, adding the specified amount to the account with
	 * the given IBAN.
	 *
	 * @param users  the list of users to search for the target account
	 * @param output the JSON array node to which the command's output can be added
	 * (currently unused)
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		for (User user : users) {
			var accountOptional = user.getAccounts().stream()
					.filter(acc -> acc.getIban().equals(accountIban))
					.findFirst();

			if (accountOptional.isPresent()) {
				var account = accountOptional.get();
				account.setBalance(account.getBalance() + amount);
				return;
			}
		}
	}
}
