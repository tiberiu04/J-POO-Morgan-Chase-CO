package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.ValutarCourse;
import org.poo.utils.Pair;

import java.util.*;

import static org.poo.utils.Pair.convertCurrency;

/**
 * Command implementation for transferring money between accounts.
 */
public class SendMoney implements Command {

	/**
	 * The IBAN of the sender's account.
	 */
	private final String account;

	/**
	 * The amount of money to be transferred.
	 */
	private final double amount;

	/**
	 * The IBAN of the receiver's account.
	 */
	private final String receiver;

	/**
	 * The timestamp of the transaction.
	 */
	private final int timestamp;

	/**
	 * A description of the transaction.
	 */
	private final String description;

	/**
	 * The list of exchange rates for currency conversion.
	 */
	private final ArrayList<ValutarCourse> exchangeRates;

	/**
	 * Constructor for initializing the SendMoney command.
	 *
	 * @param account       the IBAN of the sender's account
	 * @param amount        the amount to be transferred
	 * @param receiver      the IBAN of the receiver's account
	 * @param timestamp     the timestamp of the transaction
	 * @param description   a description of the transaction
	 * @param exchangeRates the list of exchange rates for currency conversion
	 */
	public SendMoney(String account, double amount, String receiver, int timestamp, String description, ArrayList<ValutarCourse> exchangeRates) {
		this.account = account;
		this.amount = amount;
		this.receiver = receiver;
		this.timestamp = timestamp;
		this.description = description;
		this.exchangeRates = exchangeRates;
	}

	/**
	 * Executes the money transfer between accounts.
	 *
	 * @param users  the list of users containing the accounts
	 * @param output the JSON array node to store the output
	 */
	@Override
	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandOutput = objectMapper.createObjectNode();
		commandOutput.put("command", "sendMoney");

		Account sourceAccount = null;
		Account targetAccount = null;

		for (User user : users) {
			for (Account acc : user.getAccounts()) {
				if (acc.getIban().equals(account)) {
					sourceAccount = acc;
				}
				if (acc.getIban().equals(receiver)) {
					targetAccount = acc;
				}
			}
		}

		if (sourceAccount == null || targetAccount == null) {
			return;
		}

		if (sourceAccount.getBalance() < amount) {
			Transactions newTransaction = new Transactions(
					"sendMoney",
					"Insufficient funds",
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
			sourceAccount.getTransactions().add(newTransaction);
			return;
		}

		double totalAmount = amount;
		if (sourceAccount.getPlan().equals("standard"))
			totalAmount = amount + amount * 0.002;
		else if (sourceAccount.getPlan().equals("silver") && amount < Pair.convertCurrency(500,
				"RON", sourceAccount.getCurrency(), exchangeRates))
			totalAmount = amount + amount * 0.001;

		double converted = convertCurrency(amount, targetAccount.getCurrency(), sourceAccount.getCurrency(), exchangeRates);
		if (converted < 0) {
			return;
		}

		sourceAccount.setBalance(sourceAccount.getBalance() - totalAmount);

		targetAccount.setBalance(targetAccount.getBalance() + converted);

		Transactions sourceTransaction = new Transactions(
				"sendMoney",
				description,
				amount,
				receiver,
				account,
				timestamp,
				"sent",
				null,
				null,
				null,
				null
		);
		sourceTransaction.setCurrency(sourceAccount.getCurrency());
		sourceAccount.getTransactions().add(sourceTransaction);

		Transactions targetTransaction = new Transactions(
				"sendMoney",
				description,
				converted,
				receiver,
				account,
				timestamp,
				"received",
				null,
				null,
				null,
				null
		);
		targetTransaction.setCurrency(targetAccount.getCurrency());
		targetAccount.getTransactions().add(targetTransaction);
	}
}
