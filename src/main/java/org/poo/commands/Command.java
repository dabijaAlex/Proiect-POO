package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

//@Getter @Setter
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
}