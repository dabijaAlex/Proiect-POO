package org.poo.commands.transactionCommands.splitPayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.notFoundExceptions.UserNotFoundException;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

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

    /**
     * Constructor
     * @param input
     * @param users
     */
    public RejectSplitPayment(final CommandInput input,
                              final HashMap<String, User> users) {
        this.email = input.getEmail();
        this.type = input.getSplitPaymentType();
        this.timestamp = input.getTimestamp();
        this.users = users;
        this.cmdName = "rejectSplitPayment";
        this.timestampTheSecond = timestamp;
    }

    /**
     * check it is valid email
     * get user based on email
     * remove first split payment from user queue and start rejected payment mechanism
     * @param output
     */
    public void execute(final ArrayNode output) throws UserNotFoundException {
        if (!email.contains("@")) {
            throw new UserNotFoundException();
        }
        User user = getUserReference(users, email);

        SingleSplitPayment splitPayment;
        try {
            splitPayment = user.getSplitPayments().removeFirst();
        } catch (NoSuchElementException e) {
            return;
        }
        splitPayment.rejected();
    }
}
