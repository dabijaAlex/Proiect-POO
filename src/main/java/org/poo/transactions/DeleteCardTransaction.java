package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteCardTransaction extends Transaction {
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    public DeleteCardTransaction(int timestamp, String description, String card, String cardHolder, String account) {
        this.timestamp = timestamp;
        this.description = description;
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }
}
