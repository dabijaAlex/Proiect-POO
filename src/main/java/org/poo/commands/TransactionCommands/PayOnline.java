package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
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
    private String cardNumber;
    private String currency;
    private String email;


    public PayOnline(CommandInput command, HashMap<String, User> users) {
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


    public void execute(ArrayNode output) {
//        User user = users.get(cardNumber);
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

        Card card = cont.getCard(cardNumber);
        if(card.getStatus().equals("frozen")) {
            cont.addTransaction(new FrozenCardTransaction(timestamp));
            return;
        }

        //  card is active

        //  get conv rate
        double convRate = 1;
        if(!currency.equals(cont.getCurrency())) {
            convRate = ExchangeRateList.convertRate(currency, cont.getCurrency());
        }

        //  check for sufficient funds
        if(cont.getBalance() < amount * convRate) {
            cont.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }

        cont.setBalance(cont.getBalance() - amount * convRate);
        if(card.useCard(cont, users)) {
            amount = amount * convRate;
            account = cont.getIBAN();
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
}
