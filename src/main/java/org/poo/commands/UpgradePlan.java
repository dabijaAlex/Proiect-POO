package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.app.Account;
import org.poo.app.InsufficientFundsException;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.plans.AlreadyHasPlanException;
import org.poo.app.plans.CannotDowngradePlanException;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

public class UpgradePlan extends Command {

    private HashMap<String, User> users;
    private String newPlanType;
    private String iban;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public UpgradePlan(final CommandInput command, final HashMap<String, User> users) {
        this.iban = command.getAccount();
        this.newPlanType = command.getNewPlanType();
        this.users = users;

    }

    /**
     * add teh funds to the account if it exists else throw exception
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException ,
            InsufficientFundsException {
        Account cont = getAccountReference(users, iban);
        try {
            if (newPlanType.equals("silver")) {
                cont.upgradeServicePlanToSilver();
            }
            if (newPlanType.equals("gold")) {
                cont.upgradeServicePlanToGold();
            }
        } catch (AlreadyHasPlanException e) {
            // handle error
        } catch (CannotDowngradePlanException e) {
            // handle error
        }

    }
}
