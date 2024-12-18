package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.ExchangeRateList;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.SendMoneyTransaction;

import java.util.HashMap;

@Getter @Setter
public class SendMoney extends Command {
    HashMap<String, User> users;
    private double amountAsDouble;
    private String senderAlias;
    private String receiverAlias;

    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private String transferType;
    private int timestamp;


    public SendMoney(CommandInput command, HashMap<String, User> users) {
        this.senderAlias = command.getAccount();
        this.receiverAlias = command.getReceiver();
        this.cmdName = command.getCommand();
        this.amountAsDouble = command.getAmount();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;
        this.description = command.getDescription();

        this.users = users;

        transferType = "sent";
    }

    public void execute(ArrayNode output) {

        User sender = users.get(senderAlias);

        User receiverUser = users.get(receiverAlias);
        if (sender == null) {
            return;
        }
        senderIBAN = sender.getAccount(senderAlias).getIBAN();
        super.account = senderIBAN;

        //  senderul nu are voie sa fie alias
        if (senderIBAN.equals(senderAlias) == false) {
            return;
        }
        Account senderAccount = sender.getAccount(senderIBAN);
        if (receiverUser == null) {
            return;
        }
        receiverIBAN = receiverUser.getAccount(receiverAlias).getIBAN();


        Account receiverAccount = receiverUser.getAccount(receiverIBAN);
        if (senderAccount == null || receiverAccount == null)
            return;


        //  check if diff currencies
        double convRate = 1;
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            convRate = ExchangeRateList.convertRate(senderAccount.getCurrency(), receiverAccount.getCurrency());
        }

        //  sender doesn t have the money
        if (senderAccount.getBalance() < amountAsDouble) {

            senderAccount.addTransaction(new InsufficientFundsTransaction(timestamp));
            return;
        }
        senderAccount.setBalance(senderAccount.getBalance() - amountAsDouble);
        receiverAccount.setBalance(receiverAccount.getBalance() + amountAsDouble * convRate);
        amount = amountAsDouble + " " + senderAccount.getCurrency();
        senderAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amount, receiverIBAN, description, "sent", timestamp));
        sender.addTransaction(this);

        receiverAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amountAsDouble * convRate + " " + receiverAccount.getCurrency(), receiverIBAN, description, "received", timestamp));

    }
}
