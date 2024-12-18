package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class ChangeInterestRateTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     * @param interestRate
     */
    public ChangeInterestRateTransaction(final int timestamp, final double interestRate) {
        this.description = "Interest rate of the account changed to " + interestRate;
        this.timestamp = timestamp;
    }
}
