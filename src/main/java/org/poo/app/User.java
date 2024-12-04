package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter @Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;

    public User(UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void deleteAccount(Account account) {
        this.accounts.remove(account);
    }

    public Account getAccount(String IBAN) {
        for (Account account : this.accounts) {
            if(account.getIBAN().equals(IBAN)) {
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
