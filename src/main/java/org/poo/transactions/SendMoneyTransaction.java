package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public final class SendMoneyTransaction extends Transaction {
    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private String transferType;

    /**
     * Constructor
     * @param senderIBAN
     * @param amount
     * @param receiverIBAN
     * @param description
     * @param transferType
     * @param timestamp
     */
    public SendMoneyTransaction(final String senderIBAN, final String amount,
                                final String receiverIBAN, final String description,
                                final String transferType, final int timestamp) {
        this.senderIBAN = senderIBAN;
        this.amount = amount;
        this.receiverIBAN = receiverIBAN;
        this.description = description;
        this.transferType = transferType;
        this.timestamp = timestamp;
    }
}
