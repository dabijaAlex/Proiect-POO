package org.poo.app.accounts.userTypes;

import org.poo.app.Card;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import org.poo.fileio.UserInput;
import org.poo.transactions.PayOnlineTransaction;

import java.util.ArrayList;

public class Employee extends BAccUser {

    public Employee(String email, String username) {
        super(email, username);
    }



    public void changeSpendingLimit(Account account, double limit) {
        throw new ChangeSpendingLimitException();
    }

    public void changeDepositLimit(Account account, double limit) {
        throw new ChangeDepositLimitException();
    }

    public void deleteCard(String cardNumber, Account account) {
        Card card = account.getCard(cardNumber);
        if(!card.getEmail().equals(email))
            throw new NotAuthorizedException();
        account.getCards().remove(card);
    }

    public void setMinBalance(Account account, double minBalance) {
        throw new NotAuthorizedException();
    }


    public void makePayment(Account account, double amount, double commision, int timestamp, Commerciant commerciant) {
        if(amount >= account.getSpendingLimit())

            throw new NotAuthorizedException();
        spent2.add(new Amounts(amount, timestamp));

        account.setBalance(account.getBalance() - amount - commision);

        ArrayList<CommerciantForBusiness> commerciantsForBusinesses = account.getCommerciantsForBusiness();
        for(CommerciantForBusiness c : commerciantsForBusinesses) {
            if(commerciant.getCommerciant().equals(c.getName())) {
                c.addEmployee(this.username);
                c.addAmount(amount);
                return;
            }
        }
        commerciantsForBusinesses.add(new CommerciantForBusiness(commerciant.getCommerciant()));
        commerciantsForBusinesses.getLast().addEmployee(this.username);
        commerciantsForBusinesses.getLast().addAmount(amount);


    }
    public void addFunds(Account account, double funds, int timestamp) {
        if (account.getDepositLimit() < funds && account.getDepositLimit() != -1)
            throw new NotAuthorizedException();
        account.setBalance(account.getBalance() + funds);
        deposited2.add(new Amounts(funds, timestamp));

    }



    public void setAlias(Account account, String alias) {
        throw new NotAuthorizedException();
    }
}
