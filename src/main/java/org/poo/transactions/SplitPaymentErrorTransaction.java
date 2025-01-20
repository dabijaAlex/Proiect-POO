package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class SplitPaymentErrorTransaction extends Transaction {
    private String description;
    private List<String> involvedAccounts;
    private String currency;
    private String splitPaymentType;
    private List<Double> amountForUsers;
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
                                             final String currency, final String error,
                                        final String type, final List<Double> amountEach) {
        super(true);

        this.timestamp = timestamp;
        this.description = description;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = type;
        this.amountForUsers = amountEach;

        this.currency = currency;
        this.error = "Account " + error + " has insufficient funds for a split payment.";
    }

}
