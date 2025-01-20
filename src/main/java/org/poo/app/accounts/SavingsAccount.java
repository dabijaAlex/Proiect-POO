package org.poo.app.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;
import org.poo.app.User;
import org.poo.app.plans.ServicePlan;
import org.poo.commands.otherCommands.AddInterest;
import org.poo.commands.otherCommands.ChangeInterestRate;
import org.poo.transactions.AddInterestTransaction;
import org.poo.transactions.ChangeInterestRateTransaction;


@Getter
@Setter
public class SavingsAccount extends Account {
    /**
     * Constructor
     * @param iban
     * @param balance
     * @param currency
     * @param type
     */
    public SavingsAccount(final String iban, final double balance, final String currency,
                          final String type,
                          final double interestRate, final User user, final String email) {
        super(iban, balance, currency, type, interestRate, user, email);
    }


    /**
     * not a classic account so this returns null
     * @param currency
     * @return
     */
    @Override
    public Account getClassicAccount(final String currency) {
        return null;
    }

    /**
     * make a withdrawal of x amount
     * if not enough money throw exception
     * @param targetAccount
     * @param amount
     * @throws InsufficientFundsException
     */
    @Override
    public void makeWithdrawal(final Account targetAccount,
                               final double amount) throws InsufficientFundsException {
        if (amount > this.getBalance()) {
            throw new InsufficientFundsException();
        }
        this.balance -= amount;
        targetAccount.setBalance(targetAccount.getBalance() + amount);
    }


    /**
     * add the interest to the account since it is a savings ine
     * @param output
     * @param command
     */
    @Override
    public void addInterest(final ArrayNode output, final AddInterest command) {
        double interest = balance * interestRate;
        balance = balance + interest;
        this.addTransaction(new AddInterestTransaction(command.getTimestampTheSecond(),
                interest, this.currency));
    }

    /**
     * set another interest rate
     * @param interestRate
     * @param output
     * @param command
     */
    public void setInterestRate(final double interestRate, final ArrayNode output,
                                final ChangeInterestRate command) {
        this.interestRate = interestRate;
        this.addTransaction(new ChangeInterestRateTransaction(command.getTimestamp(),
                interestRate));
    }
}
