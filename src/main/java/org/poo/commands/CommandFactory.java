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
                return new PrintTransactions(command, userHashMap);
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
                return new CreateOneTimeCard(command, userHashMap);
            }
            case "deleteAccount" -> {
                return new DeleteAccount(command, userHashMap);
            }
            case "deleteCard" -> {
                return new DeleteCard(command, userHashMap);
            }
            case "setMinimumBalance" -> {
                return new SetMinBalance(command, userHashMap);
            }
            case "checkCardStatus" -> {
                return new CheckCardStatus(command, userHashMap);
            }
            case "payOnline" -> {
                return new PayOnline(command, userHashMap);
            }
            case "sendMoney" -> {
                return new SendMoney(command, userHashMap);
            }
            case "setAlias" -> {
                return new SetAlias(command, userHashMap);
            }
            case "addInterest" -> {
                return new AddInterest(command, userHashMap);
            }
            case "changeInterestRate" -> {
                return new ChangeInterestRate(command, userHashMap);
            }
            case "report" -> {
                return new Report(command, userHashMap);
            }
            case "spendingsReport" -> {
                return new SpendingReport(command, userHashMap);
            }
            case "splitPayment" -> {
                return new SplitPayment(command, userHashMap);
            }
            default -> {
                System.out.println(command.getCommand());
            }
        }
        return null;
    }
}
