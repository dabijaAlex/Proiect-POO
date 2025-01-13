package org.poo.app.commerciants;

import org.poo.app.accounts.Account;
import org.poo.fileio.CommerciantInput;

public class TechCommerciant extends Commerciant {
    public TechCommerciant(CommerciantInput input) {
        super(input, 10);
    }

     public double getCashbackAmount(double amount, Account account) {
        if(account.getTechDiscounts() > 0) {
            account.setTechDiscounts(account.getTechDiscounts() - 1);
            return 10.0 / 100.0 * amount;
        }
        return 0;
    }
}
