package org.poo.entities;

import java.util.List;

/**
 * Represents a transaction entity containing details about various types of transactions.
 */
public class Transactions {

	/**
	 * The command associated with the transaction.
	 */
	private String command;

	/**
	 * A description of the transaction.
	 */
	private String description;

	/**
	 * The amount involved in the transaction.
	 */
	private double amount;

	/**
	 * The IBAN of the receiver's account.
	 */
	private String receiverIban;

	/**
	 * The IBAN of the sender's account.
	 */
	private String senderIban;

	/**
	 * The timestamp of the transaction.
	 */
	private int timestamp;

	/**
	 * The type of transfer associated with the transaction.
	 */
	private String transferType;

	/**
	 * The card associated with the transaction, if applicable.
	 */
	private String card;

	/**
	 * The cardholder involved in the transaction, if applicable.
	 */
	private String cardHolder;

	/**
	 * The commerciant involved in the transaction, if applicable.
	 */
	private String commerciant;

	/**
	 * The list of accounts involved in a split payment transaction.
	 */
	private List<String> accountsForSplit;

	/**
	 * The currency used in the transaction.
	 */
	private String currency;

	/**
	 * Constructs a Transactions object with detailed parameters.
	 *
	 * @param command          the command associated with the transaction
	 * @param description      a description of the transaction
	 * @param amount           the amount involved
	 * @param receiverIban     the IBAN of the receiver
	 * @param senderIban       the IBAN of the sender
	 * @param timestamp        the timestamp of the transaction
	 * @param transferType     the type of transfer
	 * @param card             the card involved in the transaction
	 * @param cardHolder       the cardholder involved
	 * @param commerciant      the commerciant involved
	 * @param accountsForSplit the list of accounts for split payment
	 */
	public Transactions(String command,
						String description,
						double amount,
						String receiverIban,
						String senderIban,
						int timestamp,
						String transferType,
						String card,
						String cardHolder,
						String commerciant,
						List<String> accountsForSplit) {
		this.command = command;
		this.description = description;
		this.amount = amount;
		this.receiverIban = receiverIban;
		this.senderIban = senderIban;
		this.timestamp = timestamp;
		this.transferType = transferType;
		this.card = card;
		this.cardHolder = cardHolder;
		this.commerciant = commerciant;
		this.accountsForSplit = accountsForSplit;
	}

	/**
	 * Default constructor for creating an empty Transactions object.
	 */
	public Transactions() {
	}

	// Getters and Setters

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReceiverIban() {
		return receiverIban;
	}

	public void setReceiverIban(String receiverIban) {
		this.receiverIban = receiverIban;
	}

	public String getSenderIban() {
		return senderIban;
	}

	public void setSenderIban(String senderIban) {
		this.senderIban = senderIban;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getCommerciant() {
		return commerciant;
	}

	public void setCommerciant(String commerciant) {
		this.commerciant = commerciant;
	}

	public List<String> getAccountsForSplit() {
		return accountsForSplit;
	}

	public void setAccountsForSplit(List<String> accountsForSplit) {
		this.accountsForSplit = accountsForSplit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSenderIBAN() {
		return senderIban;
	}

	public String getReceiverIBAN() {
		return receiverIban;
	}
}
