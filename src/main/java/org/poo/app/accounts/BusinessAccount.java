package org.poo.app.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.Card;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.accounts.userTypes.BAccUser;
import org.poo.app.accounts.userTypes.Employee;
import org.poo.app.accounts.userTypes.Manager;
import org.poo.app.accounts.userTypes.Owner;
import org.poo.app.plans.ServicePlan;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter @Setter
public class BusinessAccount extends Account {
    private String ownerName;
    private String ownerEmail;
    private double spendingLimit;
    private double depositLimit ;
    private ArrayList<BAccUser> businessAssociate = new ArrayList<>();
    public BusinessAccount(final String ownerName, final String ownerEmail, final String IBAN,
                           final double balance, final String currency,
                           final String type, final ServicePlan servicePlan, User user) {
        super(IBAN, balance, currency, type, servicePlan, 0, user, ownerEmail);
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.addBusinessAssociate("owner", ownerEmail, user.getFirstName() + " " + user.getLastName());
        spendingLimit = ExchangeRateGraph.makeConversion("RON", currency, 500);
        depositLimit = ExchangeRateGraph.makeConversion("RON", currency, 500);

    }

    public void addBusinessAssociate(String role, String email, String username) {
        if(role.equals("owner"))
            businessAssociate.add(new Owner(email, username));
        if(role.equals("manager"))
            businessAssociate.add(new Manager(email, username));
        if(role.equals("employee"))
            businessAssociate.add(new Employee(email, username));
    }

    @Override
    public ArrayList<BAccUser> abc() {
        return businessAssociate;
    }

    private BAccUser getCurrentAssociate(String email) {
        for(BAccUser user : businessAssociate){
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }


    public void makePayment(final double amount, final double commission, String email, int timestamp) {
        BAccUser associate = getCurrentAssociate(email);
        if(associate == null) {
            throw new NotFoundException();
        }
        associate.makePayment(this, amount, commission, timestamp);
    }

    public void addFunds(final double amount, String email, int timestamp) {
        BAccUser associate = getCurrentAssociate(email);
        if(associate == null) {
            throw new NotFoundException();
        }
        associate.addFunds(this, amount, timestamp);
    }

    /**
     * delete card from account
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber, String email) {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.deleteCard(cardNumber, this);
    }

    public void setAliasCommand(final String alias, String email) {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setAlias(this, alias);
    }

    public void setMinBalanceCommand(final double minBalance, String email) {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setMinBalance(this, minBalance);
    }

    public void changeSpendingLimit(final double spendingLimit, String email) {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeSpendingLimit(this, spendingLimit);
    }

    public void changeDepositLimit(final double depositLimit, String email) {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeDepositLimit(this, depositLimit);
    }

}
