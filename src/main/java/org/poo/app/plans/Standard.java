package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public final class Standard extends ServicePlan {
    private String name = "Standard";

    @Override
    public void upgradeToSilver(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - 100.0 * convRate < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance() - 100.0 * convRate);
        account.getUserRef().setServicePlan(new Silver());


//        return new Silver();
    }

    @Override
    public void upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        double price = ExchangeRateGraph.makeConversion("RON", account.getCurrency(), 350);
        if(account.getBalance() - 350.0 * convRate < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - 350.0 * convRate);
        account.getUserRef().setServicePlan(new Gold());


//        return new Gold();
    }

    public double getLowCashback(double amount) {
        return 0.1 / 100 * amount;
    }
    public double getMedianCashback(double amount) {
        return 0.2 / 100 * amount;
    }
    public double getHighCashback(double amount) {
        return 0.25 / 100 * amount;
    }

    public Standard getThisPlan() {
        return this;
    }
}
