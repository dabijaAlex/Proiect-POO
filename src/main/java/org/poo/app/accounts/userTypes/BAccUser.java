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


    public BAccUser(final String email, final String username) {
        this.email = email;
        this.username = username;
    }

    public BAccUser(final BAccUser other) {
        username = other.username;
        email = other.email;
        spent = other.spent;
        deposited = other.deposited;
        spent2 = other.spent2;
        deposited2 = other.deposited2;
    }

    public void changeSpendingLimit(final Account account,
                                    final double newLimit) throws ChangeSpendingLimitException {
    }
    public void changeDepositLimit(final Account account,
                                   final double newLimit) throws ChangeDepositLimitException {
    }
    public void deleteCard(final String card,
                           final Account account) throws NotAuthorizedException {
        account.getCards().remove(account.getCard(card));
    }
    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
    }
    public void makePayment(final Account account, final double amount,
                            final double commision, final  int timestamp,
                            final  Commerciant commerciant) throws NotAuthorizedException {
    }
    public void addFunds(final Account account, final double funds, final int timestamp) {
        deposited2.add(new Amounts(funds, timestamp));
        account.setBalance(account.getBalance() + funds);

    }
    public void setAlias(final Account account, final String alias) {
    }

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
}
