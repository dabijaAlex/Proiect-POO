package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final  class AddInterestTransaction extends Transaction {
    private double amount;
    private String currency;
    private String description;

    /**
     * Constructor
     * @param timestamp
     * @param amount
     * @param currency
     */
    public AddInterestTransaction(final int timestamp, final double amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.description = "Interest rate income";
    }
}
