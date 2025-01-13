package org.poo.app.accounts.userTypes;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class CommerciantForBusiness {
    private String name;
    private ArrayList<String> managers = new ArrayList<>();
    private ArrayList<String> employees = new ArrayList<>();
    private double totalReceived = 0;

    public CommerciantForBusiness(String name) {
        this.name = name;
    }

    public void addManager(String manager){
        managers.add(manager);
    }
    public void addEmployee(String employee){
        employees.add(employee);
    }
    public void addAmount(double amount){
        totalReceived += amount;
    }

    public ArrayList<String> getManagersCopy(){
        return new ArrayList<>(managers);
    }
    public ArrayList<String> getEmployeesCopy(){
        return new ArrayList<>(employees);
    }
}
