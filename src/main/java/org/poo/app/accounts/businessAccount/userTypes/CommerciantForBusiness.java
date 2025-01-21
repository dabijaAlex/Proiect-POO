package org.poo.app.accounts.businessAccount.userTypes;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * this class is used to store which business associate made a payment
 *      to a commerciant and the amount that they payed
 */
@Getter @Setter
public class CommerciantForBusiness {
    private String name;
    private ArrayList<String> managers = new ArrayList<>();
    private ArrayList<String> employees = new ArrayList<>();
    private double totalReceived = 0;

    /**
     * constructor
     * @param name
     */
    public CommerciantForBusiness(final String name) {
        this.name = name;
    }

    /**
     * it s first time manager pays here so add him to list
     * @param manager
     */
    public void addManager(final String manager) {
        managers.add(manager);
    }

    /**
     * it s first time employee pays here so add him to list
     * @param employee
     */
    public void addEmployee(final String employee) {
        employees.add(employee);
    }

    /**
     * add amount spent in the latest transaction
     * @param amount
     */
    public void addAmount(final double amount) {
        totalReceived += amount;
    }

    /**
     * make deep copy of list for printing in business report
     * @return
     */
    public ArrayList<String> getManagersCopy() {
        return new ArrayList<>(managers);
    }

    /**
     * make deep copy for printing in business report
     * @return
     */
    public ArrayList<String> getEmployeesCopy() {
        return new ArrayList<>(employees);
    }
}
