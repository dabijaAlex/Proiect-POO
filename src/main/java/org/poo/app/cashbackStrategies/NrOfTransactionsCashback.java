package org.poo.app.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.HashMap;

@Getter @Setter
public final class NrOfTransactionsCashback implements CashbackStrategy {
    public double getCashback(final Commerciant commerciant, final double amount, final Account account) {
        return commerciant.getCashbackAmount(amount, account);
    }


    public void addPaymentToMap(final Commerciant commerciant, final double amount,
                                final Account account, final String currency) {
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
