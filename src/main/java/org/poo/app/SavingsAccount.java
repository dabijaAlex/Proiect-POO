package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.AddInterest;
import org.poo.commands.ChangeInterestRate;
import org.poo.transactions.ChangeInterestRateTransaction;


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
                          final String type) {
        super(IBAN, balance, currency, type);
    }

    /**
     * add the interest to the account since it is a savings ine
     * @param output
     * @param command
     */
    @Override
    public void addInterest(final ArrayNode output, final AddInterest command) {
        balance += balance * interestRate;
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
