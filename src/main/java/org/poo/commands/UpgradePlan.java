package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.ValutarCourse;
import org.poo.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class UpgradePlan implements Command{
	private String account;
	private String plan;
	private int timestamp;
	private ArrayList<ValutarCourse> exchangeRates;

	public UpgradePlan(String account, String plan, int timestamp, ArrayList<ValutarCourse> exchangeRates) {
		this.account = account;
		this.plan = plan;
		this.timestamp = timestamp;
		this.exchangeRates = exchangeRates;
	}

	@Override
	public void execute(List<User> users, ArrayNode output) {
		Account targetAccount = findAccountByIban(users, account);
		if (plan.equals("silver")) {
			targetAccount.setPlan("silver");
			targetAccount.setBalance(targetAccount.getBalance() - Pair.convertCurrency(100, "RON", targetAccount.getCurrency(), exchangeRates));
		} else if (plan.equals("gold") && targetAccount.getPlan().equals("silver")) {
			targetAccount.setPlan("gold");
			targetAccount.setBalance(targetAccount.getBalance() - Pair.convertCurrency(250, "RON", targetAccount.getCurrency(), exchangeRates));
		} else {
			targetAccount.setPlan("gold");
			targetAccount.setBalance(targetAccount.getBalance() - Pair.convertCurrency(350, "RON", targetAccount.getCurrency(), exchangeRates));
		}

		Transactions transaction = new Transactions(
				"upgradePlan",
				"Upgrade plan",
				0.0,
				null,
				plan,
				timestamp,
				null,
				null,
				account,
				null,
				null
		);
		targetAccount.getTransactions().add(transaction);
	}

	private Account findAccountByIban(final List<User> users, final String iban) {
		for (User user : users) {
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(iban)) {
					return account;
				}
			}
		}
		return null;
	}
}
