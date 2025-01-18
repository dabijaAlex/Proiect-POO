package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.plans.*;
import org.poo.app.splitPayment.SingleSplitPayment;
import org.poo.commands.TransactionCommands.SplitPayment;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Getter @Setter
public class User {
    @JsonIgnore
    private static int counter = 0;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    @JsonIgnore
    private int index = 0;
    @JsonIgnore
    private String birthDate;
    @JsonIgnore
    private String occupation;
    @JsonIgnore
    private ArrayList<SingleSplitPayment> splitPayments = new ArrayList<>();
    @JsonIgnore
    protected ServicePlan servicePlan;



    public void addSplitPayment(SingleSplitPayment splitPayment) {
        splitPayments.add(splitPayment);
    }

    /**
     * constructor
     * @param user
     */
    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        this.birthDate = user.getBirthDate();
        this.occupation = user.getOccupation();
        counter++;
        index = counter;
        if(user.getOccupation().equals("student")) {
            servicePlan = new Student();
        } else {
            servicePlan = new Standard();
        }
    }

    public Account getFirstClassicAccount(String currency) {
        for(Account account : accounts) {
            if(account.getClassicAccount(currency) != null) {
                return account;
            }
        }
        return null;
    }

    /**
     * copy the user into another reference for arrayOutput
     * @param other
     */
    public User(final User other) {
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.accounts = new ArrayList<Account>();
        for (Account account : other.accounts) {
            this.accounts.add(new Account(account));
        }
    }


    /**
     * Add account
     * @param account
     */
    public void addAccount(final Account account) {
        this.accounts.add(account);
    }

    /**
     * Del acc
     * @param account
     */
    public void deleteAccount(final Account account) {
        this.accounts.remove(account);
    }


    /**
     * get account based on Iban cardNumber or alias
     * prerequisite for the getReference methods defined in other parts of the program
     * @param ibanOrCardOrAlias
     * @return
     */
    public Account getAccount(final String ibanOrCardOrAlias) {
        for (Account account : this.accounts) {
            if (account.getIban().equals(ibanOrCardOrAlias)) {
                return account;
            }
        }
        for (Account account : this.accounts) {
            if (account.getCard(ibanOrCardOrAlias) != null) {
                return account;
            }
        }
        for (Account account : this.accounts) {
            if (account.getAlias().equals(ibanOrCardOrAlias)) {
                return account;
            }
        }
        return null;
    }

    public void upgradeServicePlanToGold(Account cont) throws InsufficientFundsException, AlreadyHasPlanException,
            CannotDowngradePlanException {
        cont.getServicePlan().upgradeToGold(cont);
        for (Account account : this.accounts) {
            account.setServicePlan(new Gold());
        }
    }

    public void upgradeServicePlanToSilver(Account cont) throws InsufficientFundsException, AlreadyHasPlanException,
            CannotDowngradePlanException {
        cont.getServicePlan().upgradeToSilver(cont);
        for (Account account : this.accounts) {
            account.setServicePlan(new Silver());
        }
    }
}
