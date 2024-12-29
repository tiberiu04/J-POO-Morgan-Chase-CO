package org.poo.entities;

/**
 * Represents an exchange rate (Valutar Course) between two currencies.
 */
public class ValutarCourse {

	/**
	 * The source currency.
	 */
	private String from;

	/**
	 * The target currency.
	 */
	private String to;

	/**
	 * The exchange rate from the source currency to the target currency.
	 */
	private double rate;

	/**
	 * Default constructor for creating an empty ValutarCourse object.
	 */
	public ValutarCourse() {
	}

	/**
	 * Constructor to initialize a ValutarCourse with specific details.
	 *
	 * @param from the source currency
	 * @param to   the target currency
	 * @param rate the exchange rate
	 */
	public ValutarCourse(String from, String to, double rate) {
		this.from = from;
		this.to = to;
		this.rate = rate;
	}

	/**
	 * Returns the source currency.
	 *
	 * @return the source currency
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the source currency.
	 *
	 * @param from the source currency to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Returns the target currency.
	 *
	 * @return the target currency
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the target currency.
	 *
	 * @param to the target currency to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Returns the exchange rate.
	 *
	 * @return the exchange rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * Sets the exchange rate.
	 *
	 * @param rate the exchange rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * Returns a string representation of the exchange rate.
	 *
	 * @return a string containing the source and target currencies and the exchange rate
	 */
	@Override
	public String toString() {
		return "ValutarCourse{" +
				"from='" + from + '\'' +
				", to='" + to + '\'' +
				", rate=" + rate +
				'}';
	}
}
