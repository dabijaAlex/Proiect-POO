package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class NoClassicAccount extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public NoClassicAccount(final int timestamp) {
        this.timestamp = timestamp;
        this.description = "You do not have a classic account.";
    }
}
