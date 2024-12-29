package org.poo.entities;

import java.util.ArrayList;

/**
 * Represents a bank account entity with various attributes such as IBAN, balance, currency, and cards.
 */
public class Account {

	/**
	 * The International Bank Account Number (IBAN) of the account.
	 */
	private String iban;

	/**
	 * The current balance of the account.
	 */
	private double balance;

	/**
	 * The currency of the account.
	 */
	private String currency;

	/**
	 * The type of the account (e.g., savings, current).
	 */
	private String type;

	/**
	 * The list of cards associated with the account.
	 */
	private ArrayList<Card> cards = new ArrayList<>();

	/**
	 * The list of transactions associated with the account.
	 */
	private ArrayList<Transactions> transactions = new ArrayList<>();

	/**
	 * An optional alias for the account.
	 */
	private String alias;

	/**
	 * The minimum balance that must be maintained in the account.
	 */
	private double minimumBalance;

	private String plan = "standard";

	/**
	 * Default constructor initializing the balance to 0.0.
	 */
	public Account() {
		this.balance = 0.0;
	}

	/**
	 * Constructor to initialize the account with specific details.
	 *
	 * @param iban     the IBAN of the account
	 * @param currency the currency of the account
	 * @param type     the type of the account
	 */
	public Account(String iban, String currency, String type) {
		this.iban = iban;
		this.balance = 0.0;
		this.currency = currency;
		this.type = type;
	}

	/**
	 * Deposits a specified amount into the account.
	 *
	 * @param amount the amount to deposit
	 */
	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
		}
	}

	/**
	 * Adds a card to the account.
	 *
	 * @param card the card to add
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * Returns the current balance of the account.
	 *
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Returns the IBAN of the account.
	 *
	 * @return the IBAN
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * Returns the currency of the account.
	 *
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Returns the type of the account.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the list of cards associated with the account.
	 *
	 * @return the list of cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * Sets the balance of the account.
	 *
	 * @param balance the new balance
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Sets the currency of the account.
	 *
	 * @param currency the new currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Sets the type of the account.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the IBAN of the account.
	 *
	 * @param iban the new IBAN
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * Adds funds to the account balance.
	 *
	 * @param amount the amount to add
	 */
	public void addFunds(double amount) {
		this.balance += amount;
	}

	/**
	 * Deletes all cards associated with the account.
	 */
	public void deleteAllCards() {
		this.cards.clear();
	}

	/**
	 * Adds a transaction to the account's transaction history.
	 *
	 * @param transaction the transaction to add
	 */
	public void addTransaction(Transactions transaction) {
		transactions.add(transaction);
	}

	/**
	 * Returns the list of transactions associated with the account.
	 *
	 * @return the list of transactions
	 */
	public ArrayList<Transactions> getTransactions() {
		return transactions;
	}

	/**
	 * Sets an alias for the account.
	 *
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Returns the alias of the account.
	 *
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the minimum balance for the account.
	 *
	 * @param minimumBalance the minimum balance to set
	 */
	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	/**
	 * Returns the minimum balance of the account.
	 *
	 * @return the minimum balance
	 */
	public double getMinimumBalance() {
		return minimumBalance;
	}

	/**
	 * Determines if the account is a savings account.
	 *
	 * @return true if the account is a savings account, false otherwise
	 */
    public boolean isSavingsAccount() {
        return false;
    }

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getPlan() {
		return plan;
	}

    /**
	 * Returns a string representation of the account, which is the IBAN.
	 *
	 * @return the IBAN as a string
	 */
	@Override
	public String toString() {
		return iban;
	}
}
