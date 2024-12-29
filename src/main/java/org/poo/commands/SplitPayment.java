package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.entities.User;
import org.poo.entities.Account;
import org.poo.entities.Transactions;
import org.poo.entities.ValutarCourse;

import java.util.*;

import static org.poo.utils.Pair.convertCurrency;

/**
 * Command implementation for handling split payments across multiple accounts.
 */
public class SplitPayment implements Command {

    /**
     * List of IBANs of the accounts involved in the split payment.
     */
    private final List<String> accountsForSplit;

    /**
     * The total amount to be split among the accounts.
     */
    private final double amount;

    /**
     * The currency of the amount being split.
     */
    private final String currency;

    /**
     * The timestamp of the split payment operation.
     */
    private final int timestamp;

    /**
     * The list of exchange rates for handling currency conversions.
     */
    private final List<ValutarCourse> exchangeRates;

    /**
     * Constructor for initializing the SplitPayment command.
     *
     * @param accountsForSplit the list of IBANs of the accounts involved
     * @param amount           the total amount to be split
     * @param currency         the currency of the amount
     * @param timestamp        the timestamp of the operation
     * @param exchangeRates    the list of exchange rates for currency conversion
     */
    public SplitPayment(List<String> accountsForSplit, double amount, String currency, int timestamp, List<ValutarCourse> exchangeRates) {
        this.accountsForSplit = accountsForSplit;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
    }

    /**
     * Executes the SplitPayment command, distributing the specified amount among the listed accounts.
     *
     * @param users  the list of users containing the accounts
     * @param output the JSON array node to store the output
     */
    @Override
    public void execute(List<User> users, ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode resultNode = mapper.createObjectNode();

        double amountPerAccount = amount / accountsForSplit.size();

        boolean paymentValid = true;

        String failingAccount = null;

        for (String iban : accountsForSplit) {
            Account account = findAccountByIban(users, iban);
            if (account == null || account.getBalance() < convertCurrency(amountPerAccount, currency, account.getCurrency(), exchangeRates)) {
                paymentValid = false;
                failingAccount = iban;
            }
        }

        for (String iban : accountsForSplit) {
            Account account = findAccountByIban(users, iban);
            if (account != null) {
                Transactions transaction = new Transactions(
                        "splitPayment",
                        "Split payment of " + String.format("%.2f", amount) + " " + currency,
                        amountPerAccount,
                        iban,
                        null,
                        timestamp,
                        currency,
                        null,
                        failingAccount,
                        null,
                        accountsForSplit
                );

                account.getTransactions().add(transaction);

                if (paymentValid) {
                    account.setBalance(account.getBalance() - convertCurrency(amountPerAccount, currency, account.getCurrency(), exchangeRates));
                }
            }
        }
    }

    /**
     * Finds an account by its IBAN.
     *
     * @param users the list of users to search
     * @param iban  the IBAN of the account
     * @return the Account object if found, or null otherwise
     */
    private Account findAccountByIban(List<User> users, String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }
}
