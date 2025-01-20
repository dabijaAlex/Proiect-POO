package org.poo.app.accounts.userTypes;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Amounts {
    double val;
    int timestamp;

    /**
     * constructor for class that helps us match each payment of a user to a specific timestamp
     *      useful for the business account report
     * @param val
     * @param timestamp
     */
    public Amounts(double val, int timestamp) {
        this.val = val;
        this.timestamp = timestamp;
    }
}
