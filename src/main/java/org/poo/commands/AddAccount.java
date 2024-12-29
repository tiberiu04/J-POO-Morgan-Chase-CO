package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.User;

import java.util.List;

/**
 * Command implementation for adding a new account to a user.
 */
public class AddAccount implements Command {

	/**
	 * The email of the user to whom the account will be added.
	 */
	private final String email;

	/**
	 * The currency of the new account.
	 */
	private final String currency;

	/**
	 * The type of account to add (e.g., savings or classic).
	 */
	private final String accountType;

	/**
	 * The interest rate for savings accounts.
	 */
	private final double interestRate;

	/**
	 * The timestamp of when the account was added.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the AddAccount command.
	 *
	 * @param email        the email of the user
	 * @param currency     the currency of the new account
	 * @param accountType  the type of account (savings or classic)
	 * @param interestRate the interest rate (only applicable for savings accounts)
	 * @param timestamp    the timestamp of the operation
	 */
	public AddAccount(final String email, final String currency, final String accountType,
					  final double interestRate, final int timestamp) {
		this.email = email;
		this.currency = currency;
		this.accountType = accountType;
		this.interestRate = interestRate;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the AddAccount command, adding a new account to the specified user.
	 *
	 * @param users  the list of users to search for the target user
	 * @param output the JSON array node to which the command's output can be added
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		User user = findUserByEmail(users, email);
		if (user != null) {
			Account newAccount = AccountFactory.createAccount(accountType, currency,
					interestRate);

			user.addAccount(newAccount);
			if (user.getOccupation().equals("student")) {
				newAccount.setPlan("student");
			}

			Transactions transaction = createTransaction();
			newAccount.getTransactions().add(transaction);
		}
	}

	/**
	 * Finds a user by their email address.
	 *
	 * @param users the list of users
	 * @param email the email to search for
	 * @return the user if found, otherwise null
	 */
	private User findUserByEmail(List<User> users, String email) {
		return users.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Creates a transaction representing the account creation.
	 *
	 * @return the created transaction
	 */
	private Transactions createTransaction() {
		return new Transactions(
				"addAccount",
				"New account created",
				0.0,
				null,
				null,
				timestamp,
				null,
				null,
				null,
				null,
				null
		);
	}
}
