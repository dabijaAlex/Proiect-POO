package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangeInterestRateTransaction extends Transaction {
    private String description;

    public ChangeInterestRateTransaction(int timestamp, double interestRate) {
        this.description = "Interest rate of the account changed to " + interestRate;
        this.timestamp = timestamp;
    }
}
