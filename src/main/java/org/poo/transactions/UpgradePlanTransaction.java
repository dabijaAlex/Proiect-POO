package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class UpgradePlanTransaction extends Transaction {
    private String accountIBAN;
    private String description = "Upgrade plan";
    private String newPlanType;
    private int timestamp;

    /**
     * Constructor
     * @param accountIBAN
     * @param newPlanType
     * @param timestamp
     */
    public UpgradePlanTransaction(final String accountIBAN, final String newPlanType,
                                  final int timestamp) {
        this.accountIBAN = accountIBAN;
        this.newPlanType = newPlanType;
        this.timestamp = timestamp;
    }
}
