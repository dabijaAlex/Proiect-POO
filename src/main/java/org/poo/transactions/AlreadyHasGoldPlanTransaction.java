package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlreadyHasGoldPlanTransaction extends Transaction {
    private String description;
    public AlreadyHasGoldPlanTransaction(int timestamp, String plan) {
        description = "The user already has the " + plan + " plan.";
        this.timestamp = timestamp;
    }
}
