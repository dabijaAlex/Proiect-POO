package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class ServicePlan {

    public double getCommissionAmount(final double amount) {
        return amount * 0.2 / 100;
    }

    public abstract ServicePlan upgrade();
}
