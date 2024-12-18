package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Transaction {
    protected int timestamp;


    public void addSpendingTransactionToList(List<Transaction> transactions) {
    }

    @JsonIgnore
    public double getAmountDouble() {
        return 0;
    }

    @JsonIgnore
    public String getCommerciant2() {
        return null;
    }

}


