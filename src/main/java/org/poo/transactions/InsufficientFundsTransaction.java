package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InsufficientFundsTransaction extends Transaction {
    private int timestamp;
    private String description;

    public InsufficientFundsTransaction(int timestamp) {
        this.timestamp = timestamp;
        this.description = "Insufficient funds";
    }
}
