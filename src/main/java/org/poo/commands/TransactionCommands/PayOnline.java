package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.ExchangeRateList;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.commands.CreateOneTimeCard;
import org.poo.fileio.CommandInput;
import org.poo.transactions.FrozenCardTransaction;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.PayOnlineTransaction;

import java.util.HashMap;

@Getter @Setter
public class PayOnline extends Command {
    HashMap<String, User> users;
    private String account;


    private int timestamp;
    private String description;
    private double amount;
    private String commerciant;



    public PayOnline(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.cardNumber = command.getCardNumber();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.timestamp = command.getTimestamp();
//        this.description = command.getDescription();
        super.timestamp = timestamp;

        this.description = "Card payment";
        this.commerciant = command.getCommerciant();
        this.email = command.getEmail();
        this.users = users;
    }
    public void execute(ArrayNode output) {
        User user = users.get(cardNumber);

        if(user == null) {
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

        Account cont = user.getAccount(cardNumber);
        if(cont == null) {
            return;
        }

        Card card = cont.getCard(cardNumber);
        if(card != null && card.getStatus().equals("active")) {
            double convRate = 1;
            if(currency.equals(cont.getCurrency()) == false) {
                convRate = ExchangeRateList.convertRate(currency, cont.getCurrency());
            }

            super.account = cont.getIBAN();

            if(cont.getBalance() < amount * convRate) {
                cont.addTransaction(new InsufficientFundsTransaction(timestamp));
                return;
            }
            cont.setBalance(cont.getBalance() - amount * convRate);
            if(card.useCard(cont, users) == true) {
                amount = amount * convRate;
                account = cont.getIBAN();
                super.account = account;
                cont.addTransaction(new PayOnlineTransaction(timestamp, description, amount, commerciant));


                DeleteCard del = new DeleteCard(timestamp, cardNumber, users);
                del.execute(output);
                CreateOneTimeCard cr = new CreateOneTimeCard(timestamp, user.getEmail(), cont.getIBAN(), users);
                cr.execute(output);

                return;
            }

            amount = amount * convRate;
            account = cont.getIBAN();
            cont.addTransaction(new PayOnlineTransaction(timestamp, description, amount, commerciant));

        }
        if(card != null && card.getStatus().equals("frozen")) {
            cont.addTransaction(new FrozenCardTransaction(timestamp));
            return;
        }

    }
}
