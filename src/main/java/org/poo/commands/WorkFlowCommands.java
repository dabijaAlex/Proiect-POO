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
        super.timestamp = timestamp;
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

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
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
        super.timestamp = timestamp;
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


//        System.out.println("add the card");
        user.addTransaction(this);
    }




    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}

@Getter @Setter
class CreateOneTimeCard extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    public CreateOneTimeCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;
        this.users = users;

        this.description = "New card created";
    }
    public void execute(final ArrayNode output) {
        User user = users.get(cardHolder);
        if(user == null)
            return;
        Account cont = user.getAccount(account);
        if(cont != null) {
            cardNumber = Utils.generateCardNumber();
            cont.addCard(new Card(cardNumber, "active"));
            this.card = cardNumber;
            users.put(cardNumber, user);
            user.addTransaction(this);


        }

    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
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
        super.timestamp = timestamp;
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

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}

@Getter @Setter
class SetMinBalance extends Command {
    HashMap<String, User> users;


    public SetMinBalance(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(ArrayNode output) {
        User user = users.get(account);
        if(user == null) {
            return;
        }
        Account acc = user.getAccount(account);
        acc.setMinBalance(amount);

    }
}

@Getter @Setter
class CheckCardStatus extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    private int timestamp;
    private String description;


    public CheckCardStatus(CommandInput command, HashMap<String, User> users) {
        this.users = users;
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();
    }
    public void execute(ArrayNode output) {
        User user = users.get(cardNumber);
        if(user == null) {
            description = "Card not found";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("command", cmdName);

            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", description);

            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);

            output.add(objectNode);
            return;
        } else {
            Account acc = user.getAccount(cardNumber);
            if(acc.getCard(cardNumber).getStatus().equals("frozen"))
                return;

            if(acc.getBalance() <= acc.getMinBalance()) {
                description = "You have reached the minimum amount of funds, the card will be frozen";
                acc.getCard(cardNumber).setStatus("frozen");
            }
            else if (acc.getBalance() <= acc.getMinBalance() + 30) {
                description = "Warning";
            }
        }
        user.addTransaction(this);
    }
}


@Getter @Setter
class InsufficientFunds extends Command {
    private int timestamp;
    private String description;
    public InsufficientFunds(int timestamp) {
        this.timestamp = timestamp;
        description = "Insufficient funds";
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
                    user.addTransaction(new InsufficientFunds(timestamp));
                    return;
                }
                cont.setBalance(cont.getBalance() - amount * convRate);
                card.useCard();

                amount = amount * convRate;
                account = cont.getIBAN();
                user.addTransaction(this);

            }
            if(card != null && card.getStatus().equals("frozen")) {
                user.addTransaction(new FrozenCard(timestamp));
                return;
            }

        }

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
        User sender = users.get(senderAlias);
        User receiverUser = users.get(receiverAlias);
        if(sender == null) {
            return;
        }
        senderIBAN = sender.getAccount(senderAlias).getIBAN();
        Account senderAccount = sender.getAccount(senderIBAN);
        if(receiverUser == null) {
//            amount = amountAsDouble + " " + senderAccount.getCurrency();
//            sender.addTransaction(this);
            return;
        }
        receiverIBAN = receiverUser.getAccount(receiverAlias).getIBAN();


        Account receiverAccount = receiverUser.getAccount(receiverIBAN);
        if(senderAccount == null || receiverAccount == null)
            return;

        double convRate = 1;
        if(!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            convRate = ExchangeRateList.convertRate(senderAccount.getCurrency(), receiverAccount.getCurrency());
        }

        if(senderAccount.getBalance() < amountAsDouble) {
            sender.addTransaction(new InsufficientFunds(timestamp));
            return;
        }
        senderAccount.setBalance(senderAccount.getBalance() - amountAsDouble);
        receiverAccount.setBalance(receiverAccount.getBalance() + amountAsDouble * convRate);
        amount = amountAsDouble + " " + senderAccount.getCurrency();
        sender.addTransaction(this);

    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}


@Getter @Setter
class SetAlias extends Command {
    private HashMap<String, User> users;
    public SetAlias(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.alias = command.getAlias();
        this.account = command. getAccount();

        this.users = users;
    }
    public void execute(ArrayNode output) {
        User user = users.get(email);
        if(user == null) {
            users.put(this.alias, user);
        }

    }
}


@Getter @Setter
class SplitPayment extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    @JsonIgnore
    private double total;
    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;

    public SplitPayment(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.involvedAccounts = command.getAccounts();
        this.timestamp = command.getTimestamp();
        this.currency = command.getCurrency();
        this.total = command.getAmount();

        this.description = "Split payment of " + String.format("%.2f", total) + " " + currency;

        this.users = users;
    }
    public void execute(ArrayNode output) {
        int nrAccounts = involvedAccounts.size();
        amount = total / nrAccounts;
        ArrayList<Double> paymentForEach = new ArrayList<>();
        ArrayList<Account> conturi = new ArrayList<>();
        for(String account : involvedAccounts) {
            User user = users.get(account);
            if(user == null) {
                return;
            }
            Account acc = user.getAccount(account);
            if(acc == null) {
                return;
            }
            double convRate = 1;
            if(currency.equals(acc.getCurrency()) == false) {
                convRate = ExchangeRateList.convertRate(currency, acc.getCurrency());
            }
//            acc.setBalance(acc.getBalance() - amount * convRate);
            paymentForEach.add(amount * convRate);
            conturi.add(acc);
//            user.addTransaction(this);
        }
        //  check they have money;
        for(int i = 0; i < nrAccounts; i++) {
            if(conturi.get(i).getBalance() < paymentForEach.get(i)) {
                return;
            }
        }

        for(int i = 0; i < nrAccounts; i++) {
            conturi.get(i).setBalance(conturi.get(i).getBalance() - paymentForEach.get(i));
            User user = users.get(conturi.get(i).getIBAN());
            if(user == null) {
                return;
            }
            user.addTransaction(this);
        }

    }
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
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    private String currency;
    @JsonIgnore
    private int timestamp;


    private ArrayList<Command> transactions;

    public Report(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.startTimestamp = command.getStartTimestamp();
        this.endTimestamp = command.getEndTimestamp();
        this.transactions = new ArrayList<>();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);




        User user = users.get(account);
        Account cont = user.getAccount(account);
        IBAN = this.account;
        balance = cont.getBalance();
        currency = cont.getCurrency();


        for(Command transaction : user.getTransactions()) {
            if(transaction.timestamp >= this.startTimestamp && transaction.timestamp <= this.endTimestamp) {
//                transactions.add(transaction);
                transaction.addToList(transactions);
            }
        }

        objectNode.putPOJO("output", this);
//        objectNode.putPOJO("output", transactions);

        objectNode.put("timestamp", timestamp);
        output.add(objectNode);

    }
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



