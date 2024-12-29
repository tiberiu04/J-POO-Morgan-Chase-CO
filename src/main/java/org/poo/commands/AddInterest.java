/**
 * Package containing command classes for managing user operations.
 */
package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.Account;

import java.util.List;

import static org.poo.utils.Utils.PROCENT;

/**
 * Command to add interest to savings accounts.
 */
public class AddInterest implements Command {
	/**
	 * Executes the command (placeholder).
	 */
	private final String account;
	private final double interestRate;
	private final int timestamp;

	/**
	 * Initializes a ChangeInterestRate command.
	 *
	 * @param account      The IBAN of the account.
	 * @param interestRate The new interest rate to set.
	 * @param timestamp    The execution timestamp.
	 */
	public AddInterest(String account, double interestRate, int timestamp) {
		this.account = account;
		this.interestRate = interestRate;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the AddInterest command, adding interest to savings accounts only.
	 *
	 * @param users  The list of users containing their accounts.
	 * @param output The output ArrayNode to store the result in JSON format.
	 */
	@Override
	public void execute(final List<User> users, final ArrayNode output) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode commandNode = mapper.createObjectNode();
		ObjectNode outputNode = mapper.createObjectNode();

		boolean accountFound = false;

		for (User user : users) {
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(account)) {
					if (account.isSavingsAccount()) {
						double interest = account.getBalance() * interestRate / PROCENT;
						account.setBalance(account.getBalance() + interest);

						outputNode.put("description", "Interest added successfully");
						outputNode.put("newBalance", account.getBalance());
						Transactions transaction = new Transactions("addInterest",
								"Interest rate of the account changed to " + interest,
								interest,
								account.getIban(),
								null,
								timestamp,
								null,
								null,
								null,
								null,
								null);
						account.getTransactions().add(transaction);
					} else {
						outputNode.put("description", "This is not a savings account");
					}

					commandNode.put("command", "addInterest");
					commandNode.set("output", outputNode);
					commandNode.put("timestamp", timestamp);
					output.add(commandNode);
					return;
				}
			}
		}

		if (!accountFound) {
			outputNode.put("description", "This is not a savings account");
			outputNode.put("timestamp", timestamp);
			commandNode.put("command", "addInterest");
			commandNode.set("output", outputNode);
			commandNode.put("timestamp", timestamp);
			output.add(commandNode);
		}
	}
}
