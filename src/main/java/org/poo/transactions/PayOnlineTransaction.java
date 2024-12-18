package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PayOnlineTransaction extends Transaction {
    private String description;
    private double amount;
    private String commerciant;

    public PayOnlineTransaction(int timestamp, String description, double amount, String commerciant) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.commerciant = commerciant;
    }

    public void addSpendingTransactionToList(List<Transaction> transactions) {
        transactions.add(this);
    }

    @Override
    public double getAmountDouble() {
        return amount;
    }
    @Override
    public String getCommerciant2() {
        return commerciant;
    }
}
