package com.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Processes payroll calculations for employees.
 * Handles gross pay, taxes, and deductions based on employee type and benefits.
 */
public class PayrollProcessor {

    // Tax bracket thresholds
    private static final double TAX_BRACKET_1 = 1000.0;
    private static final double TAX_BRACKET_2 = 3000.0;
    private static final double TAX_BRACKET_3 = 5000.0;

    // Tax rates for each bracket
    private static final double TAX_RATE_1 = 0.0;   // 0% for first $1000
    private static final double TAX_RATE_2 = 0.10;  // 10% for $1001-$3000
    private static final double TAX_RATE_3 = 0.20;  // 20% for $3001-$5000
    private static final double TAX_RATE_4 = 0.30;  // 30% above $5000

    // Deduction amounts
    private static final double HEALTH_INSURANCE = 150.0;
    private static final double RETIREMENT_RATE = 0.05;  // 5% of gross
    private static final double UNION_DUES = 50.0;

    // Part-time max hours
    private static final double MAX_PART_TIME_HOURS = 120.0;

    /**
     * Calculates gross pay based on employee type and hours/days worked.
     *
     * @param employee    the employee
     * @param hoursOrDays hours worked (PART_TIME) or days worked (CONTRACTOR), ignored for FULL_TIME
     * @return the gross pay rounded to 2 decimal places
     */
    public double calculateGrossPay(Employee employee, double hoursOrDays) {
        double grossPay;

        switch (employee.getEmployeeType()) {
            case FULL_TIME:
                // Fixed monthly salary
                grossPay = employee.getPayRate();
                break;
            case PART_TIME:
                // Hourly rate × hours worked (max 120 hours/month)
                double cappedHours = Math.min(hoursOrDays, MAX_PART_TIME_HOURS);
                grossPay = employee.getPayRate() * cappedHours;
                break;
            case CONTRACTOR:
                // Daily rate × days worked
                grossPay = employee.getPayRate() * hoursOrDays;
                break;
            default:
                throw new IllegalArgumentException("Unknown employee type: " + employee.getEmployeeType());
        }

        return round(grossPay);
    }

    /**
     * Calculates tax using progressive tax brackets.
     * Tax brackets:
     * - 0% for first $1000
     * - 10% for $1001-$3000
     * - 20% for $3001-$5000
     * - 30% above $5000
     *
     * @param grossPay the gross pay amount
     * @return the tax amount rounded to 2 decimal places
     */
    public double calculateTax(double grossPay) {
        double tax = 0.0;
        double remaining = grossPay;

        // First $1000 - 0% tax
        if (remaining <= TAX_BRACKET_1) {
            return round(tax);
        }
        remaining -= TAX_BRACKET_1;

        // $1001 - $3000 - 10% tax
        double bracket2Amount = TAX_BRACKET_2 - TAX_BRACKET_1; // $2000
        if (remaining <= bracket2Amount) {
            tax += remaining * TAX_RATE_2;
            return round(tax);
        }
        tax += bracket2Amount * TAX_RATE_2;
        remaining -= bracket2Amount;

        // $3001 - $5000 - 20% tax
        double bracket3Amount = TAX_BRACKET_3 - TAX_BRACKET_2; // $2000
        if (remaining <= bracket3Amount) {
            tax += remaining * TAX_RATE_3;
            return round(tax);
        }
        tax += bracket3Amount * TAX_RATE_3;
        remaining -= bracket3Amount;

        // Above $5000 - 30% tax
        tax += remaining * TAX_RATE_4;

        return round(tax);
    }

    /**
     * Calculates applicable deductions for an employee.
     * Deductions include:
     * - Health insurance: $150 flat (FULL_TIME only)
     * - Retirement: 5% of gross (if hasRetirement flag is true)
     * - Union dues: $50 flat (if isUnionMember flag is true)
     *
     * @param employee the employee
     * @param grossPay the gross pay amount
     * @return a map of deduction names to amounts
     */
    public Map<String, Double> calculateDeductions(Employee employee, double grossPay) {
        Map<String, Double> deductions = new LinkedHashMap<>();

        // Health insurance - only for FULL_TIME employees
        if (employee.getEmployeeType() == EmployeeType.FULL_TIME) {
            deductions.put("Health Insurance", HEALTH_INSURANCE);
        }

        // Retirement contribution - 5% of gross if employee has opted in
        if (employee.hasRetirement()) {
            double retirementAmount = round(grossPay * RETIREMENT_RATE);
            deductions.put("Retirement (5%)", retirementAmount);
        }

        // Union dues - $50 if employee is a union member
        if (employee.isUnionMember()) {
            deductions.put("Union Dues", UNION_DUES);
        }

        return deductions;
    }

    /**
     * Generates a complete pay slip for an employee.
     *
     * @param employee    the employee
     * @param hoursOrDays hours worked (PART_TIME) or days worked (CONTRACTOR), ignored for FULL_TIME
     * @return the generated PaySlip
     */
    public PaySlip generatePaySlip(Employee employee, double hoursOrDays) {
        double grossPay = calculateGrossPay(employee, hoursOrDays);
        double taxAmount = calculateTax(grossPay);
        Map<String, Double> deductions = calculateDeductions(employee, grossPay);

        double totalDeductions = deductions.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double netPay = round(grossPay - taxAmount - totalDeductions);

        return new PaySlip(employee, grossPay, taxAmount, deductions, netPay);
    }

    /**
     * Processes monthly payroll for a list of employees.
     * Uses default hours/days: 0 for FULL_TIME, 120 for PART_TIME, 22 for CONTRACTOR.
     *
     * @param employeeList the list of employees
     * @return a list of PaySlips for all employees
     */
    public List<PaySlip> processMonthlyPayroll(List<Employee> employeeList) {
        List<PaySlip> paySlips = new ArrayList<>();

        for (Employee employee : employeeList) {
            double hoursOrDays = getDefaultHoursOrDays(employee.getEmployeeType());
            PaySlip paySlip = generatePaySlip(employee, hoursOrDays);
            paySlips.add(paySlip);
        }

        return paySlips;
    }

    /**
     * Gets default hours/days based on employee type.
     * FULL_TIME: 0 (uses fixed salary)
     * PART_TIME: 120 (max hours)
     * CONTRACTOR: 22 (typical work days in a month)
     */
    private double getDefaultHoursOrDays(EmployeeType type) {
        switch (type) {
            case FULL_TIME:
                return 0;
            case PART_TIME:
                return MAX_PART_TIME_HOURS;
            case CONTRACTOR:
                return 22;
            default:
                return 0;
        }
    }

    /**
     * Rounds a value to 2 decimal places using HALF_UP rounding.
     */
    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
