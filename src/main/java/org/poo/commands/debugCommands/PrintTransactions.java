package org.poo.commands.debugCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

@Getter @Setter
public final class PrintTransactions extends Command {
    private int timestamp;
    private String email;
    private HashMap<String, User> users;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public PrintTransactions(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.email = command.getEmail();

        this.users = users;
    }

    /**
     * take the transactions from each account of an user, put them together and then sort them
     * by their timestamp
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        ArrayList<Transaction> transactions = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);


        User user = getUserReference(users, email);
        for (Account account : user.getAccounts()) {
            for (Transaction transaction : account.getTransactions()) {
                transactions.add(transaction);
            }
        }

        Collections.sort(transactions, new Comparator<Transaction>() {

            public int compare(final Transaction t1, final Transaction t2) {
                return t1.getTimestamp() - t2.getTimestamp();
            }
        });

        ArrayNode outputArray = mapper.createArrayNode();

        for (Transaction transaction : transactions) {
            outputArray.addPOJO(transaction);
        }

        objectNode.set("output", outputArray);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }
}
