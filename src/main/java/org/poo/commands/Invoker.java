package org.poo.commands;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Invoker {
    private ArrayList<Command> cmds;

    public Invoker() {
        cmds = new ArrayList<>();
    }

    public void solve() {
        for (Command command : cmds) {
            command.execute();
        }
    }

}
