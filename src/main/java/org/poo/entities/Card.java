package org.poo.entities;

/**
 * Represents a Card entity with attributes such as card number, status, and minimum balance.
 */
public class Card {

	/**
	 * The unique number of the card.
	 */
	private String cardNumber;

	/**
	 * The status of the card (e.g., active, frozen).
	 */
	private String status;

	/**
	 * The minimum balance required for the card.
	 */
	private double minimumBalance;

	/**
	 * Default constructor for creating a Card object.
	 */
	public Card() {

	}

	/**
	 * Constructor to initialize the Card with specific details.
	 *
	 * @param cardNumber the unique number of the card
	 * @param status     the status of the card
	 */
	public Card(String cardNumber, String status) {
		this.cardNumber = cardNumber;
		this.status = status;
	}

	/**
	 * Returns the unique number of the card.
	 *
	 * @return the card number
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Sets the unique number of the card.
	 *
	 * @param cardNumber the card number to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * Returns the status of the card.
	 *
	 * @return the card status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status of the card.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Sets the minimum balance required for the card.
	 *
	 * @param minimumBalance the minimum balance to set
	 */
	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	/**
	 * Determines if this card is a one-time card.
	 *
	 * @return false, as the base Card class does not represent a one-time card
	 */
	public boolean isOneTimeCard() {
		return false;
	}

	/**
	 * Returns a string representation of the card.
	 *
	 * @return a string containing the card number and status
	 */
	@Override
	public String toString() {
		return "Card{" +
				"cardNumber :" + cardNumber + '\'' +
				", status :" + status + '\'' +
				'}';
	}
}
