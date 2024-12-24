package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
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
        return amount * 0.1 / 100;
    }

    public ServicePlan upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - 250 * convRate < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - 250 * convRate);
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
}
