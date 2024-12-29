package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.Transactions;
import org.poo.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Command implementation for deleting a user's account.
 */
public class DeleteAccount implements Command {

	/**
	 * The email of the user whose account will be deleted.
	 */
	private final String email;

	/**
	 * The IBAN of the account to be deleted.
	 */
	private final String accountIban;

	/**
	 * The timestamp of when the account deletion is executed.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the DeleteAccount command.
	 *
	 * @param email       the email of the user
	 * @param accountIban the IBAN of the account to delete
	 * @param timestamp   the timestamp of the operation
	 */
	public DeleteAccount(final String email, final String accountIban, final int timestamp) {
		this.email = email;
		this.accountIban = accountIban;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the DeleteAccount command, deleting a user's account if conditions are met.
	 *
	 * @param users  the list of users to search for the target account
	 * @param output the JSON array node to which the command's output will be added
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandOutput = objectMapper.createObjectNode();
		commandOutput.put("command", "deleteAccount");

		Optional<User> userOptional = findUserByEmail(users, email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Optional<org.poo.entities.Account> accountOptional = findAccountByIban(user, accountIban);

			if (accountOptional.isPresent()) {
				var account = accountOptional.get();

				if (account.getBalance() != 0) {
					addErrorOutput(output, commandOutput, "Account couldn't be deleted - see org.poo.transactions for details");
					Transactions transactions = new Transactions(
							"deleteAccount",
							"Account couldn't be deleted - there are funds remaining",
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
					account.getTransactions().add(transactions);
					return;
				}

				user.getAccounts().remove(account);

				addSuccessOutput(output, commandOutput, "Account deleted");
				return;
			} else {
				addErrorOutput(output, commandOutput, "Account not found");
				return;
			}
		}
		addErrorOutput(output, commandOutput, "User not found");
	}

	/**
	 * Finds a user by email.
	 *
	 * @param users the list of users
	 * @param email the email to search for
	 * @return an Optional containing the user if found
	 */
	private Optional<User> findUserByEmail(List<User> users, String email) {
		return users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
	}

	/**
	 * Finds an account by IBAN for a specific user.
	 *
	 * @param user  the user whose accounts are searched
	 * @param iban the IBAN to search for
	 * @return an Optional containing the account if found
	 */
	private Optional<org.poo.entities.Account> findAccountByIban(User user, String iban) {
		return user.getAccounts().stream().filter(acc -> acc.getIban().equals(iban)).findFirst();
	}

	/**
	 * Adds an error message to the command output.
	 *
	 * @param output        the JSON array node to append to
	 * @param commandOutput the JSON object for the command
	 * @param message       the error message to add
	 */
	private void addErrorOutput(ArrayNode output, ObjectNode commandOutput, String message) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode errorOutput = objectMapper.createObjectNode();
		errorOutput.put("error", message);
		errorOutput.put("timestamp", timestamp);
		commandOutput.set("output", errorOutput);
		commandOutput.put("timestamp", timestamp);
		output.add(commandOutput);
	}

	/**
	 * Adds a success message to the command output.
	 *
	 * @param output        the JSON array node to append to
	 * @param commandOutput the JSON object for the command
	 * @param message       the success message to add
	 */
	private void addSuccessOutput(ArrayNode output, ObjectNode commandOutput, String message) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode successOutput = objectMapper.createObjectNode();
		successOutput.put("success", message);
		successOutput.put("timestamp", timestamp);
		commandOutput.set("output", successOutput);
		commandOutput.put("timestamp", timestamp);
		output.add(commandOutput);
	}

}
