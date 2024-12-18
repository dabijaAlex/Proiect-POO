package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SplitPaymentErrorTransaction extends SplitPaymentTransaction {
    private String error;
    public SplitPaymentErrorTransaction(int timestamp, String description, double amount, List<String> involvedAccounts, String currency, String error) {
        super(timestamp, description, amount, involvedAccounts, currency);
        this.error = "Account " + error + " has insufficient funds for a split payment.";
    }
}
