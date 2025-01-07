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
    public ServicePlan upgradeToSilver(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(100.0 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance() - Math.round(100.0 * convRate * 100.0) / 100.0);

        return new Silver();
    }

    @Override
    public ServicePlan upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(350.0 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - Math.round(350.0 * convRate * 100.0) / 100.0);

        return new Gold();
    }

    public double getLowCashback(double amount) {
        return Math.round(0.1 / 100 * amount * 100.0) / 100.0;
    }
    public double getMedianCashback(double amount) {
        return Math.round(0.2 / 100 * amount * 100.0) / 100.0;
    }
    public double getHighCashback(double amount) {
        return Math.round(0.25 / 100 * amount * 100.0) / 100.0;
    }
}
