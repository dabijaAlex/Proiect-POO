package org.poo.app;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Invoker;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import  org.poo.fileio.UserInput;

import org.poo.commands.CommandFactory;
import org.poo.commands.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class AppStart {
    private ArrayList<User> users;
    private HashMap<String, User> userHashMap;

    public AppStart(ObjectInput input, final ArrayNode output) {
        userHashMap = new HashMap<>();
        users = new ArrayList<>();
        for(UserInput usr : input.getUsers()) {
            User x = new User(usr);
            users.add(x);
            userHashMap.put(usr.getEmail(), x);
        }


        Invoker invoker = new Invoker(output);
        ArrayList<Command> commands = new ArrayList<>();
        CommandFactory factory = new CommandFactory();

        //  extract commands and init them with factory
        for(CommandInput commandInput: input.getCommands()) {
            commands.add(factory.createCommand(commandInput, userHashMap));
        }



        invoker.setCmds(commands);
        invoker.solve();



    }



}
