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
    @JsonIgnore double interestRate;
    @JsonIgnore
    protected ArrayList<Transaction> transactions;

    public Account(String IBAN, double balance, String currency, String type) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();

        this.transactions = new ArrayList<>();
    }
    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Account(Account other) {
        this.IBAN = other.IBAN;
        this.balance = other.balance;
        this.currency = other.currency;
        this.type = other.type;
        this.cards = new ArrayList<>();
        for (Card card : other.getCards()) {
            this.cards.add(new Card(card));
        }
    }


    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void deleteCard(String cardNumber) {
        this.cards.remove(getCard(cardNumber));
//            throw new NotFoundException();
    }

    public Card getCard(String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public void useCard(String cardNumber) {
        Card card = getCard(cardNumber);
        if(card != null) {
            useCard(cardNumber);
        }
    }

    public void addInterest(ArrayNode output, AddInterest command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCmdName());


        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", command.getTimestamp());
        outputNode.put("description", "This is not a savings account");

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", command.getTimestamp());

        output.add(objectNode);
    }

    public void setInterestRate(double interestRate, ArrayNode output, User user, ChangeInterestRate command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", command.getCmdName());


        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", command.getTimestamp());
        outputNode.put("description", "This is not a savings account");

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", command.getTimestamp());

        output.add(objectNode);
    }


}
