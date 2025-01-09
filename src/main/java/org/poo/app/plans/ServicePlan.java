package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;

@Getter @Setter
public abstract class ServicePlan {

    public double getCommissionAmount(final double amount, String currency) {
        return (double) Math.round(amount * 0.2 / 100.0 * 100.0) / 100.0;
    }

    public abstract void upgradeToGold(Account account);
    public abstract void upgradeToSilver(Account account);
    public void addPayment(final double amount, String currency, Account account, User user) {

    }
    public abstract double getLowCashback(double amount);
    public abstract double getMedianCashback(double amount);
    public abstract double getHighCashback(double amount);

    public ServicePlan getThisPlan() {
        return this;
    }

}
