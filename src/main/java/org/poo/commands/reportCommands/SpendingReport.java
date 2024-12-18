package org.poo.commands.reportCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.*;


@Getter @Setter
class Commerciant extends Command {
    private String commerciant;
    private double total;
    public Commerciant(String commerciant, double total) {
        this.commerciant = commerciant;
        this.total = total;
    }
}


@Getter @Setter
public class SpendingReport extends Command {
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
    private ArrayList<Commerciant> commerciants;



    public SpendingReport(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
        this.timestamp = command.getTimestamp();

        this.users = users;
        this.transactions = new ArrayList<>();
        this.commerciants = new ArrayList<>();
    }


    public void execute(ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);



        ObjectNode outputNode = mapper.createObjectNode();


        User user = null;
        Account cont = null;
        try {
            user = getUserReference(users, IBAN);

            cont = getAccountReference(users, IBAN);
            balance = cont.getBalance();
            currency = cont.getCurrency();
        } catch (NotFoundException e) {
            addNotFoundError(timestamp, "Account not found", output);
            return;
        }


        //  check if it is a savings account
        if(cont.getType().equals("savings")) {
            outputNode.put("error", "This kind of report is not supported for a saving account");
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        }


        //  get all transactions by timestamp and IBAN that created them
        for(Transaction transaction : cont.getTransactions()) {
            if(transaction.getTimestamp() >= this.startTimestamp &&
                    transaction.getTimestamp() <= this.endTimestamp) {
//                transaction.addSpendingToList(transactions, this.IBAN);
                transaction.addSpendingTransactionToList(transactions);
            }
        }

        //  group transactions that have the same vendor
        for(Transaction transaction : transactions.reversed()) {
            int ok = 0;
            for(Commerciant commerciant : commerciants) {
                if(commerciant.getCommerciant() != null && commerciant.getCommerciant().equals(transaction.getCommerciant2())) {
                    commerciant.setTotal(commerciant.getTotal() + transaction.getAmountDouble());
                    ok = 1;
                    break;
                }
            }
            if(ok == 0) {
                commerciants.add(new Commerciant(transaction.getCommerciant2(), transaction.getAmountDouble()));
            }
        }
        sortCommerciantsAlphabetically();

        objectNode.putPOJO("output", this);

        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }


//fol clasa de comparator

    private void sortCommerciantsAlphabetically() {
        //  https://stackoverflow.com/questions/18895915/how-to-sort-an-array-of-objects-in-java
        Collections.sort(commerciants, new Comparator<Commerciant>(){

            public int compare(Commerciant o1, Commerciant o2)
            {
                return o1.getCommerciant().compareTo(o2.getCommerciant());
            }
        });
    }

}
