package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;

@Getter @Setter
public abstract class ServicePlan {

    public double getCommissionAmount(final double amount) {
        return (double) Math.round(amount * 0.2 / 100 * 100) / 100;
    }

    public abstract ServicePlan upgradeToGold(Account account);
    public abstract ServicePlan upgradeToSilver(Account account);
    public void addPayment(final int amount) {

    }
    public abstract double getLowCashback(double amount);
    public abstract double getMedianCashback(double amount);
    public abstract double getHighCashback(double amount);


}
