package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class CashWithdrawalTransaction extends Transaction {
    private String description;
    private double amount;
    public CashWithdrawalTransaction(final int timestamp, final double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.description = "Cash withdrawal of " + amount;
    }
}
