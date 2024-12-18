package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.FailedDeleteAccountTransaction;

import java.util.HashMap;

@Getter @Setter
public class DeleteAccount extends Command {
    private HashMap<String, User> users;
    private int timestamp;
    private String IBAN;
    private String email;
    private String cmdName;

    public DeleteAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        User user = null;
        Account cont = null;

        try {
            user = getUserReference(users, IBAN);
            cont = getAccountReference(users, IBAN);
        } catch (NotFoundException e) {

                outputNode.put("timestamp", timestamp);
                outputNode.put("description", "User not found");

                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);

                output.add(objectNode);
                return;
        }


        if(cont.getBalance() > 0) {
            outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
            cont.addTransaction(new FailedDeleteAccountTransaction(timestamp));

            return;
        }

        user.deleteAccount(cont);
        //  clear cards
        for(Card card : cont.getCards()) {
            users.remove(card.getCardNumber());
        }
        users.remove(IBAN);

        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);


    }

}
