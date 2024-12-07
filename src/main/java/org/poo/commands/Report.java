package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

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


    private ArrayList<Command> transactions;

    public Report(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
        this.transactions = new ArrayList<>();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);

        User user = null;
        try {
            user = getUserReference(users, IBAN);
        } catch (NotFoundException e) {
            this.addNotFoundError(timestamp, "Account not found", output);
            return;
        }

        Account cont = user.getAccount(IBAN);
        balance = cont.getBalance();
        currency = cont.getCurrency();


        for(Command transaction : user.getTransactions()) {
            if(transaction.timestamp >= this.startTimestamp && transaction.timestamp <= this.endTimestamp
                && cont.getIBAN().equals(transaction.getIBAN())) {
                transaction.addToList(transactions);
            }
        }

        objectNode.putPOJO("output", this);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);

    }
}
