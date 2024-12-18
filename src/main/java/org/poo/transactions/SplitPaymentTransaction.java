package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SplitPaymentTransaction extends Transaction {
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param amount
     * @param involvedAccounts
     * @param currency
     */
    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final double amount, final List<String> involvedAccounts,
                                   final String currency) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.currency = currency;
    }
}
