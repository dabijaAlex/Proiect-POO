package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public final class RejectedSplitPaymentTransaction extends Transaction {
    private String description;
    private String splitPaymentType;
    private String currency;
    private List<Double> amountForUsers;
    private List<String> involvedAccounts;
    private String error = "One user rejected the payment.";

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param type
     * @param currency
     * @param amountForUsers
     * @param involvedAccounts
     */
    public RejectedSplitPaymentTransaction(final int timestamp, final String description,
                                           final String type, final String currency,
                                           final List<Double> amountForUsers,
                                           final List<String> involvedAccounts) {
        this.timestamp = timestamp;
        this.description = description;
        this.splitPaymentType = type;
        this.currency = currency;
        this.amountForUsers = amountForUsers;
        this.involvedAccounts = involvedAccounts;
    }
}
