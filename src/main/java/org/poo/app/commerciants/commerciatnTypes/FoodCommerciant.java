package org.poo.app.commerciants.commerciatnTypes;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public final class FoodCommerciant extends Commerciant {
    private static final double CASHBACK_PERCENT = 2.0 / 100.0;

    /**
     * Constructor
     * @param input
     */
    public FoodCommerciant(final CommerciantInput input) {
        super(input);
    }

    /**
     * Get coupon discount based on commerciant type
     * @param amount
     * @param account
     * @return
     */
    public double getCashbackAmount(final double amount, final Account account) {
        if (account.getFoodDiscounts() > 0) {
            account.setFoodDiscounts(account.getFoodDiscounts() - 1);
            return CASHBACK_PERCENT * amount;
        }
        return 0;
    }
}
