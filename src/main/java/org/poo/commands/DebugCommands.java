package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


//public void getCardsInHand(final Match match, final ArrayNode output) {
//    ObjectMapper mapper = new ObjectMapper();
//    Player player = match.getPlayerByIdx(match.getCurrentCommand().getPlayerIdx());
//    ObjectNode objectNode = mapper.createObjectNode();
//    objectNode.put("command", "getCardsInHand");
//    objectNode.put("playerIdx", player.getIdx());
//    objectNode.putPOJO("output", player.getHandCardsCopy());
//    output.add(objectNode);
//}



@Getter @Setter
class PrintUsers extends Command {
    private HashMap<String, User> users;
     public PrintUsers(CommandInput command, HashMap<String, User> users) {
         this.cmdName = command.getCommand();
         this.timestamp = command.getTimestamp();
         this.users = users;
     }

     public void execute(final ArrayNode output) {
         ObjectMapper mapper = new ObjectMapper();
         ObjectNode objectNode = mapper.createObjectNode();
         objectNode.put("command", cmdName);

         ArrayNode usersArray = mapper.createArrayNode();

         Collection<User> usersCollection = this.users.values();
         Set<User> usersSet = new HashSet<>(usersCollection);
         for(User user : usersSet) {
            usersArray.addPOJO(new User(user));
         }
         objectNode.set("output", usersArray);

         objectNode.put("timestamp", timestamp);

         output.add(objectNode);
     }
}

@Getter @Setter
class PrintTransactions extends Command {
    public PrintTransactions(CommandInput command) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.email = command.getEmail();
    }
    public void execute() {
    }
}




