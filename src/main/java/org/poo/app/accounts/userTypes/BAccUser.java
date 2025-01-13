package org.poo.app.accounts.userTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Card;
import org.poo.app.ExchangeRateGraph;
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


    public BAccUser(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public BAccUser(BAccUser other) {
        username = other.username;
        email = other.email;
        spent = other.spent;
        deposited = other.deposited;
        spent2 = other.spent2;
        deposited2 = other.deposited2;
    }

    public void changeSpendingLimit(Account account, double newLimit) {
        account.setSpendingLimit(newLimit);
    }
    public void changeDepositLimit(Account account, double newLimit) {
        account.setDepositLimit(newLimit);
    }
    public void deleteCard(String card, Account account) {
        account.getCards().remove(account.getCard(card));
    }
    public void setMinBalance(Account account, double minBalance) {
        account.setMinBalance(minBalance);
    }
    public void makePayment(Account account, double amount, double commision, int timestamp, Commerciant commerciant) {
        spent2.add(new Amounts(amount, timestamp));
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
    }
    public void addFunds(Account account, double funds, int timestamp) {
        deposited2.add(new Amounts(funds, timestamp));
        account.setBalance(account.getBalance() + funds);

    }
    public void setAlias(Account account, String alias) {
        account.setAlias(alias);
    }

    public double getSpentInTimestamps(int start, int end) {
        double total = 0;
        for(Amounts amount : spent2) {
            if(amount.timestamp >= start && amount.timestamp <= end) {
                total = total + amount.val;
            }
        }
        spent = total;
        return total;

    }

    public double getDepositedInTimestamps(int start, int end) {
        double total = 0;
        for(Amounts amount : deposited2) {
            if(amount.timestamp >= start && amount.timestamp <= end) {
                total = total + amount.val;
            }
        }
        deposited = total;
        return total;
    }
}
