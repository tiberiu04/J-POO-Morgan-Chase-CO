package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.entities.*;
import org.poo.utils.Pair;
import org.poo.utils.Utils;

import java.util.*;

import static org.poo.utils.Pair.convertCurrency;

/**
 * Command implementation for processing online payments using a user's card.
 */
public class PayOnline implements Command {

	/**
	 * The email address of the user initiating the payment.
	 */
	private String email;

	/**
	 * The card number used for the payment.
	 */
	private String cardNumber;

	/**
	 * The amount to be paid.
	 */
	private double amount;

	/**
	 * The currency in which the payment is made.
	 */
	private String currency;

	/**
	 * The timestamp of the transaction.
	 */
	private int timestamp;

	/**
	 * The description of the payment.
	 */
	private String description;

	/**
	 * The merchant's name associated with the payment.
	 */
	private String commerciant;

	/**
	 * The list of exchange rates for currency conversion.
	 */
	private ArrayList<ValutarCourse> exchangeRates;

	/**
	 * Constructs a new PayOnline command.
	 *
	 * @param email         the email address of the user
	 * @param cardNumber    the card number used for the payment
	 * @param amount        the amount to be paid
	 * @param currency      the currency of the payment
	 * @param timestamp     the timestamp of the transaction
	 * @param description   the description of the payment
	 * @param commerciant   the merchant's name
	 * @param exchangeRates the list of exchange rates for currency conversion
	 */
	public PayOnline(String email, String cardNumber, double amount, String currency, int timestamp,
					 String description, String commerciant, ArrayList<ValutarCourse> exchangeRates) {
		this.email = email;
		this.cardNumber = cardNumber;
		this.amount = amount;
		this.currency = currency;
		this.timestamp = timestamp;
		this.description = description;
		this.commerciant = commerciant;
		this.exchangeRates = exchangeRates;
	}

	/**
	 * Executes the PayOnline command by searching for the user's card and processing the payment.
	 *
	 * @param users  the list of users
	 * @param output the JSON array node for output
	 */
	public void execute(List<User> users, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode commandOutput = objectMapper.createObjectNode();
		commandOutput.put("command", "payOnline");

		for (User user : users) {
			if (user.getEmail().equals(email)) {
				for (Account account : user.getAccounts()) {
					var cardOptional = account.getCards().stream()
							.filter(card -> card.getCardNumber().equals(cardNumber))
							.findFirst();

					if (cardOptional.isPresent()) {
						Card card = cardOptional.get();

						if (!"active".equals(card.getStatus())) {
							Transactions insuff = new Transactions("payOnline",
									"The card is frozen",
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
							insuff.setCurrency(currency);
							account.getTransactions().add(insuff);
							return;
						}

						if (card instanceof OneTimeCard && ((OneTimeCard) card).isUsed()) {
							return;
						}

						double totalAmount = amount;
						if (account.getPlan().equals("standard"))
							totalAmount = amount + amount * 0.002;
						else if (account.getPlan().equals("silver") && amount < Pair.convertCurrency(500,
								"RON", currency, exchangeRates))
							totalAmount = amount + amount * 0.001;

						double convertedAmount = convertCurrency(totalAmount, currency, account.getCurrency(), exchangeRates);
						if (convertedAmount < 0) {
							return;
						}

						if (account.getBalance() < convertedAmount) {
							Transactions insuff = new Transactions("payOnline",
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
							account.getTransactions().add(insuff);
							return;
						}

						account.setBalance(account.getBalance() - convertedAmount);
						Transactions insuff = new Transactions("payOnline",
								"Card payment",
								Pair.convertCurrency(amount, currency, account.getCurrency(), exchangeRates),
								null,
								null,
								timestamp,
								null,
								cardNumber,
								null,
								commerciant,
								null
						);
						account.getTransactions().add(insuff);
						if (card.isOneTimeCard()) {
							((OneTimeCard) card).useCard();
							card.setStatus("inactive");
							//account.getCards().remove(card);

							Transactions transaction = new Transactions(
									"deleteCard",
									"The card has been destroyed",
									0.0,
									null,
									null,
									timestamp,
									null,
									cardNumber,
									user.getEmail(),
									account.getIban(),
									null
							);

							account.getTransactions().add(transaction);
							String newCardNumber = Utils.generateCardNumber();
							OneTimeCard newCard = new OneTimeCard();
							newCard.setCardNumber(newCardNumber);
							newCard.setStatus("active");
							newCard.setTimestamp(timestamp);

							Transactions transactions = new Transactions(
									"createCard",
									"New card created",
									0.0,
									account.getIban(),
									null,
									timestamp,
									null,
									newCardNumber,
									user.getEmail(),
									null,
									null
							);

							account.getTransactions().add(transactions);
							account.addCard(newCard);
						}

						return;
					}
				}
			}
		}

		ObjectNode errorOutput = createErrorOutput(objectMapper, "Card not found");
		commandOutput.set("output", errorOutput);
		commandOutput.put("timestamp", timestamp);
		output.add(commandOutput);
	}

	/**
	 * Creates a JSON error output node.
	 *
	 * @param objectMapper the JSON object mapper
	 * @param description  the error description
	 * @return a JSON node representing the error
	 */
	private ObjectNode createErrorOutput(ObjectMapper objectMapper, String description) {
		ObjectNode errorOutput = objectMapper.createObjectNode();
		errorOutput.put("timestamp", timestamp);
		errorOutput.put("description", description);
		return errorOutput;
	}

}
