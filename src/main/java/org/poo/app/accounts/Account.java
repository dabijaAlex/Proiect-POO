package org.poo.app.accounts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Card;
import org.poo.app.NotASavingsAccount;
import org.poo.app.User;
import org.poo.app.accounts.userTypes.BAccUser;
import org.poo.app.accounts.userTypes.CommerciantForBusiness;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.plans.ServicePlan;
import org.poo.commands.otherCommands.AddInterest;
import org.poo.commands.otherCommands.ChangeInterestRate;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;
import java.util.ArrayList;

@Getter @Setter
public class Account {
    @JsonProperty("IBAN")
    protected String iban;
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
    protected User userRef;
    @JsonIgnore
    protected double spentAtCommerciant = 0;
    @JsonIgnore
    protected int foodDiscounts = 0;
    @JsonIgnore
    protected int clothesDiscounts = 0;
    @JsonIgnore
    protected int techDiscounts = 0;


    public void addFoodDiscount() {
        if (foodDiscounts == 0) {
            foodDiscounts++;
        }
    }
    public void addClothesDiscount() {
        if (clothesDiscounts == 0) {
            clothesDiscounts++;
        }
    }
    public void addTechDiscount() {
        if (techDiscounts == 0) {
            techDiscounts++;
        }
    }

    /**
     * Constructor
     * @param iban
     * @param balance
     * @param currency
     * @param type
     */
    public Account(final String iban, final double balance, final String currency,
                   final String type, final double interestRate,
                   final User user, final String email) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
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

    public String createCard(final String emailCreator) {
        String card = Utils.generateCardNumber();
        this.addCard(new Card(card, "active", emailCreator));
        return card;
    }


    public void makePayment(final double amount, final double commission,
                            final String queryingPersonEmail, final int timestamp,
                            final Commerciant commerciant) {
        balance = balance - (commission + amount);
    }

    public void addFunds(final double amount, final String queryingPersonEmail,
                         final int timestamp) {
        balance = balance + amount;
    }

    /**
     * delete card from account
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber, final String queryingPersonEmail) {
        Card card = getCard(cardNumber);
        if (card.getStatus().equals("active")) {
            return;
        }
        this.cards.remove(getCard(cardNumber));
    }

    public void deleteCardOneTime(final String cardNumber, final String queryingPersonEmail) {
        this.cards.remove(getCard(cardNumber));
    }
    public void setAliasCommand(final String newAlias, final String queryingPersonEmail) {
        this.alias = newAlias;
    }

    public void setMinBalanceCommand(final double newMinBalance, final String queryingPersonEmail) {
        this.minBalance = newMinBalance;
    }
    /**
     * copy account so the reference from output is different
     * @param other
     */
    public Account(final Account other) {
        this.iban = other.iban;
        this.balance = other.balance;
        this.currency = other.currency;
        this.type = other.type;
        this.cards = new ArrayList<>();
        for (Card card : other.getCards()) {
            this.cards.add(new Card(card));
        }
    }



    public Account getClassicAccount(final String givenCurrency) {
        if (this.currency.equals(givenCurrency)) {
            return this;
        }
        return null;
    }


    public void makeWithdrawal(final Account targetAccount,
                               final double amount) throws NotASavingsAccount {
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
     * @param newInterestRate
     * @param output
     * @param command
     */
    public void setInterestRate(final double newInterestRate, final ArrayNode output,
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

    public void setSpendingLimit(final double spendingLimit) {

    }

    public void setDepositLimit(final double depositLimit) {

    }

    @JsonIgnore
    public double getSpendingLimit() {
        return 0;
    }

    @JsonIgnore
    public double getDepositLimit() {
        return 0;
    }

    public void addBusinessAssociate(final String role, final String queryingPersonEmail,
                                     final String username) {

    }

    public void changeSpendingLimit(final double amount,
                                    final String queryingPersonEmail)
                                    throws NotABusinessAccountException {
        throw new NotABusinessAccountException();
    }

    public void changeDepositLimit(final double amount,
                                   final String queryingPersonEmail)
                                    throws NotABusinessAccountException {
        throw new NotABusinessAccountException();
    }

    public ArrayList<BAccUser> abc() {
        return null;
    }

    @JsonIgnore
    public ArrayList<CommerciantForBusiness> getCommerciantsForBusiness() {
        return null;
    }

    @JsonIgnore
    public BAccUser getCurrentAssociate(final String queryingPersonEmail) {
        return new BAccUser("", "");
    }




}
