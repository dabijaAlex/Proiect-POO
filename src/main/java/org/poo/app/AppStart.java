package org.poo.app;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Invoker;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import org.poo.commands.CommandFactory;
import org.poo.commands.Command;

import java.util.ArrayList;


public class AppStart {


    public AppStart(ObjectInput input, final ArrayNode output) {
//        System.out.println("dada");
        Invoker invoker = new Invoker();
        ArrayList<Command> commands = new ArrayList<>();
        CommandFactory factory = new CommandFactory();

        //  extract commands and init them with factory
        for(CommandInput commandInput: input.getCommands()) {
            commands.add(factory.createCommand(commandInput));
        }
        invoker.setCmds(commands);



    }

}
