package com.payroll;

/**
 * Represents an employee in the payroll system.
 * Contains employee information and pay-related flags.
 */
public class Employee {
    private final String id;
    private final String name;
    private final EmployeeType employeeType;
    private final double payRate;
    private final boolean isUnionMember;
    private final boolean hasRetirement;

    public Employee(String id, String name, EmployeeType employeeType,
                    double payRate, boolean isUnionMember, boolean hasRetirement) {
        this.id = id;
        this.name = name;
        this.employeeType = employeeType;
        this.payRate = payRate;
        this.isUnionMember = isUnionMember;
        this.hasRetirement = hasRetirement;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public double getPayRate() {
        return payRate;
    }

    public boolean isUnionMember() {
        return isUnionMember;
    }

    public boolean hasRetirement() {
        return hasRetirement;
    }

    @Override
    public String toString() {
        return String.format("Employee[id=%s, name=%s, type=%s, payRate=%.2f, union=%b, retirement=%b]",
                id, name, employeeType, payRate, isUnionMember, hasRetirement);
    }
}
