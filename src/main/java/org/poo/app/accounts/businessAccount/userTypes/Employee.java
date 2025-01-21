package org.poo.app.accounts.businessAccount.userTypes;

import org.poo.app.Card;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.commerciatnTypes.Commerciant;
import java.util.ArrayList;

public final class Employee extends BAccUser {

    /**
     * constructor that calls superclass
     * @param email
     * @param username
     */
    public Employee(final String email, final String username) {
        super(email, username);
    }


    /**
     * action not allowed so it throws
     * @param account
     * @param limit
     * @throws ChangeSpendingLimitException
     */
    public void changeSpendingLimit(final Account account,
                                    final double limit) throws ChangeSpendingLimitException {
        throw new ChangeSpendingLimitException();
    }

    /**
     * action not allowed so this throws
     * @param account
     * @param limit
     * @throws ChangeDepositLimitException
     */
    public void changeDepositLimit(final Account account,
                                   final double limit) throws ChangeDepositLimitException {
        throw new ChangeDepositLimitException();
    }

    /**
     * check if card was made by this employee.
     *      if true delete else throw not authorised exception
     * @param cardNumber
     * @param account
     * @throws NotAuthorizedException
     */
    public void deleteCard(final String cardNumber,
                           final Account account) throws NotAuthorizedException {
        Card card = account.getCard(cardNumber);
        if (!card.getEmail().equals(email)) {
            throw new NotAuthorizedException();
        }
        account.getCards().remove(card);
    }

    /**
     * action not allowed so this throws
     * @param account
     * @param minBalance
     * @throws NotAuthorizedException
     */
    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }


    /**
     * take amount from account
     * add this payment to the list of payments made by this user
     * add this payment to the commerciant where it took place
     * @param account
     * @param amount
     * @param commision
     * @param timestamp
     * @param commerciant
     */
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

    /**
     * check if bellow deposit limit
     *      if true add funds and add to this account the amount he deposited
     *          else throw not authorised exception
     * @param account
     * @param funds
     * @param timestamp
     * @throws NotAuthorizedException
     */
    public void addFunds(final Account account, final double funds,
                         final int timestamp) throws NotAuthorizedException {
        if (account.getDepositLimit() < funds && account.getDepositLimit() != -1) {
            throw new NotAuthorizedException();
        }
        account.setBalance(account.getBalance() + funds);
        deposited2.add(new Amounts(funds, timestamp));

    }


    /**
     * action not allowed so this throws
     * @param account
     * @param alias
     * @throws NotAuthorizedException
     */
    public void setAlias(final Account account, final String alias) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    /**
     * add employee to given list
     * @param list
     */
    public void addEmployeeToList(ArrayList<BAccUser> list) {
        list.add(new BAccUser(this));
    }
}
