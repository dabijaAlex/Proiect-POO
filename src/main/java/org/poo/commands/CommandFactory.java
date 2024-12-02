package org.poo.commands;

import org.poo.fileio.CommandInput;

public class CommandFactory {
    public Command createCommand(CommandInput command) {
        switch (command.getCommand()) {
            case "printUsers" -> {
                return new PrintUsers(command);
            }
            case "printTransactions" -> {
                return new PrintTransactions(command);
            }
            case "addAccount" -> {
                return new AddAccount(command);
            }
            case "addFunds" -> {
                return new AddFunds(command);
            }
            case "createCard" -> { // there is another type of card
                return new CreateCard(command);
            }
            case "createOneTimeCard" -> {
                return new CreateOneTimeCard(command);
            }
            case "deleteAccount" -> {
                return new DeleteAccount(command);
            }
            case "deleteCard" -> {
                return new DeleteCard(command);
            }
            case "setMinBalance" -> {
                return new SetMinBalance(command);
            }
            case "checkCardStatus" -> {
                return new CheckCardStatus(command);
            }
            case "payOnline" -> {
                return new PayOnline(command);
            }
            case "sendMoney" -> {
                return new SendMoney(command);
            }
            case "setAlias" -> {
                return new SetAlias(command);
            }
            case "addInterest" -> {
                return new AddInterest(command);
            }
            case "changeInterestRate" -> {
                return new ChangeInterestRate(command);
            }
            case "report" -> {
                return new Report(command);
            }
            case "spendingReport" -> {
                return new SpendingReport(command);
            }
            default -> {
                System.out.println(command.getCommand());
            }
        }
        return null;
    }
}
