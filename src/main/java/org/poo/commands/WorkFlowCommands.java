package org.poo.commands;


import lombok.Getter;
import lombok.Setter;

import org.poo.fileio.CommandInput;
import java.util.List;

@Getter @Setter
class AddAccount extends Command {
    public AddAccount(CommandInput command) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();
    }
    public void execute() {}

}

@Getter @Setter
class AddFunds extends Command {
    public AddFunds(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();
    }
}


@Getter @Setter
class CreateCard extends Command {
    public CreateCard(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}
}

@Getter @Setter
class CreateOneTimeCard extends Command {
    public CreateOneTimeCard(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}

}

@Getter @Setter
class DeleteAccount extends Command {
    public DeleteAccount(CommandInput command) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();
    }
    public void execute() {}
}


@Getter @Setter
class DeleteCard extends Command {
    public DeleteCard(CommandInput command) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();
    }
    public void execute() {}
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



