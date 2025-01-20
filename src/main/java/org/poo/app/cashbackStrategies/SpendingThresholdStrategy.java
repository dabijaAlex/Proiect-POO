package org.poo.app.cashbackStrategies;

import org.poo.app.ExchangeRateGraph;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;


public final class SpendingThresholdStrategy implements CashbackStrategy {
    private static final double LOW_CASHBACK_THRESHOLD = 100;
    private static final double MED_CASHBACK_THRESHOLD = 300;
    private static final double HIGH_CASHBACK_THRESHOLD = 500;


    /**
     * add to the account the amount that has been spent at the spending threshold commerciant
     * @param commerciant
     * @param amount
     * @param account
     * @param currency
     */
    public void addPayment(final Commerciant commerciant, final double amount,
                                final Account account, final String currency) {
        double amountInRon = ExchangeRateGraph.makeConversion(currency, "RON", amount);
        account.setSpentAtCommerciant(account.getSpentAtCommerciant() + amountInRon);
    }


    /**
     * give out cashback based on the amount that has been spent at these
     *      types of commerciants and also the users service plan
     * Also check for any available coupons and make the discount if they exist
     * @param commerciant
     * @param amount
     * @param account
     * @return
     */
    @Override
    public double getCashback(final Commerciant commerciant, final double amount,
                              final Account account) {
        User user = account.getUserRef();
        double spentMoneySoFar = account.getSpentAtCommerciant()
                + ExchangeRateGraph.makeConversion(account.getCurrency(), "RON", amount);
        if (spentMoneySoFar < LOW_CASHBACK_THRESHOLD) {
            return commerciant.getCashbackAmount(amount, account);
        }
        if (spentMoneySoFar < MED_CASHBACK_THRESHOLD) {
            return user.getServicePlan().getLowCashback(amount)
                    + commerciant.getCashbackAmount(amount, account);
        }
        if (spentMoneySoFar < HIGH_CASHBACK_THRESHOLD) {
            return user.getServicePlan().getMedianCashback(amount)
                    + commerciant.getCashbackAmount(amount, account);
        }
        return user.getServicePlan().getHighCashback(amount)
                + commerciant.getCashbackAmount(amount, account);
    }
}
