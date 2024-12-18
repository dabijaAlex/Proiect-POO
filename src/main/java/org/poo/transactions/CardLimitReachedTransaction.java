package org.poo.transactions;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class CardLimitReachedTransaction extends Transaction {
    private String description;

    /**
     * Constructor
     * @param timestamp
     */
    public CardLimitReachedTransaction(final int timestamp) {
        this.timestamp = timestamp;
        description = "You have reached the minimum amount of funds, the card will be frozen";
    }
}
