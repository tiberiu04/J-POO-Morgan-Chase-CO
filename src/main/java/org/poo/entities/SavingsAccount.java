package org.poo.entities;

/**
 * Represents a Savings Account, extending the {@link Account} class with an interest rate attribute.
 */
public class SavingsAccount extends Account {

	/**
	 * The interest rate associated with the savings account.
	 */
	private double interestRate;

	/**
	 * Returns the interest rate of the savings account.
	 *
	 * @return the interest rate
	 */
	public double getInterestRate() {
		return interestRate;
	}

	/**
	 * Sets the interest rate for the savings account.
	 *
	 * @param interestRate the interest rate to set
	 */
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	@Override
	/**
	 * Determines if this account is a savings account.
	 *
	 * @return true, as this account is a savings account
	 */
    public boolean isSavingsAccount() {
		return true;
	}
}
