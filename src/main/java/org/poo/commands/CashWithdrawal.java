package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.*;
import org.poo.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class CashWithdrawal implements Command {
	private String cardNumber;
	private double amount;
	private String email;
	private String location;
	private int timestamp;
	private ArrayList<ValutarCourse> exchangeRates;

	public CashWithdrawal(String cardNumber,
						  double amount,
						  String email,
						  String location,
						  int timestamp,
						  ArrayList<ValutarCourse> exchangeRates) {
		this.cardNumber = cardNumber;
		this.amount = amount;
		this.email = email;
		this.location = location;
		this.timestamp = timestamp;
		this.exchangeRates = exchangeRates;
	}

	@Override
	public void execute(List<User> users, ArrayNode output) {
		Account targetAccount = findAccountByCardNumber(users, cardNumber);
		if (targetAccount == null) {
			return;
		}

		double totalAmount = amount;
		if (targetAccount.getPlan().equals("standard")) {
			totalAmount = amount + amount * 0.002;
		} else if (targetAccount.getPlan().equals("silver") && amount < 500) {
			totalAmount = amount + amount * 0.001;
		}

		// Verificăm dacă există suficienți bani în cont
		if (targetAccount.getBalance() < Pair.convertCurrency(totalAmount, "RON", targetAccount.getCurrency(), exchangeRates)) {
			return;
		}

		for (Card card : targetAccount.getCards()) {
			if (card.getCardNumber().equals(cardNumber) && !card.getStatus().equals("active")) {
				System.out.println("SUGI PULA");
				// Adaugă mesaj de eroare specific
				ObjectNode result = output.addObject();
				result.put("command", "cashWithdrawal");

				ObjectNode resultOutput = result.putObject("output");
				resultOutput.put("description", "Card has already been used");
				resultOutput.put("timestamp", timestamp);

				result.put("timestamp", timestamp);
				output.add(resultOutput);
				return;
			}
		}

		targetAccount.setBalance(targetAccount.getBalance() -
				Pair.convertCurrency(totalAmount, "RON", targetAccount.getCurrency(), exchangeRates));

		Transactions transaction = new Transactions(
				"cashWithdrawal",
				"Cash withdrawal of " + amount,
				amount,
				null,
				null,
				timestamp,
				null,
				null,
				null,
				null,
				null
		);
		targetAccount.getTransactions().add(transaction);
	}

	private Account findAccountByCardNumber(List<User> users, String cardNumber) {
		for (User user : users) {
			for (Account account : user.getAccounts()) {
				for (Card card : account.getCards()) {
					if (card.getCardNumber().equals(cardNumber)) {
						return account;
					}
				}
			}
		}
		return null;
	}
}
