package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FailedDeleteAccountTransaction extends Transaction {
    private String description;
    private int timestamp;

    public FailedDeleteAccountTransaction(int timestamp) {
        this.description = "Account couldn't be deleted - there are funds remaining";
        this.timestamp = timestamp;
    }
}
