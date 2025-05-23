package org.poo.commands.debugCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;



@Getter @Setter
public final class PrintUsers extends Command {
    private int timestamp;
    private HashMap<String, User> users;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
     public PrintUsers(final CommandInput command, final HashMap<String, User> users) {
         this.cmdName = command.getCommand();
         this.timestamp = command.getTimestamp();
         this.users = users;
     }

    /**
     * take all values from hashmap and place them in a set
     * make the set a list and then sort the users by their index (when they were created)
     * @param output
     */
     public void execute(final ArrayNode output) {
         ObjectMapper mapper = new ObjectMapper();
         ObjectNode objectNode = mapper.createObjectNode();
         objectNode.put("command", cmdName);

         ArrayNode usersArray = mapper.createArrayNode();

         Collection<User> usersCollection = this.users.values();
         Set<User> usersSet = new HashSet<>(usersCollection);
         List<User> userList = new ArrayList<>(usersSet);
         Collections.sort(userList, new Comparator<User>() {

             public int compare(final User u1, final User u2) {
                 return u1.getIndex() - u2.getIndex();
             }
         });

         for (User user : userList) {
            usersArray.addPOJO(new User(user));
         }
         objectNode.set("output", usersArray);

         objectNode.put("timestamp", timestamp);

         output.add(objectNode);
     }
}




