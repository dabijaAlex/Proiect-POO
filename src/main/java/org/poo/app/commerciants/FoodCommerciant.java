package org.poo.app.commerciants;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public class FoodCommerciant extends Commerciant {
    public FoodCommerciant(CommerciantInput input) {
        super(input, 2);
    }

    public double getCashbackAmount(double amount, Account account) {
        if(account.getFoodDiscounts() > 0) {
            account.setFoodDiscounts(account.getFoodDiscounts() - 1);
            return 2.0 / 100.0 * amount;
        }
        return 0;
    }
}
