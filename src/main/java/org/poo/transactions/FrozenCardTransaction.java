package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FrozenCardTransaction extends Transaction {
    private int timestamp;
    private String description;

    public FrozenCardTransaction(int timestamp) {
        this.timestamp = timestamp;
        this.description = "The card is frozen";
    }
}
