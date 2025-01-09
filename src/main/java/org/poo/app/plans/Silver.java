package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;

@Getter @Setter
public final class Silver extends ServicePlan{
    private String name = "Silver";
    private int nrPaymentsOver300 = 0;

    @Override
    public double getCommissionAmount(final double amount, final String currency) {

        if(ExchangeRateGraph.makeConversion(currency, "RON", amount) <= 500)
            return 0;
        return Math.round(amount * 0.1 / 100.0 * 100.0) / 100.0;
    }

    public void upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - Math.round(250 * convRate * 100.0) / 100.0 < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(Math.round((account.getBalance() - 250 * convRate) * 100.0) / 100.0);
//        return new Gold();
    }

    public void upgradeToSilver(Account account) throws AlreadyHasPlanException {
        System.out.println("can t upgrade to silver");
        throw new AlreadyHasPlanException();
    }


    public void addPayment(final double amount, String currency, Account account, User user) {
        double convRate = ExchangeRateGraph.convertRate(currency, "RON");
        if(amount * convRate > 300) {
            nrPaymentsOver300 += 1;
        }
        if(nrPaymentsOver300 >= 5) {
            for(Account acc: user.getAccounts()) {
                acc.setServicePlan(new Gold());
            }
//            account.setServicePlan(new Gold());
        }
    }

    public Silver getThisPlan() {
        return this;
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
