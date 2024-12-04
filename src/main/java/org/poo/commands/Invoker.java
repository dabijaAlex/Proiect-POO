package org.poo.commands;


import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

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
            if(command != null)
                command.execute(output);
        }
    }

}
