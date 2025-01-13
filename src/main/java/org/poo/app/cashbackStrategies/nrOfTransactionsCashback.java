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
        return commerciant.getCashbackAmount(amount, account);
    }


    public void addPaymentToMap(Commerciant commerciant, double amount, Account account, String currency) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double nrTranzactii = accountsMap.get(account);
        if(nrTranzactii == null) {
            accountsMap.put(account, 1.0);
            return;
        } else {
            nrTranzactii++;
            accountsMap.replace(account, nrTranzactii);
        }
        if(nrTranzactii == 2){
            account.addFoodDiscount();
        }
        if(nrTranzactii == 5){
            account.addClothesDiscount();
        }
        if(nrTranzactii == 10){
            account.addTechDiscount();
        }

    }
}
