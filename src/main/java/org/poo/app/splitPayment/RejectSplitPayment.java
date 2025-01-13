package org.poo.app.splitPayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.UserNotFound;
import org.poo.app.accounts.Account;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Getter
@Setter
public class RejectSplitPayment extends Command {
    private String email;
    private String type;
    private int timestamp;

    @JsonIgnore
    private HashMap<String, User> users;
    public RejectSplitPayment(CommandInput input, HashMap<String, User> users) {
        this.email = input.getEmail();
        this.type = input.getSplitPaymentType();
        this.timestamp = input.getTimestamp();
        this.users = users;
        this.cmdName = "rejectSplitPayment";
        this.timestampTheSecond = timestamp;
    }

    public void execute(ArrayNode output) {
        if(timestamp == 204) {
            System.out.println(2);
        }
        User user = null;
        try {
            user = getUserReference(users, email);
            if(!user.getEmail().equals(email)) {
                throw new UserNotFound();
            }
        } catch (NotFoundException e) {
            throw new UserNotFound();
        }

        SingleSplitPayment splitPayment;
        try {
            splitPayment = user.getSplitPayments().removeFirst();
        } catch (NoSuchElementException e) {
            return;
        }
        splitPayment.rejected();
    }



}