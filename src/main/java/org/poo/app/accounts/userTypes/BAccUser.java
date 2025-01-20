package org.poo.app.accounts.userTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import java.util.ArrayList;

@Getter @Setter
public class BAccUser {
    @JsonIgnore
    protected String email;
    @JsonIgnore
    protected ArrayList<Amounts> spent2 = new ArrayList<Amounts>();
    @JsonIgnore
    protected ArrayList<Amounts> deposited2 = new ArrayList<Amounts>();
    protected String username;
    protected double spent;
    protected double deposited;

    /**
     * constructor
     * @param email
     * @param username
     */
    public BAccUser(final String email, final String username) {
        this.email = email;
        this.username = username;
    }

    /**
     * constructor that returns a deep copy of given BAcc User
     * @param other
     */
    public BAccUser(final BAccUser other) {
        username = other.username;
        email = other.email;
        spent = other.spent;
        deposited = other.deposited;
        spent2 = other.spent2;
        deposited2 = other.deposited2;
    }

    /**
     * needs to be overwritten by each type of user because of their permissions
     * @param account
     * @param newLimit
     * @throws ChangeSpendingLimitException
     */
    public void changeSpendingLimit(final Account account,
                                    final double newLimit) throws ChangeSpendingLimitException {
    }

    /**
     * needs to be overwritten by each type of user because of their permissions
     * @param account
     * @param newLimit
     * @throws ChangeDepositLimitException
     */
    public void changeDepositLimit(final Account account,
                                   final double newLimit) throws ChangeDepositLimitException {
    }

    /**
     * remove card from account
     * overwritten by employee as he can delete cards only created byy him
     * @param card
     * @param account
     * @throws NotAuthorizedException
     */
    public void deleteCard(final String card,
                           final Account account) throws NotAuthorizedException {
        account.getCards().remove(account.getCard(card));
    }

    /**
     * needs to be overwritten by each type of user because of their permissions
     * @param account
     * @param minBalance
     * @throws NotAuthorizedException
     */
    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
    }

    /**
     * needs to be overwritten by each type of user because of their permissions
     * @param account
     * @param amount
     * @param commision
     * @param timestamp
     * @param commerciant
     * @throws NotAuthorizedException
     */
    public void makePayment(final Account account, final double amount,
                            final double commision, final  int timestamp,
                            final  Commerciant commerciant) throws NotAuthorizedException {
    }

    /**
     * add funds to account
     * overwritten methid by employee as he has a limit on how much he can deposit at once
     * @param account
     * @param funds
     * @param timestamp
     */
    public void addFunds(final Account account, final double funds, final int timestamp) {
        deposited2.add(new Amounts(funds, timestamp));
        account.setBalance(account.getBalance() + funds);

    }

    /**
     * needs to be overwritten by each type of user because of their permissions
     * @param account
     * @param alias
     */
    public void setAlias(final Account account, final String alias) {
    }

    /**
     * get how much a user has spent in a specific interval
     * @param start
     * @param end
     * @return
     */
    public double getSpentInTimestamps(final int start, final int end) {
        double total = 0;
        for (Amounts amount : spent2) {
            if (amount.timestamp >= start && amount.timestamp <= end) {
                total = total + amount.val;
            }
        }
        spent = total;
        return total;

    }

    /**
     * get how much a user has deposited in a given interval
     * @param start
     * @param end
     * @return
     */
    public double getDepositedInTimestamps(final int start, final int end) {
        double total = 0;
        for (Amounts amount : deposited2) {
            if (amount.timestamp >= start && amount.timestamp <= end) {
                total = total + amount.val;
            }
        }
        deposited = total;
        return total;
    }

    /**
     * used to add managers to a list. Overwritten in manager class
     * @param list
     */
    public void addManagerToList(final ArrayList<BAccUser> list) {
    }

    /**
     * used to add employees to a list. Overwritten in employee class
     * @param list
     */
    public void addEmployeeToList(final ArrayList<BAccUser> list) {
    }
}
