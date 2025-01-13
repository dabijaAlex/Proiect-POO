package org.poo.app.plans;

import org.poo.app.accounts.Account;

public final class Gold extends ServicePlan{

    @Override
    public double getCommissionAmount(final double amount, String currency) {
        return 0;
    }

    @Override
    public void upgradeToSilver(Account account) throws CannotDowngradePlanException{
        throw new CannotDowngradePlanException();
    }

    @Override
    public void upgradeToGold(Account account) throws AlreadyHasPlanException{
        throw new AlreadyHasPlanException();
    }


    public double getLowCashback(double amount) {
        return 0.5 / 100 * amount;
    }
    public double getMedianCashback(double amount) {
        return 0.55 / 100 * amount;
    }
    public double getHighCashback(double amount) {
        return 0.7 / 100 * amount;
    }
}
