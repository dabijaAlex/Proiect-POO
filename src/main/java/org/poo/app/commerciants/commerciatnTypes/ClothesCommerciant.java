package org.poo.app.commerciants.commerciatnTypes;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public final class ClothesCommerciant extends Commerciant {
    private static final double CASHBACK_PERCENT = 5.0 / 100.0;

    /**
     * Constructor
     * @param input
     */
    public ClothesCommerciant(final CommerciantInput input) {
        super(input);
    }

    /**
     * Get coupon discount based on commerciant type
     * @param amount
     * @param account
     * @return
     */
    public double getCashbackAmount(final double amount, final Account account) {
        if (account.getClothesDiscounts() > 0) {
            account.setClothesDiscounts(account.getClothesDiscounts() - 1);
            return CASHBACK_PERCENT * amount;
        }
        return 0;
    }
}
