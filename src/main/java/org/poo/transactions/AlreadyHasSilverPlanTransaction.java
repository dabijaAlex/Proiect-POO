package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class AlreadyHasSilverPlanTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     * @param plan
     */
    public AlreadyHasSilverPlanTransaction(final int timestamp, final String plan) {
        description = "The user already has the " + plan + " plan.";
        this.timestamp = timestamp;
    }
}
