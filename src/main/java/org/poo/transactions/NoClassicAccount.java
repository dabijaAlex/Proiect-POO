package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;

@Getter @Setter
public class NoClassicAccount extends Transaction {
    private String description;
    public NoClassicAccount(final int timestamp) {
        this.timestamp = timestamp;
        this.description = "You do not have a classic account.";
    }
}
