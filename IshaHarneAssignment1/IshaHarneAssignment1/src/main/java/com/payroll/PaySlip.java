package com.payroll;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a pay slip for an employee containing all payment details.
 * Includes gross pay, tax amount, itemized deductions, and net pay.
 */
public class PaySlip {
    private final Employee employee;
    private final double grossPay;
    private final double taxAmount;
    private final Map<String, Double> deductions;
    private final double netPay;

    public PaySlip(Employee employee, double grossPay, double taxAmount,
                   Map<String, Double> deductions, double netPay) {
        this.employee = employee;
        this.grossPay = grossPay;
        this.taxAmount = taxAmount;
        this.deductions = new LinkedHashMap<>(deductions);
        this.netPay = netPay;
    }

    public Employee getEmployee() {
        return employee;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public Map<String, Double> getDeductions() {
        return Collections.unmodifiableMap(deductions);
    }

    public double getNetPay() {
        return netPay;
    }

    public double getTotalDeductions() {
        return deductions.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================\n");
        sb.append("           PAY SLIP\n");
        sb.append("=====================================\n");
        sb.append(String.format("Employee ID:   %s\n", employee.getId()));
        sb.append(String.format("Employee Name: %s\n", employee.getName()));
        sb.append(String.format("Employee Type: %s\n", employee.getEmployeeType()));
        sb.append("-------------------------------------\n");
        sb.append(String.format("Gross Pay:     $%,10.2f\n", grossPay));
        sb.append(String.format("Tax:           $%,10.2f\n", taxAmount));
        sb.append("-------------------------------------\n");
        sb.append("Deductions:\n");
        if (deductions.isEmpty()) {
            sb.append("  (None)\n");
        } else {
            for (Map.Entry<String, Double> entry : deductions.entrySet()) {
                sb.append(String.format("  %-18s $%,8.2f\n", entry.getKey() + ":", entry.getValue()));
            }
        }
        sb.append(String.format("  %-18s $%,8.2f\n", "Total Deductions:", getTotalDeductions()));
        sb.append("-------------------------------------\n");
        sb.append(String.format("NET PAY:       $%,10.2f\n", netPay));
        sb.append("=====================================\n");
        return sb.toString();
    }
}
