package org.poo.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.accounts.Account;

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
        int timestamp = 0;
        for (Command command : cmds) {
            timestamp += 1;
            if (command != null) {
                try {
                    command.execute(output);
                } catch (NotFoundException ignore) {
                    continue;
                } catch (InsufficientFundsException ignore) {
                    continue;
                } catch (UserNotFound e) {
                    printNotFoundError(output, command.timestampTheSecond, "User", command.getCmdName());
                } catch (CardNotFound e) {
                    printNotFoundError(output, command.timestampTheSecond, "Card", command.getCmdName());
                } catch (AccountNotFound e) {
                    printNotFoundError(output, command.timestampTheSecond, "Account", command.getCmdName());
                }

            }
        }
    }

    private void printNotFoundError(ArrayNode output, int timestamp, String type, String cmdName) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", type + " not found");

        objectNode.set("output", outputNode);
        objectNode.put("timestamp", timestamp);

        output.add(objectNode);
    }

}
