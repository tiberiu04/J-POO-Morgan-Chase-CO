package org.poo.commands;

import org.poo.entities.ValutarCourse;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Factory class for creating Command objects based on input.
 */
public class CommandFactory {

    /**
     * Creates and returns a Command object based on the given input.
     *
     * @param input         the input containing command details
     * @param exchangeRates the list of exchange rates required for certain commands
     * @return the appropriate Command object
     * @throws IllegalArgumentException if the command type is unknown
     */
    public static Command getCommand(CommandInput input, ArrayList<ValutarCourse> exchangeRates) {
        switch (input.getCommand()) {
            case "printUsers":
                return new PrintUsers(input.getTimestamp());
            case "addAccount":
                return new AddAccount(
                        input.getEmail(),
                        input.getCurrency(),
                        input.getAccountType(),
                        input.getInterestRate(),
                        input.getTimestamp()
                );
            case "createCard":
                return new CreateCard(input.getEmail(), input.getAccount(), input.getTimestamp());
            case "addFunds":
                return new AddFunds(
                        input.getAccount(),
                        input.getAmount()
                );
            case "deleteAccount":
                return new DeleteAccount(
                        input.getEmail(),
                        input.getAccount(),
                        input.getTimestamp()
                );
            case "createOneTimeCard":
                return new CreateOneTimeCard(
                        input.getEmail(),
                        input.getAccount(),
                        input.getTimestamp()
                );
            case "deleteCard":
                return new DeleteCard(
                        input.getCardNumber(),
                        input.getTimestamp()
                );
            case "payOnline":
                return new PayOnline(
                        input.getEmail(),
                        input.getCardNumber(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getTimestamp(),
                        input.getDescription(),
                        input.getCommerciant(),
                        exchangeRates
                );
            case "printTransactions":
                return new PrintTransactions(input.getEmail(), input.getTimestamp(), exchangeRates);
            case "setAlias":
                return new SetAlias(input.getEmail(), input.getAlias(), input.getAccount());
            case "sendMoney":
                return new SendMoney(
                        input.getAccount(),
                        input.getAmount(),
                        input.getReceiver(),
                        input.getTimestamp(),
                        input.getDescription(),
                        exchangeRates
                );
            case "checkCardStatus":
                return new CheckCardStatus(input.getCardNumber(), input.getTimestamp());
            case "setMinimumBalance":
                return new SetMinBalance(input.getAccount(), input.getAmount(), input.getTimestamp());
            case "changeInterestRate":
                return new ChangeInterestRate(input.getAccount(), input.getInterestRate(), input.getTimestamp());
            case "addInterest":
                return new AddInterest(input.getAccount(), input.getInterestRate(), input.getTimestamp());
            case "splitPayment":
                return new SplitPayment(
                        input.getAccounts(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getTimestamp(),
                        exchangeRates
                );
            case "report":
                return new Report(
                        input.getStartTimestamp(),
                        input.getEndTimestamp(),
                        input.getAccount(),
                        input.getTimestamp(),
                        exchangeRates
                );
            case "spendingsReport":
                return new SpendingsReport(
                        input.getStartTimestamp(),
                        input.getEndTimestamp(),
                        input.getAccount(),
                        input.getTimestamp()
                );
            case "withdrawSavings":
                return new WithdrawSavings(
                        input.getAccount(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getTimestamp(),
                        exchangeRates
                );
            case "upgradePlan":
                return new UpgradePlan(input.getAccount(),
                        input.getNewPlanType(),
                        input.getTimestamp(),
                        exchangeRates);
            case "cashWithdrawal":
                return new CashWithdrawal(
                        input.getCardNumber(),
                        input.getAmount(),
                        input.getEmail(),
                        input.getLocation(),
                        input.getTimestamp(),
                        exchangeRates
                );
            default:
                throw new IllegalArgumentException("Unknown command: " + input.getCommand());
        }
    }
}
