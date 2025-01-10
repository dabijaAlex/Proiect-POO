package org.poo.app.accounts.userTypes;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Amounts {
    double val;
    int timestamp;
    public Amounts(double val, int timestamp) {
        this.val = val;
        this.timestamp = timestamp;
    }
}
