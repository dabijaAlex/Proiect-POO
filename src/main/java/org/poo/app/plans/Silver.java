package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Silver extends ServicePlan{
    private String name = "Silver";

    @Override
    public double getCommissionAmount(final double amount) {
        if(amount <= 500)
            return 0;
        return amount * 0.1 / 100;
    }

    @Override
    public ServicePlan upgrade() {
        return new Gold();
    }
}
