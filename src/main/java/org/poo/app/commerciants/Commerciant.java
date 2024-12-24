package org.poo.app.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

@Getter @Setter
public class Commerciant {
    protected String commerciant;
    protected int id;
    protected String account;
    protected String type;
    protected String cashbackStrategy;

    public Commerciant(CommerciantInput input) {
        commerciant = input.getCommerciant();
        id = input.getId();
        account = input.getAccount();
        type = input.getType();
        cashbackStrategy = input.getCashbackStrategy();
    }
}
