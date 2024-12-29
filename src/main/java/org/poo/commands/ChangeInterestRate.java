package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.SavingsAccount;

import java.util.List;

/**
 * Command to change the interest rate of a savings account.
 */
public class ChangeInterestRate implements Command {
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
	public ChangeInterestRate(String account, double interestRate, int timestamp) {
		this.account = account;
		this.interestRate = interestRate;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the command to change the interest rate.
	 *
	 * @param users  List of users containing accounts.
	 * @param output Output ArrayNode for results.
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {

		for (User user : users) {
			for (Account acc : user.getAccounts()) {
				if (acc.getIban().equals(account)) {
					if (acc.isSavingsAccount()) {
						((SavingsAccount) acc).setInterestRate(interestRate);

						Transactions transaction = new Transactions("addInterest",
								"Interest rate of the account changed to " + interestRate,
								0.0,
								null,
								null,
								timestamp,
								null,
								null,
								null,
								null,
								null);
						acc.getTransactions().add(transaction);
					} else {
						ObjectMapper mapper = new ObjectMapper();
						ObjectNode commandNode = mapper.createObjectNode();
						ObjectNode outputNode = mapper.createObjectNode();

						outputNode.put("description", "This is not a savings account");
						outputNode.put("timestamp", timestamp);
						commandNode.put("command", "changeInterestRate");
						commandNode.set("output", outputNode);
						commandNode.put("timestamp", timestamp);
						output.add(commandNode);
					}

					return;
				}
			}
		}
	}
}
