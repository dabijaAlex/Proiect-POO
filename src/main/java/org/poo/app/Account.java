package org.poo.app;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.AddInterest;
import org.poo.commands.ChangeInterestRate;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Getter @Setter
public class Account {
    @JsonProperty("IBAN")
    protected String IBAN;
    protected double balance;
    protected String currency;
    protected String type;
    protected ArrayList<Card> cards;

    @JsonIgnore
    protected double minBalance = 0;
    @JsonIgnore
    protected String alias = "a";
    @JsonIgnore
    protected double interestRate;
    @JsonIgnore
    protected ArrayList<Transaction> transactions;


    /**
     * Constructor
     * @param IBAN
     * @param balance
     * @param currency
     * @param type
     */
    public Account(final String IBAN, final double balance, final String currency,
                   final String type) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();

        this.transactions = new ArrayList<>();
    }

    /**
     * Add card to account
     * @param card
     */
    public void addCard(final Card card) {
        this.cards.add(card);
    }

    /**
     * copy account so the reference from output is different
     * @param other
     */
    public Account(final Account other) {
        this.IBAN = other.IBAN;
        this.balance = other.balance;
        this.currency = other.currency;
        this.type = other.type;
        this.cards = new ArrayList<>();
        for (Card card : other.getCards()) {
            this.cards.add(new Card(card));
        }
    }


    /**
     * Add transaction to account
     * @param transaction
     */
    public void addTransaction(final Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * delete card from account
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber) {
        this.cards.remove(getCard(cardNumber));
    }

    /**
     * get card by card number
     * @param cardNumber
     * @return
     */
    public Card getCard(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }


    /**
     * this is a normal account so add interest will add error to output
     * @param output
     * @param command
     */
    public void addInterest(final ArrayNode output, final AddInterest command) {
        this.interestError("This is not a savings account",
                output, command.getCmdName(), command.getTimestamp());

    }


    /**
     * this is a normal account so add interest will add error to output
     * @param interestRate
     * @param output
     * @param command
     */
    public void setInterestRate(final double interestRate, final ArrayNode output,
                                final ChangeInterestRate command) {
        this.interestError("This is not a savings account",
                output, command.getCmdName(), command.getTimestamp());
    }

    /**
     * this method is called by both earlier methods
     * @param description
     * @param output
     * @param cmdName
     * @param timestamp
     */
    private void interestError(final String description, final ArrayNode output,
                               final String cmdName, final int timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);


        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", description);

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);
    }
}
