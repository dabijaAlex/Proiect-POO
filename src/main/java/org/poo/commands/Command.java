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

    public void execute(final ArrayNode output){
    }


    public User getUserReference(final HashMap<String, User> users, final String key) throws NotFoundException {
        User user = users.get(key);
        if(user == null)
            throw new NotFoundException();
        return user;
    }

    public Account getAccountReference(final HashMap<String, User> users, final String key) throws NotFoundException {
        User user = users.get(key);
        if(user == null)
            throw new NotFoundException();
        Account cont = user.getAccount(key);
        if(cont == null) {
            throw new NotFoundException();
        }
        return cont;
    }

    protected void addNotFoundError(final int timestamp, final String description, final ArrayNode output) {
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

}