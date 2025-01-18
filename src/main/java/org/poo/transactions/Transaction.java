package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Transaction {
    protected int timestamp;
    @JsonIgnore
    private boolean isSplitPayment = false;

    public Transaction() {

    }
    public Transaction(final boolean isSplitPayment) {
        this.isSplitPayment = isSplitPayment;
    }

    /**
     * this method is for transactions that characterize as spending transactions
     * @param transactions
     */
    public void addSpendingTransactionToList(final List<Transaction> transactions) {
    }

    /**
     * need this method to get the amount when i have an array of Transactions
     * @return
     */
    @JsonIgnore
    public double getAmountDouble() {
        return 0;
    }

    /**
     * need this method to get the commerciant when i have an array of Transactions
     * @return
     */
    @JsonIgnore
    public String getCommerciant2() {
        return null;
    }

}


