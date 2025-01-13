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
import org.poo.transactions.WithdrewSavingsTransaction;

import java.math.BigDecimal;


@Getter
@Setter
public class SavingsAccount extends Account {
    /**
     * Constructor
     * @param IBAN
     * @param balance
     * @param currency
     * @param type
     */
    public SavingsAccount(final String IBAN, final double balance, final String currency,
                          final String type, ServicePlan servicePlan, final double interestRate, final User user, final String email) {
        super(IBAN, balance, currency, type, servicePlan, interestRate, user, email);
    }


    @Override
    public Account getClassicAccount(String currency) {
        return null;
    }

    @Override
    public void makeWithdrawal(Account targetAccount, double amount) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate(targetAccount.getCurrency(), this.getCurrency());
//        double amountToTakeFromAccount = convRate * amount + servicePlan.getCommissionAmount(amount, currency);
        double amountToTakeFromAccount = convRate * amount;
        if(amountToTakeFromAccount > this.getBalance()){
            throw new InsufficientFundsException();
        }
        this.balance -= amountToTakeFromAccount;
        targetAccount.setBalance(targetAccount.getBalance() + convRate * amount);
    }


    /**
     * add the interest to the account since it is a savings ine
     * @param output
     * @param command
     */
    @Override
    public void addInterest(final ArrayNode output, final AddInterest command) {
        double interest = balance * interestRate;
        balance = balance + 0.1;
        balance = balance + interest;
        balance = balance - 0.1;
        this.addTransaction(new AddInterestTransaction(command.getTimestampTheSecond(), interest, this.currency));
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
