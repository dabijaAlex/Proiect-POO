package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PayOnlineTransaction extends Transaction {
    private int timestamp;
    private String description;
    private double amount;
    private String commerciant;

    public PayOnlineTransaction(int timestamp, String description, double amount, String commerciant) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.commerciant = commerciant;
    }
}
