package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddAccountTransaction extends Transaction {
    private String description;
    private int timestamp;
    public AddAccountTransaction(String description, int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }
}
