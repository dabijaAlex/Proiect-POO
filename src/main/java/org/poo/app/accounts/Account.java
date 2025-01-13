package org.poo.app.accounts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Card;
import org.poo.app.InsufficientFundsException;
import org.poo.app.NotASavingsAccount;
import org.poo.app.User;
import org.poo.app.accounts.userTypes.BAccUser;
import org.poo.app.accounts.userTypes.CommerciantForBusiness;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.plans.AlreadyHasPlanException;
import org.poo.app.plans.CannotDowngradePlanException;
import org.poo.app.plans.ServicePlan;
import org.poo.commands.TransactionCommands.SplitPayment;
import org.poo.commands.otherCommands.AddInterest;
import org.poo.commands.otherCommands.ChangeInterestRate;
import org.poo.transactions.CreateCardTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

@Getter @Setter
public class Account {
    @JsonProperty("IBAN")
    protected String IBAN;
    protected double balance;
    protected String currency;
    protected String type;
    protected ArrayList<Card> cards;

    @JsonIgnore
    protected String email;

    @JsonIgnore
    protected double minBalance = 0;
    @JsonIgnore
    protected String alias = "a";
    @JsonIgnore
    protected double interestRate;
    @JsonIgnore
    protected ArrayList<Transaction> transactions;
    @JsonIgnore
    protected ServicePlan servicePlan;
    @JsonIgnore
    protected User userRef;
    @JsonIgnore
    protected double spentAtCommerciant = 0;
    @JsonIgnore
    protected int FoodDiscounts = 0;
    @JsonIgnore
    protected int ClothesDiscounts = 0;
    @JsonIgnore
    protected int TechDiscounts = 0;


    public void addFoodDiscount() {
        if(FoodDiscounts == 0)
            FoodDiscounts++;
    }
    public void addClothesDiscount() {
        if(ClothesDiscounts == 0)
            ClothesDiscounts++;
    }
    public void addTechDiscount() {
        if(TechDiscounts == 0)
            TechDiscounts++;
    }

    /**
     * Constructor
     * @param IBAN
     * @param balance
     * @param currency
     * @param type
     */
    public Account(final String IBAN, final double balance, final String currency,
                   final String type, ServicePlan servicePlan, final double interestRate, final User user, final String email) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.servicePlan = servicePlan;
        this.interestRate = interestRate;
        this.userRef = user;
        this.email = email;

        this.transactions = new ArrayList<>();
    }

    /**
     * Add card to account
     * @param card
     */
    public void addCard(final Card card) {
        this.cards.add(card);
    }

    public String createCard(String emailCreator) {
        String card = Utils.generateCardNumber();
        this.addCard(new Card(card, "active", emailCreator));
        return card;
    }


    public void makePayment(final double amount, final double commission, String email,
                            final int timestamp, Commerciant commerciant) {
        balance = balance - (commission + amount);
    }

    public void addFunds(final double amount, final String email, final int timestamp) {
        balance = balance + amount;
    }

    /**
     * delete card from account
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber, final String email) {
        Card card = getCard(cardNumber);
        if(card.getStatus().equals("active")) {
            return;
        }
        this.cards.remove(getCard(cardNumber));
    }

    public void deleteCardOneTime(final String cardNumber, final String email) {
        Card card = getCard(cardNumber);
//        if(!card.isOneTime() && card.getStatus().equals("active")) {
//            return;
//        }
        this.cards.remove(getCard(cardNumber));
    }
    public void setAliasCommand(final String alias, String email) {
        this.alias = alias;
    }

    public void setMinBalanceCommand(double minBalance, String email) {
        this.minBalance = minBalance;
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



    public Account getClassicAccount(String currency) {
        if(this.currency.equals(currency)) {
            return this;
        }
        return null;
    }


    public void makeWithdrawal(Account targetAccount, double amount) throws NotASavingsAccount {
        throw new NotASavingsAccount();
    }




    /**
     * Add transaction to account
     * @param transaction
     */
    public void addTransaction(final Transaction transaction) {
        this.transactions.add(transaction);
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

    public void setSpendingLimit(final double spendingLimit) {}
    public void setDepositLimit(final double depositLimit) {}
    @JsonIgnore
    public double getSpendingLimit() {return 0;}
    @JsonIgnore
    public double getDepositLimit() {return 0;}
    public void addBusinessAssociate(String role, String email, String username){}

    public void changeSpendingLimit(double amount, String email){
        throw new NotABusinessAccountException();
    }
    public void changeDepositLimit(double amount, String email){
        throw new NotABusinessAccountException();
    }
    public ArrayList<BAccUser> abc() {return null;}

    @JsonIgnore
    public ArrayList<CommerciantForBusiness> getCommerciantsForBusiness() {
        return null;
    }

    @JsonIgnore
    public BAccUser getCurrentAssociate(String email) {
        return new BAccUser("", "");
//        return null;
    }




}
