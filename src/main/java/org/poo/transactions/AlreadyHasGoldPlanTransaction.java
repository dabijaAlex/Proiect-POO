package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class AlreadyHasGoldPlanTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     * @param plan
     */
    public AlreadyHasGoldPlanTransaction(final int timestamp, final String plan) {
        description = "The user already has the " + plan + " plan.";
        this.timestamp = timestamp;
    }
}
