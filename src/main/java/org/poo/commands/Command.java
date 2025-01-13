package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.app.AccountNotFound;
import org.poo.app.UserNotFound;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;

import java.util.HashMap;

public class Command {
    @JsonIgnore
    protected String cmdName = null;
    @JsonIgnore
    protected int timestampTheSecond;

    /**
     * this method is overwritten in every class that extends command
     * @param output
     */
    public void execute(final ArrayNode output) {
    }


    /**
     * a way to easily get the user reference and throw exception if it could not be found
     * @param users
     * @param key
     * @return
     * @throws NotFoundException
     */
    public User getUserReference(final HashMap<String, User> users,
                                 final String key) throws NotFoundException {
        if(key == null)
            throw new NotFoundException();
        User user = users.get(key);
        if (user == null) {
            if(key.contains("@"))
                throw new UserNotFound();
            if(key.contains("RO"))
                throw new AccountNotFound();
            throw new NotFoundException();
        }
        return user;
    }

    /**
     * a way to easily get the account reference and throw exception if it could not be found
     * @param users
     * @param key
     * @return
     * @throws NotFoundException
     */
    public Account getAccountReference(final HashMap<String, User> users,
                                       final String key) throws NotFoundException {
        if(key == null)
            throw new NotFoundException();
        User user = getUserReference(users, key);
        if (user == null) {
            throw new NotFoundException();
        }
        Account cont = user.getAccount(key);
        if (cont == null) {
            throw new NotFoundException();
        }
        return cont;
    }

    /**
     *
     * @return
     */
    public String getCmdName() {
        return cmdName;
    }
    public int getTimestampTheSecond() {
        return timestampTheSecond;
    }
}
