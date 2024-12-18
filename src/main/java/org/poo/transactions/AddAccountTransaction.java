package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class AddAccountTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public AddAccountTransaction(final int timestamp) {
        this.description = "New account created";
        this.timestamp = timestamp;
    }
}
