package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public final class Student extends ServicePlan{
    private String name = "Student";

    @Override
    public double getCommissionAmount(final double amount) {
        return 0;
    }

    @Override
    public ServicePlan upgradeToSilver(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - 100 * convRate < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - 100 * convRate);

        return new Silver();
    }

    @Override
    public ServicePlan upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - 350 * convRate < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - 350 * convRate);

        return new Gold();
    }

}
