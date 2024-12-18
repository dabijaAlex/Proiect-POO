package org.poo.commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.Object;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.*;
import org.poo.fileio.CommandInput;
import org.poo.transactions.*;
import org.poo.utils.Utils;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter @Setter
public class AddFunds extends Command {
    private HashMap<String, User> users;
    private String IBAN;
    private double amount;

    public AddFunds(CommandInput command, HashMap<String, User> users) {
        this.IBAN = command.getAccount();
        this.amount = command.getAmount();
        this.users = users;

    }
    public void execute(final ArrayNode output) throws NotFoundException {
        Account cont = getAccountReference(users, IBAN);
        cont.setBalance(cont.getBalance() + amount);
    }
}


@Getter @Setter
class FailedDeleteCard extends Command {
    private String description;
    private int timestamp;

    public FailedDeleteCard(String description, int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
        super.timestamp = timestamp;
    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}

@Getter @Setter
class DeleteAccount extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    private int timestamp;
    private String IBAN;
    private String email;
    private String cmdName;

    public DeleteAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
        this.account = IBAN;

        this.users = users;
    }
    public void execute(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        User user = null;
        Account cont = null;

        try {
            user = getUserReference(users, IBAN);
            cont = getAccountReference(users, IBAN);
        } catch (NotFoundException e) {

                outputNode.put("timestamp", timestamp);
                outputNode.put("description", "User not found");

                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);

                output.add(objectNode);
                return;
        }


        if(cont.getBalance() > 0) {
            outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
            super.account = cont.getIBAN();
            cont.addTransaction(new FailedDeleteAccountTransaction(timestamp));
            user.addTransaction(new FailedDeleteCard("Account couldn't be deleted - there are funds remaining", timestamp));

            return;
        }

        user.deleteAccount(cont);
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);


        for(Card card : cont.getCards()) {
            users.remove(card.getCardNumber());
        }
        users.remove(IBAN);
    }

}


@Getter @Setter
class DeleteCard extends Command {
    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    @JsonIgnore
    private HashMap<String, User> users;
    @JsonIgnore
    private String IBAN;

    public DeleteCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;
        this.card = command.getCardNumber();
        this.users = users;
        this.description = "The card has been destroyed";
    }

    public DeleteCard(int timestamp, String cardNumber, HashMap<String, User> users) {
        this.timestamp = timestamp;
        super.timestamp = timestamp;
        this.card = cardNumber;
        this.users = users;
        this.description = "The card has been destroyed";
        this.cmdName = "deleteCard";
    }


    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, card);
        Account acc = getAccountReference(users, card);

        IBAN = acc.getIBAN();

        cardHolder = user.getEmail();
        account = acc.getIBAN();


        acc.deleteCard(card);
        users.remove(card);

        super.account = acc.getIBAN();
        acc.addTransaction(new DeleteCardTransaction(timestamp, description, card, cardHolder, account));
        user.addTransaction(this);
    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}


@Getter @Setter
class InsufficientFunds extends Command {
    private int timestamp;
    private String description;
    @JsonIgnore
    private String IBAN;
    public InsufficientFunds(int timestamp, String IBAN) {
        this.timestamp = timestamp;
        super.timestamp = timestamp;
        description = "Insufficient funds";
        this.IBAN = IBAN;
    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}

@Getter @Setter
class FrozenCard extends Command {
    private int timestamp;
    private String description;
    public FrozenCard(int timestamp) {
        this.timestamp = timestamp;
        description = "The card is frozen";
    }
}



@Getter @Setter
class PayOnline extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    @JsonIgnore
    private String account;
    @JsonIgnore
    private String IBAN;

    private int timestamp;
    private String description;
    private double amount;
    private String commerciant;



    public PayOnline(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.cardNumber = command.getCardNumber();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.timestamp = command.getTimestamp();
//        this.description = command.getDescription();
        super.timestamp = timestamp;

        this.description = "Card payment";
        this.commerciant = command.getCommerciant();
        this.email = command.getEmail();
        this.users = users;
    }
    public void execute(ArrayNode output) {
        User user = users.get(cardNumber);

        if(user == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", cmdName);
            ObjectNode outputNode = mapper.createObjectNode();


            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");

            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        }

        Account cont = user.getAccount(cardNumber);
        if(cont == null) {
            return;
        }
        this.IBAN = cont.getIBAN();

        Card card = cont.getCard(cardNumber);
        if(card != null && card.getStatus().equals("active")) {
            double convRate = 1;
            if(currency.equals(cont.getCurrency()) == false) {
                convRate = ExchangeRateList.convertRate(currency, cont.getCurrency());
            }

            super.account = cont.getIBAN();

            if(cont.getBalance() < amount * convRate) {
                cont.addTransaction(new PayOnlineTransaction(timestamp, description, amount, commerciant));
                user.addTransaction(new InsufficientFunds(timestamp, cont.getIBAN()));
                return;
            }
            cont.setBalance(cont.getBalance() - amount * convRate);
            if(card.useCard(cont, users) == true) {
                amount = amount * convRate;
                account = cont.getIBAN();
                super.account = account;
                cont.addTransaction(new PayOnlineTransaction(timestamp, description, amount, commerciant));
                user.addTransaction(this);


                DeleteCard del = new DeleteCard(timestamp, cardNumber, users);
                del.execute(output);
                CreateOneTimeCard cr = new CreateOneTimeCard(timestamp, user.getEmail(), cont.getIBAN(), users);
                cr.execute(output);

                return;
            }

            amount = amount * convRate;
            account = cont.getIBAN();
            cont.addTransaction(new PayOnlineTransaction(timestamp, description, amount, commerciant));
            user.addTransaction(this);

        }
        if(card != null && card.getStatus().equals("frozen")) {
            cont.addTransaction(new FrozenCardTransaction(timestamp));
            user.addTransaction(new FrozenCard(timestamp));
            return;
        }

    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }

    public void addSpendingToList(ArrayList<Command> lista, String account) {
        if(this.account.equals(account))
            lista.add(this);
    }

    @Override @JsonIgnore
    public double getAmountdouble() {
        return this.amount;
    }

    @JsonIgnore
    public String getCommerciant2() {
        return this.commerciant;
    }

}

@Getter @Setter
class SendMoneyReceiver extends Command {
    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private String transferType;
    private int timestamp;

    @JsonIgnore
    private String IBAN;
    public SendMoneyReceiver(int timestamp, String senderIBAN, String amount,
                             String receiverIBAN, String description, String transferType) {
        this.timestamp = timestamp;
        super.timestamp = timestamp;
        this.senderIBAN = senderIBAN;
        this.amount = amount;
        this.receiverIBAN = receiverIBAN;
        this.description = description;
        this.transferType = transferType;

        super.account = receiverIBAN;
        IBAN = receiverIBAN;
    }
    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}



@Getter @Setter
class SendMoney extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    @JsonIgnore
    private double amountAsDouble;
    @JsonIgnore
    private String senderAlias;
    @JsonIgnore
    private String receiverAlias;
    @JsonIgnore
    private String IBAN;

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
//        this.senderIBAN = command.getAccount();
        this.amountAsDouble = command.getAmount();
//        this.receiverIBAN = command.getReceiver();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;

        this.description = command.getDescription();
        this.users = users;

        transferType = "sent";
    }

    public void execute(ArrayNode output) {
        if (timestamp == 262)
            System.out.println("acd");

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


        //  check if diff curencies
        double convRate = 1;
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            convRate = ExchangeRateList.convertRate(senderAccount.getCurrency(), receiverAccount.getCurrency());
        }

        //  sender doesn t have the money
        if (senderAccount.getBalance() < amountAsDouble) {

            senderAccount.addTransaction(new InsufficientFundsTransaction(timestamp));
            sender.addTransaction(new InsufficientFunds(timestamp, senderAccount.getIBAN()));
            return;
        }
        senderAccount.setBalance(senderAccount.getBalance() - amountAsDouble);
        receiverAccount.setBalance(receiverAccount.getBalance() + amountAsDouble * convRate);
        amount = amountAsDouble + " " + senderAccount.getCurrency();
        this.IBAN = senderIBAN;
        senderAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amount, receiverIBAN, "sent", transferType, timestamp));
        sender.addTransaction(this);


        receiverUser.addTransaction(new SendMoneyReceiver(timestamp, senderIBAN,
                amountAsDouble * convRate + " " + receiverAccount.getCurrency(), receiverIBAN,
                description, "received"));
        receiverAccount.addTransaction(new SendMoneyTransaction(senderIBAN, amount, receiverIBAN, "received", transferType, timestamp));

    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}





