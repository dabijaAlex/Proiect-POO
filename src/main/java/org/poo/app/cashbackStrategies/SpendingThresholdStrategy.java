package org.poo.app.cashbackStrategies;

import org.poo.app.ExchangeRateGraph;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.HashMap;

public class SpendingThresholdStrategy implements CashbackStrategy {


    public void addPaymentToMap(Commerciant commerciant, double amount, Account account, String currency) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double spentMoneySoFar = accountsMap.get(account);

        if(spentMoneySoFar == null) {
            accountsMap.put(account, Double.valueOf(ExchangeRateGraph.makeConversion(currency, "RON", amount)));
        } else {
            accountsMap.replace(account, spentMoneySoFar + ExchangeRateGraph.makeConversion(currency, "RON", amount));
        }
    }

    @Override
    public double getCashback(Commerciant commerciant, double amount, Account account) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double spentMoneySoFar = accountsMap.get(account);
        if(spentMoneySoFar == null) {
            return 0;
        }
        if(spentMoneySoFar < 100) {
            return 0;
        }
        if(spentMoneySoFar < 300) {
            return account.getServicePlan().getLowCashback(amount);
        }
        if(spentMoneySoFar < 500) {
            return account.getServicePlan().getMedianCashback(amount);
        }
        return account.getServicePlan().getHighCashback(amount);
    }
}
