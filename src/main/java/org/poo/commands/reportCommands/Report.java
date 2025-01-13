package org.poo.commands.reportCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.AccountNotFound;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class Report extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    private String currency;
    @JsonIgnore
    private int timestamp;
    @JsonIgnore
    private int startTimestamp;
    @JsonIgnore
    private int endTimestamp;


    private ArrayList<Transaction> transactions;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public Report(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
        this.transactions = new ArrayList<>();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }

    /**
     * try and get account: if not found add error to output else add to output
     * the transactions that fit in the timestamps (the transactions are fond in each account)
     * @param output
     */
    public void execute(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);

        Account cont = null;
        try {
            cont = getAccountReference(users, IBAN);

        } catch (NotFoundException e) {
            throw new AccountNotFound();
        }

        balance = cont.getBalance();
        currency = cont.getCurrency();


        //  add transactions that fit in timestamp
        for (Transaction transaction : cont.getTransactions()) {
            if (transaction.getTimestamp() >= this.startTimestamp
                    && transaction.getTimestamp() <= this.endTimestamp) {
                transactions.add(transaction);
            }
        }

        objectNode.putPOJO("output", this);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);

    }
}
