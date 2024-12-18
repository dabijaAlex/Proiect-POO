package org.poo.commands;


import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.NotFoundException;

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
                } catch (NotFoundException ignore) {
                    continue;
                }
            }
        }
    }

}
