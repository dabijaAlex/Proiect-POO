package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {
    @JsonIgnore
    protected String cmdName = null;
    protected int timestamp = 0;
    protected String cardNumber = null;
    protected double amount = 0.0;
    protected String currency = null;
    protected String description = null;
    protected String commerciant = null;
    protected String email = null;
    protected String receiver = null;
    protected String account = null;
    protected double minBalance = 0.0;
    protected double interestRate = 0.0;
    protected String accountType = null;
    protected String alias = null;
    protected List<String> accountsForSplit= null;
    protected int startTimestamp = 0;
    protected int endTimestamp = 0;

    void execute(ArrayNode output){
    }
    void execute() {

    }
    public void addToList(ArrayList<Command> lista) {}

    public void addSpendingToList(ArrayList<Command> lista, String account) {}



    @JsonIgnore
   public String getCommerciant2() {
        return null;
   }

//   public double getAmount() {
//        return 0;
//   }

    @JsonIgnore
    public double getAmountdouble(){
        return 0;
    }


    public User getUserReference(HashMap<String, User> users, String key) throws NotFoundException {
        User user = users.get(key);
        if(user == null)
            throw new NotFoundException();
        return user;
    }

    public Account getAccountReference(HashMap<String, User> users, String key) throws NotFoundException {
        User user = users.get(key);
        if(user == null)
            throw new NotFoundException();
        Account cont = user.getAccount(key);
        if(cont == null) {
            throw new NotFoundException();
        }
        return cont;
    }

    protected void addNotFoundError(int timestamp, String description, ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();

        outputNode.put("timestamp", timestamp);
        outputNode.put("description", description);

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);
    }

    public String getCmdName() {
        return cmdName;
    }

    protected String getIBAN() {
        return null;
    }
}