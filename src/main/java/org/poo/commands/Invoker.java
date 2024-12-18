package org.poo.commands;


import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.NotFoundException;
import org.poo.commands.TransactionCommands.*;

import java.util.ArrayList;

@Getter @Setter
public class Invoker {
    private ArrayList<Command> cmds;
    private ArrayNode output;

    public Invoker(ArrayNode output) {
        cmds = new ArrayList<>();
        this.output = output;
    }

    public void solve() {
        for (Command command : cmds) {
            if(command != null) {
                try {
                    command.execute(output);
                } catch (NotFoundException ignore) {
                    continue;
                }
            }
        }
    }

}
