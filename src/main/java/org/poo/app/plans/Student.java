package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Student extends ServicePlan{
    private String name = "Student";

    @Override
    public double getCommissionAmount(final double amount) {
        return 0;
    }

    @Override
    public ServicePlan upgrade() {
        return new Silver();
    }


}
