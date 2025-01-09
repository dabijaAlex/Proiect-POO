package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.accounts.Account;
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
        } catch (NotFoundException e) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", cmdName);
            ObjectNode outputNode = mapper.createObjectNode();


            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");

            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        }
        if(cont.getIBAN().equals("RO37POOB7013767509830666"))
            System.out.println("2");

        Card card = cont.getCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            cont.addTransaction(new FrozenCardTransaction(timestamp));
            return;
        }

        //  card is active

        double amountInAccountCurrency = ExchangeRateGraph.makeConversion(currency, cont.getCurrency(), amount);
        double commission = cont.getServicePlan().getCommissionAmount(amountInAccountCurrency, cont.getCurrency());

        //  check for sufficient funds
        if (cont.getBalance() < amountInAccountCurrency + commission) {
            cont.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }


        cont.getServicePlan().addPayment(amountInAccountCurrency, cont.getCurrency(), cont, user);

        cont.setBalance(Math.round((cont.getBalance() - amountInAccountCurrency - commission) * 100.0) / 100.0);

        card.useCard(cont, users, amountInAccountCurrency, output, this);

        //  add cashback

        commerciant.PaymentHappened(amountInAccountCurrency, cont, cont.getCurrency());
        cont.setBalance(Math.round((cont.getBalance() + commerciant.getCashback(amountInAccountCurrency, cont)) * 100.0) / 100.0);
    }
}
