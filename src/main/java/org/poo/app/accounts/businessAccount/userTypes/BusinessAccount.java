package org.poo.app.accounts.businessAccount.userTypes;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.commerciatnTypes.Commerciant;
import org.poo.app.plans.ServicePlan;

import java.util.ArrayList;

@Getter @Setter
public final class BusinessAccount extends Account {
    private static final int DEFAULT_SPENDING_AND_DEPOSITING_LIMIT = 500;
    private String ownerName;
    private String ownerEmail;
    private double spendingLimit;
    private double depositLimit;
    private ArrayList<BAccUser> businessAssociates = new ArrayList<>();
    private ArrayList<CommerciantForBusiness> commerciantsForBusiness = new ArrayList<>();

    /**
     * constructor
     * @param ownerName
     * @param ownerEmail
     * @param IBAN
     * @param balance
     * @param currency
     * @param type
     * @param servicePlan
     * @param user
     */
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

    /**
     * add a business associate based on desired role
     *      action can be done by owner only
     * @param role
     * @param email
     * @param username
     */
    public void addBusinessAssociate(final String role, final String email, final String username) {
        for (BAccUser associate : businessAssociates) {
            if (associate.getEmail().equals(email)) {
                return;
            }
        }
        if (role.equals("owner")) {
            businessAssociates.add(new Owner(email, username));
        }
        if (role.equals("manager")) {
            businessAssociates.add(new Manager(email, username));
        }
        if (role.equals("employee")) {
            businessAssociates.add(new Employee(email, username));
        }
    }

    /**
     * returns the business associates list
     *      literall couldn t name it anything else idk why it didn t work
     * @return
     */
    @Override
    public ArrayList<BAccUser> abc() {
        return businessAssociates;
    }

    /**
     * get the current associate that tries to execute an action
     * Done by email
     * @param email
     * @return
     */
    public BAccUser getCurrentAssociate(final String email) {
        for (BAccUser user : businessAssociates) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    /**
     * make a payment
     * due to strategy design pattern different behaviour will occur
     * @param amount
     * @param commission
     * @param email
     * @param timestamp
     * @param commerciant
     * @throws NotFoundException
     */
    public void makePayment(final double amount, final double commission,
                            final String email, final int timestamp,
                            final Commerciant commerciant) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.makePayment(this, amount, commission, timestamp, commerciant);
    }

    /**
     * due to strategy design pattern different behaviour will occur
     * @param amount
     * @param email
     * @param timestamp
     * @throws NotFoundException
     */
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
     * due to strategy design pattern different behaviour will occur
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber, final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.deleteCard(cardNumber, this);
    }

    /**
     * set alias for account
     * due to strategy design pattern different behaviour will occur
     * @param alias
     * @param email
     * @throws NotFoundException
     */
    public void setAliasCommand(final String alias, final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setAlias(this, alias);
    }

    /**
     * set minimum balance for account
     * due to strategy design pattern different behaviour will occur
     * @param minBalance
     * @param email
     * @throws NotFoundException
     */
    public void setMinBalanceCommand(final double minBalance,
                                     final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.setMinBalance(this, minBalance);
    }

    /**
     * set spending limit for employees
     * due to strategy design pattern different behaviour will occur
     * @param newSpendingLimit
     * @param email
     * @throws NotFoundException
     */
    public void changeSpendingLimit(final double newSpendingLimit,
                                    final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeSpendingLimit(this, newSpendingLimit);
    }

    /**
     * set deposit limit for employees
     * due to strategy design pattern different behaviour will occur
     * @param newDepositLimit
     * @param email
     * @throws NotFoundException
     */
    public void changeDepositLimit(final double newDepositLimit,
                                   final String email) throws NotFoundException {
        BAccUser associate = getCurrentAssociate(email);
        if (associate == null) {
            throw new NotFoundException();
        }
        associate.changeDepositLimit(this, newDepositLimit);
    }

    /**
     * not a classic account so this returns null
     * @param currency
     * @return
     */
    public Account getClassicAccount(final String currency) {
        return null;
    }

    /**
     * get all managers related to this account
     * @return
     */
    public ArrayList<BAccUser> getManagers() {
        ArrayList<BAccUser> managers = new ArrayList<>();
        for (BAccUser associate : businessAssociates) {
            associate.addManagerToList(managers);
        }
        return managers;
    }

    /**
     * get all employees related to this account
     * @return
     */
    public ArrayList<BAccUser> getEmployees() {
        ArrayList<BAccUser> employees = new ArrayList<>();
        for (BAccUser associate : businessAssociates) {
            associate.addEmployeeToList(employees);
        }
        return employees;
    }

}
