package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddInterestTransaction extends Transaction {
    private double amount;
    private String currency;
    private String description;
    public AddInterestTransaction(final int timestamp, final double amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.description = "Interest rate income";
    }
}
