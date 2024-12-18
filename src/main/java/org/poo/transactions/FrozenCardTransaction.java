package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class FrozenCardTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public FrozenCardTransaction(final int timestamp) {
        this.timestamp = timestamp;
        this.description = "The card is frozen";
    }
}
