package org.poo.app.plans;

import org.poo.app.accounts.Account;

public final class Gold extends ServicePlan {
    private static final double LOW_CASHBACK_PERCENT = 0.5 / 100.0;
    private static final double MED_CASHBACK_PERCENT = 0.55 / 100.0;
    private static final double HIGH_CASHBACK_PERCENT = 0.7 / 100.0;


    /**
     * Get commission = 0 for gold account
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double getCommissionAmount(final double amount, final String currency) {
        return 0;
    }

    /**
     * upgrading fails because silver is lower tier
     * @param account
     * @throws CannotDowngradePlanException
     */
    @Override
    public void upgradeToSilver(final Account account) throws CannotDowngradePlanException {
        throw new CannotDowngradePlanException();
    }

    /**
     * upgrading fails because it is same plan type
     * @param account
     * @throws AlreadyHasPlanException
     */
    @Override
    public void upgradeToGold(final Account account) throws AlreadyHasPlanException {
        throw new AlreadyHasPlanException();
    }


    /**
     * gold plan low cashback = 0.5% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getLowCashback(final double amount) {
        return LOW_CASHBACK_PERCENT * amount;
    }

    /**
     * gold plan med cashback = 0.55% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getMedianCashback(final double amount) {
        return MED_CASHBACK_PERCENT * amount;
    }

    /**
     * gold plan high cashback = 0.7% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getHighCashback(final double amount) {
        return HIGH_CASHBACK_PERCENT * amount;
    }
}
