package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.app.accounts.Account;
import org.poo.app.InsufficientFundsException;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.plans.AlreadyHasPlanException;
import org.poo.app.plans.CannotDowngradePlanException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AlreadyHasGoldPlanTransaction;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.UpgradePlanTransaction;

import java.util.HashMap;

public class UpgradePlan extends Command {

    private HashMap<String, User> users;
    private String newPlanType;
    private String iban;
    private int timestamp;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public UpgradePlan(final CommandInput command, final HashMap<String, User> users) {
        this.iban = command.getAccount();
        this.newPlanType = command.getNewPlanType();
        this.users = users;
        this.timestamp = command.getTimestamp();
        this.cmdName = command.getCommand();
        timestampTheSecond = command.getTimestamp();
    }

    /**
     * add teh funds to the account if it exists else throw exception
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException,
            InsufficientFundsException {


        User user = getUserReference(users, iban);
        Account cont = getAccountReference(users, iban);

        try {
            if (newPlanType.equals("silver")) {
                user.upgradeServicePlanToSilver(cont);
            }
            if (newPlanType.equals("gold")) {
                user.upgradeServicePlanToGold(cont);
            }
            cont.addTransaction(new UpgradePlanTransaction(iban, newPlanType, timestamp));
        } catch (AlreadyHasPlanException e) {
            // handle error
            cont.addTransaction(new AlreadyHasGoldPlanTransaction(timestamp, newPlanType));
        } catch (CannotDowngradePlanException e) {
            // handle error
        } catch (InsufficientFundsException e) {
            cont.addTransaction(new InsufficientFundsTransaction(timestamp));
        }

    }
}
