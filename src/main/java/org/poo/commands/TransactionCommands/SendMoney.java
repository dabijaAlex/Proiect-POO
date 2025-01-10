package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
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

    @JsonIgnore
    private String email;


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

        this.email = command.getEmail();
        this.users = users;
        timestampTheSecond = timestamp;
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
    public void execute(final ArrayNode output) throws NotFoundException, UserNotFound {
        Commerciant commerciant = CommerciantMap.getCommerciantsMap().get(receiverIBAN);


        Account senderAccount = null;
        Account receiverAccount = null;
        User senderUser = null;
        try {
            senderAccount = getAccountReference(users, senderIdentifier);
            receiverAccount = getAccountReference(users, receiverIdentifier);
            senderUser = getUserReference(users, senderIdentifier);
        } catch (NotFoundException e) {
            throw new UserNotFound();
        }
        senderIBAN = senderAccount.getIBAN();
        receiverIBAN = receiverAccount.getIBAN();

        //  cand primim identifierul verificam ca acesta sa nu fie un alias pt sender
        if (!senderIBAN.equals(senderIdentifier)) {
            return;
        }

        //  check if diff currencies
//        double convRate = 1;
//        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
//            convRate = ExchangeRateGraph.convertRate(senderAccount.getCurrency(),
//                    receiverAccount.getCurrency());
//        }
        double amountInReceiverCurrency = ExchangeRateGraph.makeConversion(senderAccount.getCurrency(),
                receiverAccount.getCurrency(), amountAsDouble);

        //  sender doesn t have the money
        double senderCommission = senderAccount.getServicePlan().getCommissionAmount(amountAsDouble, senderAccount.getCurrency());
        if (senderAccount.getBalance() < amountAsDouble + senderCommission) {
            senderAccount.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }


        senderAccount.getServicePlan().addPayment(amountAsDouble, senderAccount.getCurrency(), senderAccount, senderUser);

//        senderAccount.setBalance(Math.round((senderAccount.getBalance() - amountAsDouble - senderCommission) * 100.0) / 100.0);
//        senderAccount.makePayment(Math.round((senderAccount.getBalance() - amountAsDouble - senderCommission) * 100.0) / 100.0);
        senderAccount.makePayment(amountAsDouble, senderCommission, email, timestamp);

        amount = amountAsDouble + " " + senderAccount.getCurrency();

        //  add transactions for both accounts
        senderAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amount,
                receiverIBAN, description, "sent", timestamp));

        //  receiver account was a commerciant
        if(commerciant != null) {
            //  add cashback
            commerciant.PaymentHappened(amountAsDouble, senderAccount, receiverAccount.getCurrency());
            senderAccount.setBalance(senderAccount.getBalance() + commerciant.getCashback(amountAsDouble, senderAccount));
            return;
        }


        receiverAccount.setBalance(receiverAccount.getBalance() + amountInReceiverCurrency);

        receiverAccount.addTransaction(new SendMoneyTransaction(senderIBAN,
                amountInReceiverCurrency + " " + receiverAccount.getCurrency(),
                receiverIBAN, description, "received", timestamp));
    }
}
