package org.poo.commands.transactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.notFoundExceptions.CardNotFoundException;
import org.poo.app.InsufficientFundsException;
import org.poo.app.User;
import org.poo.app.notFoundExceptions.UserNotFoundException;
import org.poo.app.accounts.Account;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.ExchangeRateGraph;
import org.poo.commands.Command;
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

    /**
     * Constructor
     * @param input
     * @param users
     */
    public CashWithdrawal(final CommandInput input, final HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.cardNumber = input.getCardNumber();
        this.email = input.getEmail();
        this.location = input.getLocation();
        this.timestamp = input.getTimestamp();
        this.amount = input.getAmount();

        timestampTheSecond = timestamp;
    }

    /**
     * get account, check if card exists and emails match, make withdrawal if enough money
     * @param output
     * @throws InsufficientFundsException
     * @throws NotFoundException
     */
    @Override
    public void execute(final ArrayNode output) throws InsufficientFundsException,
            NotFoundException {
        User user = null;
        Account account = null;
        try {
            user = getUserReference(users, cardNumber); // throws not found
            account = getAccountReference(users, cardNumber); // throws not found
        } catch (NotFoundException e) {
            throw new CardNotFoundException();
        }

        try {
            user = getUserReference(users, email); // throws not found
        } catch (NotFoundException e) {
            throw new UserNotFoundException();
        }
        User userByCard = getUserReference(users, cardNumber);
        if (!userByCard.getEmail().equals(email)) {
            throw new CardNotFoundException();
        }

        double amountInAccountCurrency = ExchangeRateGraph.makeConversion("RON",
                account.getCurrency(), amount);

        double commission = user.getServicePlan().getCommissionAmount(amountInAccountCurrency,
                account.getCurrency());
        user.getServicePlan().addPayment(amount, account.getCurrency(), account, user, timestamp);

        if (account.getBalance() < amountInAccountCurrency + commission) {
            account.addTransaction(new InsufficientFundsTransaction(timestamp));
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance() - amountInAccountCurrency - commission);

        account.addTransaction(new CashWithdrawalTransaction(timestamp, amount));

    }
}
