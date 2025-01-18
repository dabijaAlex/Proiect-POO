package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public class Standard extends ServicePlan {
    private static final double LOW_CASHBACK_PERCENT = 0.1 / 100.0;
    private static final double MED_CASHBACK_PERCENT = 0.2 / 100.0;
    private static final double HIGH_CASHBACK_PERCENT = 0.25 / 100.0;
    private static final double UPGRADE_TO_GOLD_PRICE = 350;
    private static final double UPGRADE_TO_SILVER_PRICE = 100;


    private String name = "Standard";

    /**
     * upgrade plan to silver: price = 100 RON. Throw exception if not enough money
     * @param account
     * @throws InsufficientFundsException
     */
    @Override
    public void upgradeToSilver(final Account account) throws InsufficientFundsException {
        double upgradePrice = ExchangeRateGraph.makeConversion("RON",
                account.getCurrency(), UPGRADE_TO_SILVER_PRICE);
        if (account.getBalance() - upgradePrice < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - upgradePrice);
        account.getUserRef().setServicePlan(new Silver());
    }

    /**
     * upgrade plan to gold: price = 350 RON. Throw exception if not enough money
     * @param account
     * @throws InsufficientFundsException
     */
    @Override
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
     * standard and student plan low cashback = 0.1% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getLowCashback(final double amount) {
        return LOW_CASHBACK_PERCENT * amount;
    }

    /**
     * standard and student plan low cashback = 0.2% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getMedianCashback(final double amount) {
        return MED_CASHBACK_PERCENT * amount;
    }

    /**
     * standard and student plan low cashback = 0.25% amount at spending threshold commerciant
     * @param amount
     * @return
     */
    public double getHighCashback(final double amount) {
        return HIGH_CASHBACK_PERCENT * amount;
    }
}
