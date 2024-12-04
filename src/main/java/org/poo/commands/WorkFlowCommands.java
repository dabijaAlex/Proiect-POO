package org.poo.commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.Object;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.*;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



//public void getCardsInHand(final Match match, final ArrayNode output) {
//    ObjectMapper mapper = new ObjectMapper();
//    Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
//    ObjectNode objectNode = mapper.createObjectNode();
//    objectNode.put("command", "getCardsInHand");
//    objectNode.put("playerIdx", player.getIdx());
//    objectNode.putPOJO("output", player.getHandCardsCopy());
//    output.add(objectNode);
//}

@Getter @Setter
class AddAccount extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String currency;
    @JsonIgnore
    private String accountType;
    @JsonIgnore
    private double interestRate;


    private int timestamp;
    private String description;
    public AddAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();

        this.users = users;

        description = "New account created";
    }
    public void execute(final ArrayNode output) {
        User user = users.get(email);
        String IBAN = Utils.generateIBAN();
        users.put(IBAN, user);
        user.addAccount(new Account(IBAN, 0, currency, accountType));
        user.addTransaction(this);
    }

}

@Getter @Setter
class AddFunds extends Command {
    private HashMap<String, User> users;
    public AddFunds(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();
        this.users = users;

    }
    public void execute(final ArrayNode output) {
        User user = users.get(account);
        if(user == null)
            return;
        Account cont = user.getAccount(account);
        if(cont != null)
            cont.setBalance(cont.getBalance() + amount);
    }

}


@Getter @Setter
class CreateCard extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    public CreateCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();
        this.users = users;

        this.description = "New card created";
    }
    public void execute(final ArrayNode output) {
        User user = users.get(cardHolder);
        if(user == null)
            return;
        Account cont = user.getAccount(account);
        if(cont != null) {
            card = Utils.generateCardNumber();
            cont.addCard(new Card(card, "active"));
        }
        users.put(card, user);


        System.out.println("add the card");
        user.addTransaction(this);
    }
}

@Getter @Setter
class CreateOneTimeCard extends Command {
    private HashMap<String, User> users;

    public CreateOneTimeCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
        this.users = users;
    }
    public void execute(final ArrayNode output) {
        User user = users.get(email);
        if(user == null)
            return;
        Account cont = user.getAccount(account);
        if(cont != null) {
            cardNumber = Utils.generateCardNumber();
            cont.addCard(new Card(cardNumber, "active"));
        }
        users.put(cardNumber, user);
    }

}

@Getter @Setter
class DeleteAccount extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    public DeleteAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
        this.users = users;
    }
    public void execute(final ArrayNode output) {
        User user = users.get(account);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        if(user == null) {
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "User not found");

            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        }
        Account cont = user.getAccount(account);
        if(cont != null) {
            if(cont.getBalance() > 0) {
                outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
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
            users.remove(account);
        }
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
    public DeleteCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.card = command.getCardNumber();
        this.users = users;
        this.description = "The card has been destroyed";
    }
    public void execute(final ArrayNode output) {
        User user = users.get(card);
        if(user == null)
            return;
        Account acc = user.getAccount(card);
        if(acc != null) {
            cardHolder = user.getEmail();
            account = acc.getIBAN();


                acc.deleteCard(card);
                users.remove(card);
        }
        user.addTransaction(this);
    }
}

@Getter @Setter
class SetMinBalance extends Command {
    public SetMinBalance(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}
}

@Getter @Setter
class CheckCardStatus extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    private int timestamp;
    private String description;


    public CheckCardStatus(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();
    }
    public void execute(ArrayNode output) {
        User user = users.get(account);
        if(user == null) {
            description = "Card not found";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", cmdName);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", description);

            objectNode.set("output", outputNode);

            output.add(objectNode);
        } else {


            user.addTransaction(this);
        }
    }
}


@Getter @Setter
class FailedPayOnline extends Command {
    private int timestamp;
    private String description;
    public FailedPayOnline(int timestamp) {
        this.timestamp = timestamp;
        description = "Insufficient funds";
    }
}



@Getter @Setter
class PayOnline extends Command {
    @JsonIgnore
    HashMap<String, User> users;
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
        if(cont != null) {
            Card card = cont.getCard(cardNumber);
            if(card != null && card.getStatus().equals("active")) {
                double convRate = 1;
                if(currency.equals(cont.getCurrency()) == false) {
                    convRate = ExchangeRateList.convertRate(currency, cont.getCurrency());
                }
//                System.out.println(currency + cont.getCurrency());
//                System.out.println(convRate);
                if(cont.getBalance() < amount * convRate) {
                    user.addTransaction(new FailedPayOnline(timestamp));
                    return;
                }
                cont.setBalance(cont.getBalance() - amount * convRate);
                card.useCard();

                amount = amount * convRate;
                user.addTransaction(this);

            }

        }

    }
}


@Getter @Setter
class SendMoney extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    @JsonIgnore
    private double amountAsDouble;

    private String senderIBAN;
    private String amount;
    private String receiverIBAN;
    private String description;
    private String transferType;
    private int timestamp;


    public SendMoney(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.senderIBAN = command.getAccount();
        this.amountAsDouble = command.getAmount();
        this.receiverIBAN = command.getReceiver();
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
        this.users = users;

        transferType = "sent";
    }
    public void execute(ArrayNode output) {
        User sender = users.get(senderIBAN);
        User receiverUser = users.get(this.receiverIBAN);
        if(sender == null) {
            return;
        }
        Account senderAccount = sender.getAccount(senderIBAN);
        if(receiverUser == null) {
//            amount = amountAsDouble + " " + senderAccount.getCurrency();
//            sender.addTransaction(this);
            return;
        }

        Account receiverAccount = receiverUser.getAccount(receiverIBAN);
        if(senderAccount == null || receiverAccount == null)
            return;

        double convRate = 1;
        if(!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            convRate = ExchangeRateList.convertRate(senderAccount.getCurrency(), receiverAccount.getCurrency());
        }

        if(senderAccount.getBalance() < amountAsDouble) {
            return;
        }
        senderAccount.setBalance(senderAccount.getBalance() - amountAsDouble);
        receiverAccount.setBalance(receiverAccount.getBalance() + amountAsDouble * convRate);
        amount = amountAsDouble + " " + senderAccount.getCurrency();
        sender.addTransaction(this);

    }
}


@Getter @Setter
class SetAlias extends Command {
    public SetAlias(CommandInput command) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.alias = command.getAlias();
        this.account = command. getAccount();
    }
    public void execute() {}
}


@Getter @Setter
class SplitPayment extends Command {
    public SplitPayment(CommandInput command) {
        this.cmdName = command.getCommand();
        this.accountsForSplit = command.getAccounts();
        this.timestamp = command.getTimestamp();
        this.currency = command.getCurrency();
        this.amount = command.getAmount();
    }
    public void execute() {}
}



@Getter @Setter
class AddInterest extends Command {
    public AddInterest(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}
}



@Getter @Setter
class ChangeInterestRate extends Command {
    public ChangeInterestRate(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}
}



@Getter @Setter
class Report extends Command {
    public Report(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
    }
    public void execute() {}
}




@Getter @Setter
class SpendingReport extends Command {
    public SpendingReport(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
    }
    public void execute() {}
}



