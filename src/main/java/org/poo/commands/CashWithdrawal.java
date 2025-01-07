package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CashWithdrawalTransaction;
import org.poo.transactions.InsufficientFundsTransaction;

import java.util.HashMap;

@Getter @Setter
public class CashWithdrawal extends Command {
    private HashMap<String, User> users;
    private String cardNumber;
    private double amount;
    private String email;
    private String location;
    private int timestamp;

    public CashWithdrawal(CommandInput input, HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.cardNumber = input.getCardNumber();
        this.email = input.getEmail();
        this.location = input.getLocation();
        this.timestamp = input.getTimestamp();
        this.amount = input.getAmount();

        timestampTheSecond = timestamp;
    }

    @Override
    public void execute(ArrayNode output) throws InsufficientFundsException, NotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        User user = null;
        Account account = null;



        try {
            user = getUserReference(users, cardNumber); // throws not found
            account = getAccountReference(users, cardNumber); // throws not found
        } catch (NotFoundException e) {
            throw new CardNotFound();
        }

        try {
            user = getUserReference(users, email); // throws not found
//            account = getAccountReference(users, email); // throws not found
        } catch (NotFoundException e) {
            throw new UserNotFound();
        }


        if(account.getIBAN().equals("RO37POOB7013767509830666"))
            System.out.println("2");

        double amountInAccountCurrency = ExchangeRateGraph.makeConversion("RON", account.getCurrency(), amount);

        double commission = account.getServicePlan().getCommissionAmount(amount);
        commission = ExchangeRateGraph.makeConversion("RON", account.getCurrency(), commission);

        if (account.getBalance() < amountInAccountCurrency + commission) {
            account.addTransaction(new InsufficientFundsTransaction(timestamp));
            throw new InsufficientFundsException();
        }

        account.setBalance((1000 * account.getBalance() - 1000 * amountInAccountCurrency - 1000 * commission) / 1000);

        account.addTransaction(new CashWithdrawalTransaction(timestamp, amount));

    }
}
