package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;

@Getter @Setter
public abstract class ServicePlan {
    private static final double COMMISSION_PERCENT = 0.2 / 100.0;

    /**
     * student and standad accounts commission = 0.2% of amount
     * @param amount
     * @param currency
     * @return
     */
    public double getCommissionAmount(final double amount, final String currency) {
        return amount * COMMISSION_PERCENT;
    }

    /**
     * upgrade plan to gold based on current plan
     * @param account
     */
    public abstract void upgradeToGold(Account account);

    /**
     * upgrade plan to silver based on plan
     * @param account
     */
    public abstract void upgradeToSilver(Account account);

    /**
     * for silver we need it because at 5 payments the plan gets auto promoted to gold
     * @param amount
     * @param currency
     * @param account
     * @param user
     * @param timestamp
     */
    public void addPayment(final double amount, final String currency, final Account account,
                           final User user, final int timestamp) {

    }

    /**
     * get low cashback at spending threshold commerciant based on plan type
     * @param amount
     * @return
     */
    public abstract double getLowCashback(double amount);

    /**
     * get med cashback at spending threshold commerciant based on plan type
     * @param amount
     * @return
     */
    public abstract double getMedianCashback(double amount);

    /**
     * get high cashback at spending threshold commerciant based on plan type
     * @param amount
     * @return
     */
    public abstract double getHighCashback(double amount);
}
