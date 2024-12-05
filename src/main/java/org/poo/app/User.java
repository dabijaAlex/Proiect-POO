package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class User {
    @JsonIgnore
    static int counter = 0;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    @JsonIgnore
    private ArrayList<Command> transactions;
    @JsonIgnore
    private int index = 0;

    public User(UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        counter++;
        index = counter;
    }
    public void addTransaction(Command transaction) {
        transactions.add(transaction);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void deleteAccount(Account account) {
        this.accounts.remove(account);
    }

    public Account getAccount(String IBANorCardOrAlias) {
        for (Account account : this.accounts) {
            if(account.getIBAN().equals(IBANorCardOrAlias)) {
                return account;
            }
        }
        for (Account account : this.accounts) {
            if(account.getCard(IBANorCardOrAlias) != null) {
                return account;
            }
        }
        for (Account account : this.accounts) {
            if(account.getAlias().equals(IBANorCardOrAlias)) {
                return account;
            }
        }
        return null;
    }



    public User(User other) {
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.accounts = new ArrayList<Account>();
        for(Account account : other.accounts) {
            this.accounts.add(new Account(account));
        }
    }
}
