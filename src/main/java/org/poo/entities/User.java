package org.poo.entities;

import java.util.ArrayList;

/**
 * Represents a User entity with personal details and associated accounts.
 */
public class User {

	/**
	 * The email address of the user.
	 */
	private String email;

	/**
	 * The first name of the user.
	 */
	private String firstName;

	/**
	 * The last name of the user.
	 */
	private String lastName;

	/**
	 * A list of accounts associated with the user.
	 */
	private ArrayList<Account> accounts = new ArrayList<>();

	private String occupation;
	private String dateOfBirth;
	/**
	 * Default constructor for creating an empty User object.
	 */
	public User() {
	}

	/**
	 * Constructor to initialize a User with specific details.
	 *
	 * @param email     the email address of the user
	 * @param firstName the first name of the user
	 * @param lastName  the last name of the user
	 */
	public User(String email, String firstName, String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Returns the email address of the user.
	 *
	 * @return the email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the user.
	 *
	 * @param email the email address to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the first name of the user.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the user.
	 *
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the user.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the user.
	 *
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the list of accounts associated with the user.
	 *
	 * @return the list of accounts
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	/**
	 * Adds an account to the user's list of accounts.
	 *
	 * @param account the account to add
	 */
	public void addAccount(Account account) {
		accounts.add(account);
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * Returns a string representation of the user.
	 *
	 * @return a string containing the user's details
	 */
	@Override
	public String toString() {
		return "User{" +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
