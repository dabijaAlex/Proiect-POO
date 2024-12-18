package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class CreateCardTransaction extends Transaction {
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param card
     * @param cardHolder
     * @param account
     */
    public CreateCardTransaction(final int timestamp, final String description,
                                 final String card, final String cardHolder, final String account) {
        this.timestamp = timestamp;
        this.description = description;
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }
}
