package org.poo.app.plans;

public final class Gold extends ServicePlan{
    private String name = "Gold";

    @Override
    public double getCommissionAmount(final double amount) {
        return 0;
    }

    @Override
    public ServicePlan upgrade() {
        System.out.println("can t upgrade");
        return null;
    }
}
