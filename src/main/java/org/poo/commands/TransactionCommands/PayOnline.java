package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.accounts.Account;
import org.poo.app.accounts.BusinessAccount;
import org.poo.app.cashbackStrategies.SpendingThresholdStrategy;
import org.poo.app.commerciants.Commerciant;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.FrozenCardTransaction;
import org.poo.transactions.InsufficientFundsTransaction;

import java.util.HashMap;

@Getter @Setter
public final class PayOnline extends Command {
    private HashMap<String, User> users;
    private String account;


    private int timestamp;
    private String description;
    private double amount;
    private String commerciant;
    private String cardNumber;
    private String currency;
    private String email;
    private double convRate;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public PayOnline(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.cardNumber = command.getCardNumber();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.timestamp = command.getTimestamp();

        this.description = "Card payment";
        this.commerciant = command.getCommerciant();
        this.email = command.getEmail();
        this.users = users;
        this.timestampTheSecond = timestamp;
    }


    /**
     * if trying to get the account/ user fails catch the exception and add error to output
     *
     * if card is frozen add transaction to account and return
     *
     * get conversion rate
     *
     * check for enough money: if not then add transaction to account and return
     *
     * if the card is a one time then use it, delete it and generate a new one and
     *          add transaction
     * else just use the card and add transaction (these things are done in the use card
     * method that are specific to each type)
     *
     * @param output
     */
    public void execute(final ArrayNode output) {
        if (amount == 0)
            return;
        Commerciant commerciant = CommerciantMap.getCommerciantsMap().get(this.commerciant);
        User user = null;
        Account cont = null;
        try {
            user = getUserReference(users, cardNumber);
            cont = user.getAccount(cardNumber);
            if(cont.getCurrentAssociate(email) == null)
                throw new CardNotFound();
            if(!(cont instanceof BusinessAccount))
                if(!user.getEmail().equals(email)) {
                    throw new CardNotFound();
                }
        } catch (NotFoundException e) {
            throw new CardNotFound();
        }


        Card card = cont.getCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            cont.addTransaction(new FrozenCardTransaction(timestamp));
            return;
        }



        double amountInAccountCurrency = ExchangeRateGraph.makeConversion(currency, cont.getCurrency(), amount);
        double commission = user.getServicePlan().getCommissionAmount(amountInAccountCurrency, cont.getCurrency());


        //  check for sufficient funds
        if (cont.getBalance() < amountInAccountCurrency + commission) {
            cont.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }



        cont.makePayment(amountInAccountCurrency, commission, email, timestamp, commerciant);



        card.useCard(cont, users, amountInAccountCurrency, output, this);


        user.getServicePlan().addPayment(amountInAccountCurrency, cont.getCurrency(), cont, user, timestamp);


        //  add cashback
        double cashback = commerciant.getCashback(amountInAccountCurrency, cont);


        cont.setBalance(cont.getBalance() + cashback);
        commerciant.paymentHappened(amount, cont, currency);
    }
}
