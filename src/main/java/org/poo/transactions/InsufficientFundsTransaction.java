package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class InsufficientFundsTransaction extends Transaction {
    private String description;


    /**
     * Constructor
     * @param timestamp
     */
    public InsufficientFundsTransaction(final int timestamp) {
        this.timestamp = timestamp;
        this.description = "Insufficient funds";
    }
}
