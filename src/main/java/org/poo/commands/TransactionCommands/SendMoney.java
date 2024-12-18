package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.ExchangeRateList;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.SendMoneyTransaction;

import java.util.HashMap;

@Getter @Setter
public final class SendMoney extends Command {
    private HashMap<String, User> users;
    private double amountAsDouble;
    private String senderIdentifier;
    private String receiverIdentifier;

    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private int timestamp;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SendMoney(final CommandInput command, final HashMap<String, User> users) {
        this.senderIdentifier = command.getAccount();
        this.receiverIdentifier = command.getReceiver();
        this.cmdName = command.getCommand();
        this.amountAsDouble = command.getAmount();
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();

        this.users = users;
    }

    /**
     * get sender and receiver accounts references (if fails it will throw)
     *
     * check identifier for sender is not an alias
     *
     * get the currency conversion
     *
     * if sender doesn t have the money add failed transaction to sender and return else
     * make the transfer and add transaction to oth accounts
     *
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        Account senderAccount = getAccountReference(users, senderIdentifier);
        Account receiverAccount = getAccountReference(users, receiverIdentifier);

        senderIBAN = senderAccount.getIBAN();
        receiverIBAN = receiverAccount.getIBAN();

        //  cand primim identifierul verificam ca acesta sa nu fie un alias pt sender
        if (!senderIBAN.equals(senderIdentifier)) {
            return;
        }

        //  check if diff currencies
        double convRate = 1;
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            convRate = ExchangeRateList.convertRate(senderAccount.getCurrency(),
                    receiverAccount.getCurrency());
        }

        //  sender doesn t have the money
        if (senderAccount.getBalance() < amountAsDouble) {
            senderAccount.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }

        senderAccount.setBalance(senderAccount.getBalance() - amountAsDouble);
        receiverAccount.setBalance(receiverAccount.getBalance() + amountAsDouble * convRate);
        amount = amountAsDouble + " " + senderAccount.getCurrency();

        //  add transactions for both accounts
        senderAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amount,
                receiverIBAN, description, "sent", timestamp));

        receiverAccount.addTransaction(new SendMoneyTransaction(senderIBAN,
                amountAsDouble * convRate + " " + receiverAccount.getCurrency(),
                receiverIBAN, description, "received", timestamp));

    }
}
