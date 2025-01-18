package org.poo.app.accounts.userTypes;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.ArrayList;

public final class Manager extends BAccUser {
    public Manager(final String email, final String username) {
        super(email, username);
    }

    public void deleteCard(final String card, final Account account) {
        account.getCards().remove(account.getCard(card));
    }

    public void changeSpendingLimit(final Account account,
                                    final double limit) throws ChangeSpendingLimitException {
        throw new ChangeSpendingLimitException();
    }

    public void changeDepositLimit(final Account account,
                                   final double limit) throws ChangeDepositLimitException {
        throw new ChangeDepositLimitException();
    }

    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    public void setAlias(final Account account,
                         final String alias) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }
    public void makePayment(final Account account, final double amount,
                            final double commision, final int timestamp,
                            final Commerciant commerciant) {
        spent2.add(new Amounts(amount, timestamp));
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
        ArrayList<CommerciantForBusiness> commerciantsForBusinesses =
                account.getCommerciantsForBusiness();
        for (CommerciantForBusiness c : commerciantsForBusinesses) {
            if (commerciant.getCommerciant().equals(c.getName())) {
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
