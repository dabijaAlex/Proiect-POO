package org.poo.app.commerciants;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public final class TechCommerciant extends Commerciant {
    private static final double CASHBACK_PERCENT = 10.0 / 100.0;

    /**
     * Constructor
     * @param input
     */
    public TechCommerciant(final CommerciantInput input) {
        super(input);
    }

    /**
     * Get coupon discount based on commerciant type
     * @param amount
     * @param account
     * @return
     */
     public double getCashbackAmount(final double amount, final Account account) {
        if (account.getTechDiscounts() > 0) {
            account.setTechDiscounts(account.getTechDiscounts() - 1);
            return CASHBACK_PERCENT * amount;
        }
        return 0;
    }
}
