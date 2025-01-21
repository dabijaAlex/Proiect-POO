package org.poo.app.accounts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Card;
import org.poo.app.notFoundExceptions.NotASavingsAccountException;
import org.poo.app.User;
import org.poo.app.accounts.businessAccount.userTypes.BAccUser;
import org.poo.app.accounts.businessAccount.userTypes.CommerciantForBusiness;
import org.poo.app.commerciants.commerciatnTypes.Commerciant;
import org.poo.commands.accountAdministrationCommands.AddInterest;
import org.poo.commands.accountAdministrationCommands.ChangeInterestRate;
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


    /**
     * you can only get one discount per account
     */
    public void addFoodDiscount() {
        if (foodDiscounts == 0) {
            foodDiscounts++;
        }
    }

    /**
     * you can only get one discount per account
     */
    public void addClothesDiscount() {
        if (clothesDiscounts == 0) {
            clothesDiscounts++;
        }
    }

    /**
     * you can only get one discount per account
     */
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

    /**
     * create card and add it to card list
     * @param emailCreator
     * @return
     */
    public String createCard(final String emailCreator) {
        String card = Utils.generateCardNumber();
        this.addCard(new Card(card, "active", emailCreator));
        return card;
    }


    /**
     * make a payment
     * this method is usefull for other types of accounts that opperate differently:
     *      - for business account we need to authorize the transaction
     *          based on role and spending limits
     * @param amount
     * @param commission
     * @param queryingPersonEmail
     * @param timestamp
     * @param commerciant
     */
    public void makePayment(final double amount, final double commission,
                            final String queryingPersonEmail, final int timestamp,
                            final Commerciant commerciant) {
        balance = balance - (commission + amount);
    }

    /**
     * add funds
     * this method is usefull for other types of accounts that operate differently:
     *      - for business account we need to authorize the
     *          deposit based on role and spending limits
     * @param amount
     * @param queryingPersonEmail
     * @param timestamp
     */
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

    /**
     * used after making a payment with a onetime card in order to
     *      delete this one and gen another card
     * @param cardNumber
     * @param queryingPersonEmail
     */
    public void deleteCardOneTime(final String cardNumber, final String queryingPersonEmail) {
        this.cards.remove(getCard(cardNumber));
    }

    /**
     * this method is usefull for other types of accounts that operate differently:
     *       - for business account we need to authorize the action based on roles
     * @param newAlias
     * @param queryingPersonEmail
     */
    public void setAliasCommand(final String newAlias, final String queryingPersonEmail) {
        this.alias = newAlias;
    }

    /**
     * this method is usefull for other types of accounts that operate differently:
     *     - for business account we need to authorize the action based on roles
     * @param newMinBalance
     * @param queryingPersonEmail
     */
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


    /**
     * in other types of accounts this method throws exception
     * @param givenCurrency
     * @return
     */
    public Account getClassicAccount(final String givenCurrency) {
        if (this.currency.equals(givenCurrency)) {
            return this;
        }
        return null;
    }


    /**
     * method valid only for savings account
     * @param targetAccount
     * @param amount
     * @throws NotASavingsAccountException
     */
    public void makeWithdrawal(final Account targetAccount,
                               final double amount) throws NotASavingsAccountException {
        throw new NotASavingsAccountException();
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
        throw new NotASavingsAccountException();
    }


    /**
     * this is a normal account so add interest will add error to output
     * @param newInterestRate
     * @param output
     * @param command
     */
    public void setInterestRate(final double newInterestRate, final ArrayNode output,
                                final ChangeInterestRate command) {
        throw new NotASavingsAccountException();
    }


    /**
     * method valid only for business
     * @param spendingLimit
     */
    public void setSpendingLimit(final double spendingLimit) {

    }

    /**
     * method valid only for business
     * @param depositLimit
     */
    public void setDepositLimit(final double depositLimit) {

    }

    /**
     * get the spending limit (valid for business)
     * @return
     */
    @JsonIgnore
    public double getSpendingLimit() {
        return 0;
    }

    /**
     * get the deposit limit (valid for business)
     * @return
     */
    @JsonIgnore
    public double getDepositLimit() {
        return 0;
    }

    /**
     * valid for business only
     * @param role
     * @param queryingPersonEmail
     * @param username
     */
    public void addBusinessAssociate(final String role, final String queryingPersonEmail,
                                     final String username) {

    }

    /**
     * valid for business
     * this is not business so it throws
     * @param amount
     * @param queryingPersonEmail
     * @throws NotABusinessAccountException
     */
    public void changeSpendingLimit(final double amount,
                                    final String queryingPersonEmail)
                                    throws NotABusinessAccountException {
        throw new NotABusinessAccountException();
    }

    /**
     * valid for business only
     * this is not business and it throws
     * @param amount
     * @param queryingPersonEmail
     * @throws NotABusinessAccountException
     */
    public void changeDepositLimit(final double amount,
                                   final String queryingPersonEmail)
                                    throws NotABusinessAccountException {
        throw new NotABusinessAccountException();
    }

    /**
     *  used to be overwritten in business account and return business associates list.
     *  idk why i can t name it any other way. this is the only name that java will recognize
     *      in both classes
     * @return
     */
    public ArrayList<BAccUser> abc() {
        return null;
    }

    /**
     * valid for business accounts
     * @return
     */
    @JsonIgnore
    public ArrayList<CommerciantForBusiness> getCommerciantsForBusiness() {
        return null;
    }

    /**
     * valid for business account
     * @param queryingPersonEmail
     * @return
     */
    @JsonIgnore
    public BAccUser getCurrentAssociate(final String queryingPersonEmail) {
        return new BAccUser("", "");
    }

    /**
     * useful for auto upcasting
     * not a business account so this returns null
     * @return
     */
    @JsonIgnore
    public ArrayList<BAccUser> getManagers() {
       return null;
    }

    /**
     * useful for auto upcasting
     * not a business account so this returns null
     * @return
     */
    @JsonIgnore
    public ArrayList<BAccUser> getEmployees() {
        return null;
    }



}
