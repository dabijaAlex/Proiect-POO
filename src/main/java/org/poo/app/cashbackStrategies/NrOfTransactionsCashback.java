package org.poo.app.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.HashMap;

@Getter @Setter
public final class NrOfTransactionsCashback implements CashbackStrategy {
    private static final double NR_TRANSACTIONS_FOR_FOOD_COUPON = 2;
    private static final double NR_TRANSACTIONS_FOR_CLOTHES_COUPON = 5;
    private static final double NR_TRANSACTIONS_FOR_TECH_COUPON = 10;


    /**
     * get discout if there are any coupons available
     * @param commerciant
     * @param amount
     * @param account
     * @return
     */
    public double getCashback(final Commerciant commerciant, final double amount,
                              final Account account) {
        return commerciant.getCashbackAmount(amount, account);
    }

    /**
     * add to the commerciant's db the transaction and give out
     *      coupon if threshold has been reached
     * @param commerciant
     * @param amount
     * @param account
     * @param currency
     */
    public void addPayment(final Commerciant commerciant, final double amount,
                                final Account account, final String currency) {
        HashMap<Account, Double> accountsMap = commerciant.getListAccountsThatPayedHere();
        Double nrTranzactii = accountsMap.get(account);
        if (nrTranzactii == null) {
            accountsMap.put(account, 1.0);
            return;
        } else {
            nrTranzactii++;
            accountsMap.replace(account, nrTranzactii);
        }
        if (nrTranzactii == NR_TRANSACTIONS_FOR_FOOD_COUPON) {
            account.addFoodDiscount();
        }
        if (nrTranzactii == NR_TRANSACTIONS_FOR_CLOTHES_COUPON) {
            account.addClothesDiscount();
        }
        if (nrTranzactii == NR_TRANSACTIONS_FOR_TECH_COUPON) {
            account.addTechDiscount();
        }

    }
}
