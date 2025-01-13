package org.poo.commands.reportCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.AccountNotFound;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


@Getter @Setter
final class Commerciant2 extends Command {
    private String commerciant;
    private double total;

    /**
     * Constructor
     * @param commerciant
     * @param total
     */
    Commerciant2(final String commerciant, final double total) {
        this.commerciant = commerciant;
        this.total = total;
    }
}


@Getter @Setter
public final class SpendingReport extends Command {
    @JsonIgnore
    private int endTimestamp;
    @JsonIgnore
    private int startTimestamp;
    @JsonIgnore
    private HashMap<String, User> users;

    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    private String currency;
    @JsonIgnore
    private int timestamp;

    private ArrayList<Transaction> transactions;
    private ArrayList<Commerciant2> commerciants;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SpendingReport(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
        this.timestamp = command.getTimestamp();

        this.users = users;
        this.transactions = new ArrayList<>();
        this.commerciants = new ArrayList<>();
    }


    /**
     * try get account -> if there is no account add error to output
     * check acc type -> if it is "savings" add error to output
     *
     * get all spending transactions
     * group transactions by commerciants
     * sort the commerciants list alphabetically
     *
     * @param output
     */
    public void execute(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);



        ObjectNode outputNode = mapper.createObjectNode();


        Account cont = null;
        try {

            cont = getAccountReference(users, IBAN);
            balance = cont.getBalance();
            currency = cont.getCurrency();
        } catch (NotFoundException e) {
            throw new AccountNotFound();
        }


        //  check if it is a savings account
        if (cont.getType().equals("savings")) {
            outputNode.put("error", "This kind of report is not "
                    + "supported for a saving account");
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        }


        //  get all transactions by timestamp
        for (Transaction transaction : cont.getTransactions()) {
            if (transaction.getTimestamp() >= this.startTimestamp
                    && transaction.getTimestamp() <= this.endTimestamp) {
                transaction.addSpendingTransactionToList(transactions);
            }
        }

        //  group transactions that have the same vendor
        for (Transaction transaction : transactions.reversed()) {
            int ok = 0;
            for (Commerciant2 commerciant : commerciants) {
                if (commerciant.getCommerciant() != null
                        && commerciant.getCommerciant().equals(transaction.getCommerciant2())) {
                    commerciant.setTotal(commerciant.getTotal() + transaction.getAmountDouble());
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                commerciants.add(new Commerciant2(transaction.getCommerciant2(),
                        transaction.getAmountDouble()));
            }
        }
        sortCommerciantsAlphabetically();

        objectNode.putPOJO("output", this);

        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }


//fol clasa de comparator

    /**
     * sorting function that sorts commerciants alphabetically in the list
     */
    private void sortCommerciantsAlphabetically() {
        Collections.sort(commerciants, new Comparator<Commerciant2>() {

            public int compare(final Commerciant2 c1, final Commerciant2 c2) {
                return c1.getCommerciant().compareTo(c2.getCommerciant());
            }
        });
    }

}
