package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
public class SplitPaymentEqualTransaction extends Transaction {
    private double amount;
    private String description;
    private List<String> involvedAccounts;
    private String currency;
    private String SplitPaymentType;

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param involvedAccounts
     * @param currency
     */
    public SplitPaymentEqualTransaction(final int timestamp, final String description,
                                   final List<String> involvedAccounts,
                                   final String currency, final String type, final List<Double> amountForUsers, final double amount) {
        super(true);

        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.involvedAccounts = involvedAccounts;
        this.currency = currency;
        this.SplitPaymentType = type;
    }

}
