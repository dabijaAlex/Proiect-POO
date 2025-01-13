package org.poo.app.commerciants;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public class ClothesCommerciant extends Commerciant {
    public ClothesCommerciant(CommerciantInput input) {
        super(input, 5);
    }

    public double getCashbackAmount(double amount, Account account) {
        if(account.getClothesDiscounts() > 0) {
            account.setClothesDiscounts(account.getClothesDiscounts() - 1);
            return 5.0 / 100.0 * amount;
        }
        return 0;
    }
}
