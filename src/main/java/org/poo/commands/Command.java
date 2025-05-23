package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.app.notFoundExceptions.AccountNotFoundException;
import org.poo.app.notFoundExceptions.UserNotFoundException;
import org.poo.app.accounts.Account;
import org.poo.app.notFoundExceptions.NotFoundException;
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
        if (key == null) {
            throw new NotFoundException();
        }
        User user = users.get(key);
        if (user == null) {
            if (key.contains("@")) {
                throw new UserNotFoundException();
            }
            if (key.contains("RO")) {
                throw new AccountNotFoundException();
            }
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
        if (key == null) {
            throw new NotFoundException();
        }
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
     * get command name
     * @return
     */
    public String getCmdName() {
        return cmdName;
    }

    /**
     * get the timestamp of the command
     * I should have put it from the beginning as a field but it is what it is now
     * @return
     */
    public int getTimestampTheSecond() {
        return timestampTheSecond;
    }
}
