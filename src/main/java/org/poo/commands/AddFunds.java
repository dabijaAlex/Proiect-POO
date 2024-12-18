package org.poo.commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.Object;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.*;
import org.poo.fileio.CommandInput;
import org.poo.transactions.*;
import org.poo.utils.Utils;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter @Setter
public class AddFunds extends Command {
    private HashMap<String, User> users;
    private String IBAN;
    private double amount;

    public AddFunds(CommandInput command, HashMap<String, User> users) {
        this.IBAN = command.getAccount();
        this.amount = command.getAmount();
        this.users = users;

    }
    public void execute(final ArrayNode output) throws NotFoundException {
        Account cont = getAccountReference(users, IBAN);
        cont.setBalance(cont.getBalance() + amount);
    }
}





