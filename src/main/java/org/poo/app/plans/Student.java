package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public final class Student extends ServicePlan{
    private String name = "Student";

    @Override
    public double getCommissionAmount(final double amount, String currency) {
        return 0;
    }

    @Override
    public void upgradeToSilver(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(100.0 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(Math.round((account.getBalance() - 100.0 * convRate) * 100.0) / 100.0);

//        return new Silver();
    }

    @Override
    public void upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(350.0 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(Math.round((account.getBalance() - 350.0 * convRate) * 100.0) / 100.0);


//        return new Gold();
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

    public Student getThisPlan() {
        return this;
    }
}
