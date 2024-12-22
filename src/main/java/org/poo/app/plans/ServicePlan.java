package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;

@Getter @Setter
public abstract class ServicePlan {

    public double getCommissionAmount(final double amount) {
        return amount * 0.2 / 100;
    }

    public abstract ServicePlan upgradeToGold(Account account);
    public abstract ServicePlan upgradeToSilver(Account account);
}
