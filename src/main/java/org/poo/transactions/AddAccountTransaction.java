package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddAccountTransaction extends Transaction {
    private String description;
    public AddAccountTransaction(int timestamp) {
        this.description = "New account created";
        this.timestamp = timestamp;
    }
}
