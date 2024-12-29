package org.poo.commands;

import org.poo.entities.Account;
import org.poo.entities.SavingsAccount;
import org.poo.utils.Utils;

/**
 * Factory for creating accounts based on account type.
 */
public class AccountFactory {

    /**
     * Creates a new account based on the specified type.
     *
     * @param accountType  the type of account to create (e.g., "savings" or "classic")
     * @param currency     the currency of the account
     * @param interestRate the interest rate (only applicable for savings accounts)
     * @return the created account
     * @throws IllegalArgumentException if the account type is invalid
     */
    public static Account createAccount(String accountType, String currency,
                                        double interestRate) {
        Account account;

        if ("savings".equalsIgnoreCase(accountType)) {
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setInterestRate(interestRate);
            account = savingsAccount;
        } else if ("classic".equalsIgnoreCase(accountType)) {
            account = new Account();
        } else {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        account.setCurrency(currency);
        account.setIban(Utils.generateIBAN());
        account.setMinimumBalance(0.0);
        account.setType(accountType);

        return account;
    }
}
