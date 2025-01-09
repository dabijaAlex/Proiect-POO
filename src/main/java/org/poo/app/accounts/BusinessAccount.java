package org.poo.app.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.plans.ServicePlan;

@Getter @Setter
public class BusinessAccount extends Account {
    private String ownerName;
    private String ownerEmail;
    public BusinessAccount(final String ownerName, final String ownerEmail, final String IBAN,
                           final double balance, final String currency,
                           final String type, final ServicePlan servicePlan) {
        super(IBAN, balance, currency, type, servicePlan, 0);
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
    }
}
