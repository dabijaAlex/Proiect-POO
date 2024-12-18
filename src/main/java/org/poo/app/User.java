package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter @Setter
public final class User {
    @JsonIgnore
    private static int counter = 0;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    @JsonIgnore
    private int index = 0;

    /**
     * constructor
     * @param user
     */
    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        counter++;
        index = counter;
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
            if (account.getIBAN().equals(ibanOrCardOrAlias)) {
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
}
