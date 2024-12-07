package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.*;


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
         List<User> list = new ArrayList<>(usersSet);
         list.sort(Comparator.comparingInt(user -> user.getIndex()));

         for(User user : list) {
            usersArray.addPOJO(new User(user));
         }
         objectNode.set("output", usersArray);

         objectNode.put("timestamp", timestamp);

         output.add(objectNode);
     }
}

@Getter @Setter
class PrintTransactions extends Command {
    private HashMap<String, User> users;

    public PrintTransactions(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.email = command.getEmail();

        this.users = users;
    }
    public void execute(ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        User user = this.users.get(email);

        ArrayList<Command> transactions = user.getTransactions();
        ArrayNode outputArray = mapper.createArrayNode();

        for (Command transaction : transactions) {
            outputArray.addPOJO(transaction);
        }

        objectNode.set("output", outputArray);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);

    }

}




