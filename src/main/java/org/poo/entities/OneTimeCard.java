package org.poo.entities;

/**
 * Represents a One-Time Card that can be used only once.
 * Extends the {@link Card} class and adds attributes specific to one-time use.
 */
public class OneTimeCard extends Card {

	/**
	 * Indicates whether the card has been used.
	 */
	private boolean used;

	/**
	 * The timestamp of the card creation or use.
	 */
	private int timestamp;

	/**
	 * Default constructor initializing the card as unused.
	 */
	public OneTimeCard() {
		this.used = false;
	}

	/**
	 * Returns whether the card has been used.
	 *
	 * @return {@code true} if the card has been used; {@code false} otherwise
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * Sets the usage status of the card.
	 *
	 * @param used {@code true} to mark the card as used; {@code false} otherwise
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}

	/**
	 * Returns the timestamp associated with the card.
	 *
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp associated with the card.
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Marks the card as used and updates its status to "inactive".
	 * This method should only be called when the card is being used for the first time.
	 */
	public void useCard() {
		if (!used) {
			used = true;
			this.setStatus("inactive");
		}
	}

	@Override
	/**
	 * Determines if this card is a one-time card.
	 *
	 * @return true, as this card is a one-time card
	 */
	public boolean isOneTimeCard() {
		return true;
	}
}
