package org.poo.app.accounts.userTypes;

import org.poo.app.Card;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.accounts.Account;

public class Manager extends BAccUser {
    public Manager(String email, String username) {
        super(email, username);
    }

    public void changeSpendingLimit(Account account, double limit) {
        throw new ChangeSpendingLimitException();
    }

    public void changeDepositLimit(Account account, double limit) {
        throw new ChangeDepositLimitException();
    }

    public void setMinBalance(Account account, double minBalance) {
        throw new NotAuthorizedException();
    }

    public void setAlias(Account account, String alias) {
        throw new NotAuthorizedException();
    }
}
