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
import org.poo.commands.TransactionCommands.SplitPayment;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

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
        this.cmdName = "acceptSplitPayment";
        this.timestampTheSecond = timestamp;
    }

    public void execute(ArrayNode output) {
        User user = null;
        try {
            user = getUserReference(users, email);
            if(!user.getEmail().equals(email)) {
                throw new UserNotFound();
            }
        } catch (NotFoundException e) {
            throw new UserNotFound();
        }

        SingleSplitPayment splitPayment = null;
        ArrayList<SingleSplitPayment> splitPayments;
        try {
            splitPayments = user.getSplitPayments();
            for(SingleSplitPayment sp : splitPayments) {
                if(sp.getType().equals(type)) {
                    splitPayment = sp;
                    splitPayments.remove(sp);
                }
                break;
            }
        } catch (NoSuchElementException e) {
            return;
        }
        if(splitPayment == null)
            return;
        splitPayment.setAcceptedPayment(user);

        SplitPaymentDB.checkAllAccepted();
    }



}
