package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class PayOnlineTransaction extends Transaction {
    private String description;
    private double amount;
    private String commerciant;

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param amount
     * @param commerciant
     */
    public PayOnlineTransaction(final int timestamp, final String description,
                                final double amount, final String commerciant) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.commerciant = commerciant;
    }

    /**
     * Add spending transaction to a list
     * @param transactions
     */
    public void addSpendingTransactionToList(final List<Transaction> transactions) {
        transactions.add(this);
    }

    /**
     * get the amount of transaction
     * @return
     */
    @Override
    public double getAmountDouble() {
        return amount;
    }

    /**
     * get commerciant of transaction
     * @return
     */
    @Override
    public String getCommerciant2() {
        return commerciant;
    }
}
