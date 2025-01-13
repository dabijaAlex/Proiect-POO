package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WithdrewSavingsTransaction extends Transaction {
    private double amount;
    private String classicAccountIBAN;
    private String description;
    private String savingsAccountIBAN;
    public WithdrewSavingsTransaction(final double amount, final String classicAccountIBAN, final String savingsAccountIBAN, final int timestamp) {
        this.amount = amount;
        this.classicAccountIBAN = classicAccountIBAN;
        this.description = "Savings withdrawal";
        this.savingsAccountIBAN = savingsAccountIBAN;
        this.timestamp = timestamp;
    }
}
