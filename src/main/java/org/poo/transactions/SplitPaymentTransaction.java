package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public final class SplitPaymentTransaction extends Transaction {
    private String description;
    private List<String> involvedAccounts;
    private String currency;
    private String splitPaymentType;
    private List<Double> amountForUsers;

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param involvedAccounts
     * @param currency
     */
    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final List<String> involvedAccounts,
                                   final String currency, final String type,
                                   final List<Double> amountForUsers) {
        super(true);

        this.timestamp = timestamp;
        this.description = description;
        this.involvedAccounts = involvedAccounts;
        this.currency = currency;
        this.splitPaymentType = type;
        this.amountForUsers = amountForUsers;
    }

}
