package org.poo.app.cashbackStrategies;

import org.poo.app.ExchangeRateGraph;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;


public final class SpendingThresholdStrategy implements CashbackStrategy {


    public void addPaymentToMap(final Commerciant commerciant, final double amount,
                                final Account account, final String currency) {
        double amountInRon = ExchangeRateGraph.makeConversion(currency, "RON", amount);
        account.setSpentAtCommerciant(account.getSpentAtCommerciant() + amountInRon);
    }


    @Override
    public double getCashback(final Commerciant commerciant, final double amount, final Account account) {
        User user = account.getUserRef();
        double spentMoneySoFar = account.getSpentAtCommerciant()
                + ExchangeRateGraph.makeConversion(account.getCurrency(), "RON", amount);
        if(spentMoneySoFar < 100) {
            return commerciant.getCashbackAmount(amount, account);
        }
        if(spentMoneySoFar < 300) {
            return user.getServicePlan().getLowCashback(amount) + commerciant.getCashbackAmount(amount, account);
        }
        if(spentMoneySoFar < 500) {
            return user.getServicePlan().getMedianCashback(amount) + commerciant.getCashbackAmount(amount, account);
        }
        return user.getServicePlan().getHighCashback(amount) + commerciant.getCashbackAmount(amount, account);
    }
}
