package org.poo.commands;


import org.poo.app.User;
import org.poo.app.splitPayment.AcceptSplitPayment;
import org.poo.app.splitPayment.RejectSplitPayment;
import org.poo.commands.TransactionCommands.*;
import org.poo.commands.debugCommands.PrintTransactions;
import org.poo.commands.debugCommands.PrintUsers;
import org.poo.commands.otherCommands.*;
import org.poo.commands.reportCommands.BusinessReport;
import org.poo.commands.reportCommands.Report;
import org.poo.commands.reportCommands.SpendingReport;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

public final class CommandFactory {
    /**
     * factory for commands. Get instances by matching string
     * @param command
     * @param userHashMap
     * @return
     */
    public Command createCommand(final CommandInput command,
                                 final HashMap<String, User> userHashMap) {
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
            case "withdrawSavings" -> {
                return new WithdrawSavings(command, userHashMap);
            }
            case "upgradePlan" -> {
                return new UpgradePlan(command, userHashMap);
            }
            case "cashWithdrawal" -> {
                return new CashWithdrawal(command, userHashMap);
            }
            case "acceptSplitPayment" -> {
                return new AcceptSplitPayment(command, userHashMap);
            }
            case "addNewBusinessAssociate" -> {
                return new addNewBusinessAssociate(command, userHashMap);
            }
            case "changeSpendingLimit" -> {
                return new ChangeSpendingLimit(command, userHashMap);
            }
            case "businessReport" -> {
                return new BusinessReport(command, userHashMap);
            }
            case "changeDepositLimit" -> {
                return new ChangeDepositLimit(command, userHashMap);
            }
            case "rejectSplitPayment" -> {
                return new RejectSplitPayment(command, userHashMap);
            }
            default -> {
                System.out.println(command.getCommand());
            }
        }
        return null;
    }
}
