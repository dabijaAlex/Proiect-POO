package org.poo.commands.otherCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.FailedDeleteAccountTransaction;

import java.util.HashMap;

@Getter @Setter
public final class DeleteAccount extends Command {
    private HashMap<String, User> users;
    private int timestamp;
    private String iban;
    private String email;
    private String cmdName;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public DeleteAccount(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.iban = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }

    /**
     * try and get account refference. if it doesn t exits add error to output
     *
     * check if account still has money. if it does add error to output
     *
     * delete account and its associated cards
     * @param output
     */
    public void execute(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        User user = null;
        Account cont = null;

        try {
            user = getUserReference(users, iban);
            cont = getAccountReference(users, iban);
        } catch (NotFoundException e) {

                outputNode.put("timestamp", timestamp);
                outputNode.put("description", "User not found");

                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);

                output.add(objectNode);
                return;
        }


        if (cont.getBalance() > 0) {
            outputNode.put("error", "Account couldn't be deleted - "
                    + "see org.poo.transactions for details");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
            cont.addTransaction(new FailedDeleteAccountTransaction(timestamp));

            return;
        }

        user.deleteAccount(cont);
        //  clear cards
        for (Card card : cont.getCards()) {
            users.remove(card.getCardNumber());
        }
        users.remove(iban);

        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);
    }
}
