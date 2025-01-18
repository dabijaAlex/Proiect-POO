package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;
import org.poo.transactions.UpgradePlanTransaction;

@Getter @Setter
public final class Silver extends ServicePlan {
    private static final double LOW_CASHBACK_PERCENT = 0.3 / 100.0;
    private static final double MED_CASHBACK_PERCENT = 0.4 / 100.0;
    private static final double HIGH_CASHBACK_PERCENT = 0.5 / 100.0;
    private static final double COMMISSION_PERCENT = 0.1 / 100.0;
    private static final double NO_COMMISSION_LIMIT = 500;
    private static final double UPGRADE_TO_GOLD_PRICE = 250;
    private static final double NUMBER_300 = 300;
    private static final double NUMBER_OF_TRANS_FOR_AUTO_UPGRADE = 5;
    private String name = "Silver";
    private int nrPaymentsOver300 = 0;

    /**
     * commission amount for silver account
     * commission = 0 if amount spent < 500 ron
     * commission = 0.1% amount if amount >= 500 ron
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double getCommissionAmount(final double amount, final String currency) {
        if (ExchangeRateGraph.makeConversion(currency, "RON", amount) <= NO_COMMISSION_LIMIT) {
            return 0;
        }
        return amount * COMMISSION_PERCENT;
    }

    /**
     * upgrade plan to gold: price = 250 RON. Throw exception if not enough money
     * @param account
     * @throws InsufficientFundsException
     */
    public void upgradeToGold(final Account account) throws InsufficientFundsException {
        double upgradePrice = ExchangeRateGraph.makeConversion("RON",
                account.getCurrency(), UPGRADE_TO_GOLD_PRICE);
        if (account.getBalance() - upgradePrice < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - upgradePrice);
        account.getUserRef().setServicePlan(new Gold());
    }

    /**
     * can t upgrade to silver: already this plan
     * @param account
     * @throws AlreadyHasPlanException
     */
    public void upgradeToSilver(final Account account) throws AlreadyHasPlanException {
        throw new AlreadyHasPlanException();
    }

    /**
     * add payment to this instance if amount > 300
     * promote user plan to gold at 5 payments
     * @param amount
     * @param currency
     * @param account
     * @param user
     * @param timestamp
     */
    public void addPayment(final double amount, final String currency,
                           final Account account, final User user, final int timestamp) {
        double amountInRon = ExchangeRateGraph.makeConversion(currency, "RON", amount);
        if (amountInRon > NUMBER_300) {
            nrPaymentsOver300 += 1;
        }
        if (nrPaymentsOver300 >= NUMBER_OF_TRANS_FOR_AUTO_UPGRADE) {
            for (Account acc: user.getAccounts()) {
                acc.addTransaction(new UpgradePlanTransaction(acc.getIban(), "gold", timestamp));
            }
            user.setServicePlan(new Gold());
        }
    }

    /**
     * silver plan low cashback = 0.3% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getLowCashback(final double amount) {
        return LOW_CASHBACK_PERCENT * amount;
    }

    /**
     * silver plan low cashback = 0.4% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getMedianCashback(final double amount) {
        return MED_CASHBACK_PERCENT * amount;
    }

    /**
     * silver plan low cashback = 0.5% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getHighCashback(final double amount) {
        return HIGH_CASHBACK_PERCENT * amount;
    }
}
