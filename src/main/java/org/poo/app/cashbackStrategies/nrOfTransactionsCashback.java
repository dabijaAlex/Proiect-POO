package org.poo.app.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.ClothesCommerciant;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.commerciants.FoodCommerciant;
import org.poo.app.commerciants.TechCommerciant;

import java.util.HashMap;

@Getter @Setter
public final class nrOfTransactionsCashback implements CashbackStrategy {
    public double getCashback(Commerciant commerciant, double amount, Account account) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double nrTranzactii = accountsMap.get(account);
        if(nrTranzactii == null || nrTranzactii != commerciant.getCashbackPercent() ) {
            return 0;
        }

//        return amount * commerciant.getCashbackPercent() / 100;

        return Math.round(amount * commerciant.getCashbackPercent() / 100 * 100.0) / 100.0;
    }


    public void addPaymentToMap(Commerciant commerciant, double amount, Account account, String currency) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double nrTranzactii = accountsMap.get(account);
        if(nrTranzactii == null) {
            accountsMap.put(account, Double.valueOf(1));
        } else {
            accountsMap.replace(account, nrTranzactii + 1);
        }
    }

}
