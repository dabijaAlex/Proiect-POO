package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class TooYoungCashWithdrawalTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public TooYoungCashWithdrawalTransaction(final int timestamp) {
        this.timestamp = timestamp;
        this.description = "You don't have the minimum age required.";
    }
}
