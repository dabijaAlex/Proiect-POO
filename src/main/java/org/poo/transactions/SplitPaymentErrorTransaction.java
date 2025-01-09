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
     * @param involvedAccounts
     * @param currency
     * @param error
     */
    public SplitPaymentErrorTransaction(final int timestamp, final String description,
                                        final List<String> involvedAccounts,
                                        final String currency, final String error, final String type, final List<Double> amountEach) {
        super(timestamp, description, involvedAccounts, currency, type, amountEach);
        this.error = "Account " + error + " has insufficient funds for a split payment.";
    }
}
