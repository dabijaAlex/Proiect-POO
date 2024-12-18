package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SplitPaymentTransaction extends Transaction {
    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;

    public SplitPaymentTransaction(int timestamp, String description, double amount, List<String> involvedAccounts, String currency) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.currency = currency;
    }
}
