package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.User;
import org.poo.entities.Account;

import java.util.List;

/**
 * Command implementation for setting the minimum balance for an account.
 */
public class SetMinBalance implements Command {

	/**
	 * The IBAN of the target account.
	 */
	private final String targetIBAN;

	/**
	 * The minimum balance amount to be set.
	 */
	private final double minBalanceAmount;

	/**
	 * The timestamp of the operation.
	 */
	private final int timestamp;

	/**
	 * Constructor for initializing the SetMinBalance command.
	 *
	 * @param targetIBAN       the IBAN of the target account
	 * @param minBalanceAmount the minimum balance amount to set
	 * @param timestamp        the timestamp of the operation
	 */
	public SetMinBalance(String targetIBAN, double minBalanceAmount, int timestamp) {
		this.targetIBAN = targetIBAN;
		this.minBalanceAmount = minBalanceAmount;
		this.timestamp = timestamp;
	}

	/**
	 * Executes the SetMinBalance command, setting the minimum balance for the specified account.
	 *
	 * @param users  the list of users to search for the target account
	 * @param output the JSON array node to which the command's output can be added (currently unused)
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		boolean accountFound = false;

		for (User user : users) {
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(targetIBAN)) {
					accountFound = true;

					account.setMinimumBalance(minBalanceAmount);
					return;
				}
			}
		}
	}
}
