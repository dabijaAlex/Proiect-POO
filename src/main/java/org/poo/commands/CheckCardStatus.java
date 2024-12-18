package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardLimitReachedTransaction;

import java.util.HashMap;
import java.util.NoSuchElementException;

@Getter @Setter
final public class CheckCardStatus extends Command {
    HashMap<String, User> users;
    private String IBAN;
    private int timestamp;
    private String description;
    private String cardNumber;


    public CheckCardStatus(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();

        this.users = users;
    }
    public void execute(final ArrayNode output) {
        Account acc = null;
        try {
            acc = getAccountReference(users, cardNumber);
        } catch (NotFoundException e) {
                description = "Card not found";

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("command", cmdName);

                ObjectNode outputNode = mapper.createObjectNode();
                outputNode.put("timestamp", timestamp);
                outputNode.put("description", description);

                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);

                output.add(objectNode);
                return;
        }

        if(acc.getCard(cardNumber).getStatus().equals("frozen")) {
            return;
        }

        if(acc.getBalance() <= acc.getMinBalance()) {
            description = "You have reached the minimum amount of funds, the card will be frozen";
            acc.getCard(cardNumber).setStatus("frozen");

            acc.addTransaction(new CardLimitReachedTransaction(timestamp));

        }
    }
}
