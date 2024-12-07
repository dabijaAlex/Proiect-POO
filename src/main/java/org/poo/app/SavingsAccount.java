package org.poo.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.AddInterest;
import org.poo.commands.ChangeInterestRate;
import org.poo.commands.Command;

@Getter @Setter
class SuccessChangeInterestRate extends Command {
    private String description;
    private int timestamp;
    public SuccessChangeInterestRate(int timestamp, double interestRate) {
        this.description = "Interest rate of the account changed to " + interestRate;
        this.timestamp = timestamp;
        super.timestamp = timestamp;
    }
}

@Getter
@Setter
public class SavingsAccount extends Account {
    public SavingsAccount(String IBAN, double balance, String currency, String type) {
        super(IBAN, balance, currency, type);
    }

    @Override
    public void addInterest(ArrayNode output, AddInterest command) {
        balance += balance * interestRate;
    }

    public void setInterestRate(double interestRate, ArrayNode output, User user, ChangeInterestRate command) {
        this.interestRate = interestRate;

        user.addTransaction(new SuccessChangeInterestRate(command.getTimestamp(), interestRate));
    }
}
