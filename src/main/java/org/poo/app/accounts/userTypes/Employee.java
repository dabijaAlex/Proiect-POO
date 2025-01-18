package org.poo.app.accounts.userTypes;

import org.poo.app.Card;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import java.util.ArrayList;

public final class Employee extends BAccUser {

    public Employee(final String email, final String username) {
        super(email, username);
    }



    public void changeSpendingLimit(final Account account,
                                    final double limit) throws ChangeSpendingLimitException {
        throw new ChangeSpendingLimitException();
    }

    public void changeDepositLimit(final Account account,
                                   final double limit) throws ChangeDepositLimitException {
        throw new ChangeDepositLimitException();
    }

    public void deleteCard(final String cardNumber,
                           final Account account) throws NotAuthorizedException {
        Card card = account.getCard(cardNumber);
        if (!card.getEmail().equals(email)) {
            throw new NotAuthorizedException();
        }
        account.getCards().remove(card);
    }

    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }


    public void makePayment(final Account account, final double amount, final double commision,
                            final int timestamp, final Commerciant commerciant)
                            throws NotAuthorizedException {
        if (amount >= account.getSpendingLimit()) {
            throw new NotAuthorizedException();
        }
        spent2.add(new Amounts(amount, timestamp));

        account.setBalance(account.getBalance() - amount - commision);

        ArrayList<CommerciantForBusiness> commerciantsForBusinesses =
                account.getCommerciantsForBusiness();
        for (CommerciantForBusiness c : commerciantsForBusinesses) {
            if (commerciant.getCommerciant().equals(c.getName())) {
                c.addEmployee(this.username);
                c.addAmount(amount);
                return;
            }
        }
        commerciantsForBusinesses.add(new CommerciantForBusiness(commerciant.getCommerciant()));
        commerciantsForBusinesses.getLast().addEmployee(this.username);
        commerciantsForBusinesses.getLast().addAmount(amount);


    }
    public void addFunds(final Account account, final double funds,
                         final int timestamp) throws NotAuthorizedException {
        if (account.getDepositLimit() < funds && account.getDepositLimit() != -1) {
            throw new NotAuthorizedException();
        }
        account.setBalance(account.getBalance() + funds);
        deposited2.add(new Amounts(funds, timestamp));

    }



    public void setAlias(final Account account, final String alias) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }
}
