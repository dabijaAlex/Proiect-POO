package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.InsufficientFundsException;
import org.poo.transactions.UpgradePlanTransaction;

@Getter @Setter
public final class Silver extends ServicePlan{
    private String name = "Silver";
    private int nrPaymentsOver300 = 0;

    @Override
    public double getCommissionAmount(final double amount, final String currency) {

        if(ExchangeRateGraph.makeConversion(currency, "RON", amount) <= 500)
            return 0;
        return amount * 0.1 / 100.0;
    }

    public void upgradeToGold(Account account) throws InsufficientFundsException {
        double convRate = ExchangeRateGraph.convertRate("RON", account.getCurrency());
        if(account.getBalance() - 250 * convRate < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - 250 * convRate);
        account.getUserRef().setServicePlan(new Gold());
    }

    public void upgradeToSilver(Account account) throws AlreadyHasPlanException {
        throw new AlreadyHasPlanException();
    }


    public void addPayment(final double amount, String currency, Account account, User user, int timestamp) {
        double convRate = ExchangeRateGraph.convertRate(currency, "RON");
        if(amount * convRate > 300) {
            nrPaymentsOver300 += 1;
        }
        if(nrPaymentsOver300 >= 5) {
            for(Account acc: user.getAccounts()) {
                acc.setServicePlan(new Gold());
                acc.addTransaction(new UpgradePlanTransaction(acc.getIBAN(), "gold", timestamp));
            }
        }
    }

    public Silver getThisPlan() {
        return this;
    }



    public double getLowCashback(double amount) {
        return 0.3 / 100 * amount;
    }
    public double getMedianCashback(double amount) {
        return 0.4 / 100 * amount;
    }
    public double getHighCashback(double amount) {
        return 0.5 / 100 * amount;
    }
}
