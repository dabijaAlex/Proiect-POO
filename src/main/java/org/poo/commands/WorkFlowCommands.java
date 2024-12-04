package org.poo.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.Object;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

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
    private HashMap<String, User> users;
    public AddAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();
        this.users = users;

    }
    public void execute(final ArrayNode output) {
        User user = users.get(email);
        String IBAN = Utils.generateIBAN();
        users.put(IBAN, user);
        user.addAccount(new Account(IBAN, 0, currency, accountType));
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
    private HashMap<String, User> users;

    public CreateCard(CommandInput command, HashMap<String, User> users) {
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
        if(cont != null)
            cont.addCard(new Card(Utils.generateCardNumber(), "active"));
    }
}

@Getter @Setter
class CreateOneTimeCard extends Command {
    public CreateOneTimeCard(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
    }
    public void execute(final ArrayNode output) {}

}

@Getter @Setter
class DeleteAccount extends Command {
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
        if(user == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("timestamp", 5);
            objectNode.put("description", "User not found");

            objectNode.set("output", objectNode);


            return;
        }
        Account cont = user.getAccount(account);
        if(cont != null)
            user.deleteAccount(cont);
        else {
        }
    }
}


@Getter @Setter
class DeleteCard extends Command {
    public DeleteCard(CommandInput command) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();
    }
    public void execute(final ArrayNode output) {}
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
    public CheckCardStatus(CommandInput command) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();
    }
    public void execute() {}
}

@Getter @Setter
class PayOnline extends Command {
    public PayOnline(CommandInput command) {
        this.cmdName = command.getCommand();
        this.cardNumber = command.getCardNumber();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
        this.commerciant = command.getCommerciant();
        this.email = command.getEmail();
    }
    public void execute() {}
}


@Getter @Setter
class SendMoney extends Command {
    public SendMoney(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.amount = command.getAmount();
        this.receiver = command.getReceiver();
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
    }
    public void execute() {}
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



