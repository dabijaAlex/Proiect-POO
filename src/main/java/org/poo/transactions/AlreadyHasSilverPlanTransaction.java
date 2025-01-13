package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlreadyHasSilverPlanTransaction extends Transaction {
    private String description;
    public AlreadyHasSilverPlanTransaction(int timestamp, String plan) {
        description = "The user already has the " + plan + " plan.";
        this.timestamp = timestamp;
    }
}
