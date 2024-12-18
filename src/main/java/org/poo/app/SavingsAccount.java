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
    public SavingsAccount(final String IBAN, final double balance, final String currency, final String type) {
        super(IBAN, balance, currency, type);
    }

    @Override
    public void addInterest(final ArrayNode output, final AddInterest command) {
        balance += balance * interestRate;
    }

    public void setInterestRate(final double interestRate, final ArrayNode output, final User user, final ChangeInterestRate command) {
        this.interestRate = interestRate;
        this.addTransaction(new ChangeInterestRateTransaction(command.getTimestamp(), interestRate));
    }
}
