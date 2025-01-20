package org.poo.commands.reportCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.accounts.userTypes.BAccUser;
import org.poo.app.accounts.userTypes.CommerciantForBusiness;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


@Getter @Setter
public class BusinessReport extends Command {
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private String accountIban;
    private String type;
    private ArrayList<BAccUser> employees = new ArrayList<>();
    private ArrayList<BAccUser> managers = new ArrayList<>();

    private HashMap<String, User> users;

    /**
     * constructor
     * @param commandInput
     * @param users
     */
    public BusinessReport(final CommandInput commandInput,
                          final HashMap<String, User> users) {
        this.startTimestamp = commandInput.getStartTimestamp();
        this.endTimestamp = commandInput.getEndTimestamp();
        this.timestamp = commandInput.getTimestamp();
        this.accountIban = commandInput.getAccount();
        this.type = commandInput.getType();
        this.users = users;
    }

    /**
     * check wich type of statistic is wanted and output accordingly
     * @param output
     */
    public void execute(final ArrayNode output) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "businessReport");

        Account account = getAccountReference(users, accountIban);

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("IBAN", accountIban);
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", account.getSpendingLimit());
        outputNode.put("deposit limit", account.getDepositLimit());
        outputNode.put("statistics type", type);
        if (type.equals("transaction")) {
            transactionSide(account, outputNode);
        } else {
            commerciantSide(account, mapper, outputNode);
        }

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);

    }

    /**
     * commerciants report type
     * add all commerciants where managers and employees made payments
     * add the managers and employees and the amounts they spent
     * @param account
     * @param mapper
     * @param outputNode
     */
    private void commerciantSide(final Account account, final ObjectMapper mapper,
                                 final ObjectNode outputNode) {
        ArrayNode commercaints = mapper.createArrayNode();
        Collections.sort(account.getCommerciantsForBusiness(),
                new Comparator<CommerciantForBusiness>() {
            public int compare(final CommerciantForBusiness c1, final CommerciantForBusiness c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        for (CommerciantForBusiness c: account.getCommerciantsForBusiness()) {
            ObjectNode singleCommerciant = mapper.createObjectNode();
            singleCommerciant.put("commerciant", c.getName());
            singleCommerciant.put("total received", c.getTotalReceived());
            singleCommerciant.putPOJO("managers", c.getManagersCopy());
            singleCommerciant.putPOJO("employees", c.getEmployeesCopy());
            commercaints.add(singleCommerciant);
        }
        outputNode.set("commerciants", commercaints);
    }


    /**
     * transaction report type
     * add all the employees and managers that made payments or deposits
     * the refferences that we get are deep copies
     * @param account
     * @param outputNode
     */
    private void transactionSide(final Account account, final ObjectNode outputNode) {
        managers = account.getManagers();
        employees = account.getEmployees();

        double totalSpent = 0;
        double totalDeposited = 0;
        for (BAccUser employee : employees) {
            totalDeposited = totalDeposited
                    + employee.getDepositedInTimestamps(startTimestamp, endTimestamp);
            totalSpent = totalSpent
                    + employee.getSpentInTimestamps(startTimestamp, endTimestamp);

        }

        for (BAccUser manager : managers) {
            totalDeposited = totalDeposited
                    + manager.getDepositedInTimestamps(startTimestamp, endTimestamp);
            totalSpent = totalSpent
                    + manager.getSpentInTimestamps(startTimestamp, endTimestamp);

        }

        outputNode.put("total spent", totalSpent);
        outputNode.put("total deposited", totalDeposited);
        outputNode.putPOJO("managers", managers);
        outputNode.putPOJO("employees", employees);
    }
}
