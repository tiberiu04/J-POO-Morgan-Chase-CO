package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.Account;
import org.poo.entities.User;

import java.util.List;

/**
 * Command implementation for setting an alias for a user's account.
 */
public class SetAlias implements Command {

	/**
	 * The email of the user.
	 */
	private final String email;

	/**
	 * The alias to be set for the account.
	 */
	private final String alias;

	/**
	 * The IBAN of the account.
	 */
	private final String account;

	/**
	 * Constructor for initializing the SetAlias command.
	 *
	 * @param email   the email of the user
	 * @param alias   the alias to be set
	 * @param account the IBAN of the account
	 */
	public SetAlias(String email, String alias, String account) {
		this.email = email;
		this.alias = alias;
		this.account = account;
	}

	/**
	 * Executes the SetAlias command, assigning an alias to the specified user's account.
	 *
	 * @param users  the list of users to search for the target user
	 * @param output the JSON array node to which the command's output can be added (currently unused)
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				for (Account acc : user.getAccounts()) {
					if (acc.getIban().equals(account)) {
						acc.setAlias(alias);
						return;
					}
				}
			}
		}
	}
}
