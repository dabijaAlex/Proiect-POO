package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Student extends Standard {
    private String name = "Student";

    /**
     * student commission amount for transactions = 0
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double getCommissionAmount(final double amount, final String currency) {
        return 0;
    }
}
