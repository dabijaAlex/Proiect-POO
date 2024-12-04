package org.poo.commands;

import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

public class CommandFactory {
    public Command createCommand(CommandInput command, HashMap<String, User> userHashMap) {
        switch (command.getCommand()) {
            case "printUsers" -> {
                return new PrintUsers(command, userHashMap);
            }
            case "printTransactions" -> {
                return new PrintTransactions(command);
            }
            case "addAccount" -> {
                return new AddAccount(command, userHashMap);
            }
            case "addFunds" -> {
                return new AddFunds(command, userHashMap);
            }
            case "createCard" -> {
                return new CreateCard(command, userHashMap);
            }
            case "createOneTimeCard" -> {
                return new CreateOneTimeCard(command);
            }
            case "deleteAccount" -> {
                return new DeleteAccount(command, userHashMap);
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
