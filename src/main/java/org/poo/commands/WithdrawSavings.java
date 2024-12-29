package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.User;
import org.poo.entities.ValutarCourse;
import org.poo.utils.Pair;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class WithdrawSavings implements Command {
	private String account;
	private double amount;
	private String currency;
	private int timestamp;
	private ArrayList<ValutarCourse> exchangeRates;

	public WithdrawSavings(String account, double amount, String currency, int timestamp, ArrayList<ValutarCourse> exchangeRates) {
		this.account = account;
		this.amount = amount;
		this.currency = currency;
		this.timestamp = timestamp;
		this.exchangeRates = exchangeRates;
	}

	public void execute(List<User> users, ArrayNode output) {
		Account targetAccount = findAccountByIban(users, account);
		if (targetAccount == null) {
			return;
		}

		User accountOwner = findUserByAccount(users, targetAccount);
		if (accountOwner == null) {
			return;
		}

		// Verifică dacă utilizatorul are peste 21 de ani
		if (!isUserOver21(accountOwner.getDateOfBirth())) {
			Transactions newTransaction = new Transactions(
					"withdrawSavings",
					"You don't have the minimum age required.",
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
			targetAccount.getTransactions().add(newTransaction);
			return;
		}

//		// Verifică dacă există suficienți bani în cont
//		if (targetAccount.getBalance() < Pair.convertCurrency(amount, this.currency,
//				targetAccount.getCurrency(), exchangeRates)) {
//			return;
//		}

		targetAccount.setBalance(targetAccount.getBalance() - Pair.convertCurrency(amount, this.currency,
				targetAccount.getCurrency(), exchangeRates));
	}

	// Metodă pentru a verifica dacă un utilizator are peste 21 de ani
	private boolean isUserOver21(String birthDate) {
		LocalDate birth = LocalDate.parse(birthDate); // Presupunem formatul "yyyy-MM-dd"
		LocalDate today = LocalDate.now();
		return Period.between(birth, today).getYears() >= 21;
	}

	// Metodă pentru a găsi utilizatorul pe baza contului
	private User findUserByAccount(List<User> users, Account account) {
		for (User user : users) {
			if (user.getAccounts().contains(account)) {
				return user;
			}
		}
		return null;
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
