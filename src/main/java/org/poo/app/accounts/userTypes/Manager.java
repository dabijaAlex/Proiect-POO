package org.poo.app.accounts.userTypes;

import org.poo.app.Card;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.ArrayList;

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
    public void makePayment(Account account, double amount, double commision, int timestamp, Commerciant commerciant) {
        spent2.add(new Amounts(amount, timestamp));
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
        ArrayList<CommerciantForBusiness> commerciantsForBusinesses = account.getCommerciantsForBusiness();
        for(CommerciantForBusiness c : commerciantsForBusinesses) {
            if(commerciant.getCommerciant().equals(c.getName())) {
                c.addManager(this.username);
                c.addAmount(amount);
                return;
            }
        }
        commerciantsForBusinesses.add(new CommerciantForBusiness(commerciant.getCommerciant()));
        commerciantsForBusinesses.getLast().addManager(this.username);
        commerciantsForBusinesses.getLast().addAmount(amount);
    }
}
