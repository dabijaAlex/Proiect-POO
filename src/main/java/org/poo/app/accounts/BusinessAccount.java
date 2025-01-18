package org.poo.app.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.accounts.userTypes.*;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.plans.ServicePlan;

import java.util.ArrayList;

@Getter @Setter
public final class BusinessAccount extends Account {
    private static final int DEFAULT_SPENDING_AND_DEPOSITING_LIMIT = 500;
    private String ownerName;
    private String ownerEmail;
    private double spendingLimit;
    private double depositLimit;
    private ArrayList<BAccUser> businessAssociate = new ArrayList<>();
    private ArrayList<CommerciantForBusiness> commerciantsForBusiness = new ArrayList<>();
    public BusinessAccount(final String ownerName, final String ownerEmail, final String IBAN,
                           final double balance, final String currency,
                           final String type, final ServicePlan servicePlan, final User user) {
        super(IBAN, balance, currency, type, 0, user, ownerEmail);
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.addBusinessAssociate("owner", ownerEmail, user.getFirstName()
                + " " + user.getLastName());
        spendingLimit = ExchangeRateGraph.makeConversion("RON",
                currency, DEFAULT_SPENDING_AND_DEPOSITING_LIMIT);
        depositLimit = ExchangeRateGraph.makeConversion("RON",
                currency, DEFAULT_SPENDING_AND_DEPOSITING_LIMIT);

    }

    public void addBusinessAssociate(final String role, final String email, final String username) {
        for (BAccUser associate : businessAssociate) {
            if (associate.getEmail().equals(email)) {
                return;
            }
        }
        if (role.equals("owner")) {
            businessAssociate.add(new Owner(email, username));
        }
        if (role.equals("manager")) {
            businessAssociate.add(new Manager(email, username));
        }
        if (role.equals("employee")) {
            businessAssociate.add(new Employee(email, username));
        }
    }

    @Override
    public ArrayList<BAccUser> abc() {
        return businessAssociate;
    }

    public BAccUser getCurrentAssociate(final String email) {
        for (BAccUser user : businessAssociate) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }



    public void makePayment(final double amount, final double commission,
                            final String email, final int timestamp,
                            final Commerciant commerciant) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.makePayment(this, amount, commission, timestamp, commerciant);
    }

    public void addFunds(final double amount, final String email,
                         final int timestamp) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.addFunds(this, amount, timestamp);
    }

    /**
     * delete card from account
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber, final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.deleteCard(cardNumber, this);
    }

    public void setAliasCommand(final String alias, final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setAlias(this, alias);
    }

    public void setMinBalanceCommand(final double minBalance,
                                     final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setMinBalance(this, minBalance);
    }

    public void changeSpendingLimit(final double newSpendingLimit,
                                    final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeSpendingLimit(this, newSpendingLimit);
    }

    public void changeDepositLimit(final double newDepositLimit,
                                   final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeDepositLimit(this, newDepositLimit);
    }

    public Account getClassicAccount(final String currency) {
        return null;
    }

}
