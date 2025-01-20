package org.poo.commands.TransactionCommands.splitPayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.UserNotFound;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Getter @Setter
public final class AcceptSplitPayment extends Command {
    private String email;
    private String type;
    private int timestamp;

    @JsonIgnore
    private HashMap<String, User> users;

    /**
     * Constructor
     * @param input
     * @param users
     */
    public AcceptSplitPayment(final CommandInput input,
                              final HashMap<String, User> users) {
        this.email = input.getEmail();
        this.type = input.getSplitPaymentType();
        this.timestamp = input.getTimestamp();
        this.users = users;
        this.cmdName = "acceptSplitPayment";
        this.timestampTheSecond = timestamp;
    }

    /**
     * get the first split payment of said type, set this user as accepted for
     *      the payment and then remove the payment from user's pending list
     * @param output
     */
    public void execute(final ArrayNode output) throws UserNotFound {
        if (!email.contains("@")) {
            throw new UserNotFound();
        }
        User user = getUserReference(users, email); // throws user not found


        SingleSplitPayment splitPayment = null;
        ArrayList<SingleSplitPayment> splitPayments;
        try {
            splitPayments = user.getSplitPayments();
            //  remove the split payment from user's list as he accepted it
            for (SingleSplitPayment sp : splitPayments) {
                if (sp.getType().equals(type)) {
                    splitPayment = sp;
                    splitPayments.remove(sp);
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            return;
        }
        if (splitPayment == null) {
            return;
        }
        splitPayment.setAcceptedPayment(user);

        // check if everyone accepted the payment and if yes start the transaction mechanism
        SplitPaymentDB.checkAllAccepted();
    }



}
