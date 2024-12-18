package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class FailedDeleteAccountTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public FailedDeleteAccountTransaction(final int timestamp) {
        this.description = "Account couldn't be deleted - there are funds remaining";
        this.timestamp = timestamp;
    }
}
