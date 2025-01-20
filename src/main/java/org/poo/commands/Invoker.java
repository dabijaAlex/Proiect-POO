package org.poo.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.accounts.NotABusinessAccountException;
import org.poo.app.accounts.userTypes.ChangeDepositLimitException;
import org.poo.app.accounts.userTypes.ChangeSpendingLimitException;
import org.poo.app.accounts.userTypes.NotAuthorizedException;

import java.util.ArrayList;

@Getter @Setter
public final class Invoker {
    private ArrayList<Command> cmds;
    private ArrayNode output;

    /**
     * Constructor
     * @param output
     */
    public Invoker(final ArrayNode output) {
        cmds = new ArrayList<>();
        this.output = output;
    }

    /**
     * execute each command
     * if it throws exception then handle it
     */
    public void solve() {
        for (Command command : cmds) {
            if (command != null) {
                try {
                    command.execute(output);
                } catch (NotFoundException | InsufficientFundsException
                         | NotAuthorizedException ignore) {
                } catch (UserNotFound e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "User not found");
                } catch (CardNotFound e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "Card not found");
                } catch (AccountNotFound e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "Account not found");
                } catch (ChangeSpendingLimitException e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "You must be owner in order to change spending limit.");
                } catch (ChangeDepositLimitException e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "You must be owner in order to change deposit limit.");
                } catch (NotABusinessAccountException e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "This is not a business account");
                } catch (NotASavingsAccount e) {
                    printError(command.timestampTheSecond, command.getCmdName(),
                            "This is not a savings account");
                }
            }
        }
    }

    private void printError(final int timestamp,
                            final String cmdName, final String description) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", description);

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);
    }

}
