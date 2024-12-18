package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SendMoneyTransaction extends Transaction {
    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private String transferType;
    private int timestamp;
    public SendMoneyTransaction(String senderIBAN, String amount, String receiverIBAN, String description, String transferType, int timestamp) {
        this.senderIBAN = senderIBAN;
        this.amount = amount;
        this.receiverIBAN = receiverIBAN;
        this.description = description;
        this.transferType = transferType;
        this.timestamp = timestamp;
    }
}
