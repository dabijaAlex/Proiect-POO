package org.poo.app.plans;

import org.poo.app.accounts.Account;

public final class Gold extends ServicePlan{
    private String name = "Gold";

    @Override
    public double getCommissionAmount(final double amount, String currency) {
        return 0;
    }

    @Override
    public void upgradeToSilver(Account account) throws CannotDowngradePlanException{
        System.out.println("can t upgrade to Silver");
        throw new CannotDowngradePlanException();
    }

    @Override
    public void upgradeToGold(Account account) throws AlreadyHasPlanException{
        System.out.println("can t upgrade to Gold");
        throw new AlreadyHasPlanException();
    }


    public double getLowCashback(double amount) {
        return Math.round(0.5 / 100 * amount * 100.0) / 100.0;
    }
    public double getMedianCashback(double amount) {
        return Math.round(0.55 / 100 * amount * 100.0) / 100.0;
    }
    public double getHighCashback(double amount) {
        return Math.round(0.7 / 100 * amount * 100.0) / 100.0;
    }

    public Gold getThisPlan() {
        return this;
    }
}
