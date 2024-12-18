package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class SplitPaymentErrorTransaction extends SplitPaymentTransaction {
    private String error;

    /**
     * Constructor that calls superclass constructor
     * @param timestamp
     * @param description
     * @param amount
     * @param involvedAccounts
     * @param currency
     * @param error
     */
    public SplitPaymentErrorTransaction(final int timestamp, final String description,
                                        final double amount, final List<String> involvedAccounts,
                                        final String currency, final String error) {
        super(timestamp, description, amount, involvedAccounts, currency);
        this.error = "Account " + error + " has insufficient funds for a split payment.";
    }
}
