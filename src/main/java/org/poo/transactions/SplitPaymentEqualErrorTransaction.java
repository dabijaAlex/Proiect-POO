package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter @Setter
public final class SplitPaymentEqualErrorTransaction extends Transaction {
    private double amount;
    private String description;
    private List<String> involvedAccounts;
    private String currency;
    private String SplitPaymentType;
    private String error;


    /**
     * Constructor that calls superclass constructor
     * @param timestamp
     * @param description
     * @param involvedAccounts
     * @param currency
     * @param error
     */
    public SplitPaymentEqualErrorTransaction(final int timestamp, final String description,
                                        final List<String> involvedAccounts,
                                        final String currency, final String error, final String type, final List<Double> amountEach, final double amount) {
        super(true);
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.involvedAccounts = involvedAccounts;
        this.SplitPaymentType = type;

        this.currency = currency;
        this.error = "Account " + error + " has insufficient funds for a split payment.";
    }

}
//tre sa dau remove la acc din usersasdasd
