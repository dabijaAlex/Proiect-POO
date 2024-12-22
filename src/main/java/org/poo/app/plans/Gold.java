package org.poo.app.plans;

import org.poo.app.Account;

public final class Gold extends ServicePlan{
    private String name = "Gold";

    @Override
    public double getCommissionAmount(final double amount) {
        return 0;
    }

    @Override
    public ServicePlan upgradeToSilver(Account account) throws CannotDowngradePlanException{
        System.out.println("can t upgrade to Silver");
        throw new CannotDowngradePlanException();
    }

    @Override
    public ServicePlan upgradeToGold(Account account) throws AlreadyHasPlanException{
        System.out.println("can t upgrade to Gold");
        throw new AlreadyHasPlanException();
    }
}
