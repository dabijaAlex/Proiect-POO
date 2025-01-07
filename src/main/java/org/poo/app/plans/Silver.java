package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public final class Silver extends ServicePlan{
    private String name = "Silver";
    private int nrPaymentsOver300 = 0;

    @Override
    public double getCommissionAmount(final double amount) {

        if(amount <= 500)
            return 0;
        return Math.round(amount * 0.1 / 100 * 100) / 100.0;
    }

    public ServicePlan upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(250 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - Math.round(250 * convRate * 100.0) / 100.0);
        return new Gold();
    }

    public ServicePlan upgradeToSilver(Account account) throws AlreadyHasPlanException {
        System.out.println("can t upgrade to silver");
        throw new AlreadyHasPlanException();
    }


    public void addPayment(final int amount, String currency, Account account) {
        double convRate = ExchangeRateGraph.convertRate(currency, "RON");
        if(amount * convRate > 300) {
            nrPaymentsOver300 += 1;
        }
        if(nrPaymentsOver300 >= 5) {
            account.setServicePlan(new Gold());
        }
    }



    public double getLowCashback(double amount) {
        return Math.round(0.3 / 100 * amount * 100.0) / 100.0;
    }
    public double getMedianCashback(double amount) {
        return Math.round(0.4 / 100 * amount * 100.0) / 100.0;
    }
    public double getHighCashback(double amount) {
        return Math.round(0.5 / 100 * amount * 100.0) / 100.0;
    }
}
