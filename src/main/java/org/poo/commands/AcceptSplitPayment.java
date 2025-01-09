package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.splitPayment.SingleSplitPayment;
import org.poo.app.splitPayment.SplitPaymentDB;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Getter @Setter
public class AcceptSplitPayment extends Command {
    private String email;
    private String type;
    private int timestamp;

    @JsonIgnore
    private HashMap<String, User> users;
    public AcceptSplitPayment(CommandInput input, HashMap<String, User> users) {
        this.email = input.getEmail();
        this.type = input.getSplitPaymentType();
        this.timestamp = input.getTimestamp();
        this.users = users;
    }

    public void execute(ArrayNode output) {
        User user = getUserReference(users, email);
        Account account = user.getAccountsForSplitPayment().poll();
        SplitPaymentDB.SetAcceptedPayment(account, type);
        SplitPaymentDB.checkAllAccepted();
        ArrayList<SingleSplitPayment> a = SplitPaymentDB.getSplitPaymentsList();// daca da refuze iau pointerul asta si sterg din fiecare user cu cont prima apraitie a chestieiastea
    }



}
