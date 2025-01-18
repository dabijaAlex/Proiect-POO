package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class WithdrewSavingsTransaction extends Transaction {
    private double amount;
    private String classicAccountIBAN;
    private String description;
    private String savingsAccountIBAN;

    /**
     * Constructor
     * @param amount
     * @param classicAccountIBAN
     * @param savingsAccountIBAN
     * @param timestamp
     */
    public WithdrewSavingsTransaction(final double amount,
                                      final String classicAccountIBAN,
                                      final String savingsAccountIBAN,
                                      final int timestamp) {
        this.amount = amount;
        this.classicAccountIBAN = classicAccountIBAN;
        this.description = "Savings withdrawal";
        this.savingsAccountIBAN = savingsAccountIBAN;
        this.timestamp = timestamp;
    }
}
