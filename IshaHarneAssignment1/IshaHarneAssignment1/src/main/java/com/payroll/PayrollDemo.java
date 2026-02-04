package com.payroll;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstration of the payroll calculator system.
 * Creates 6+ employees covering all types and deduction combinations.
 */
public class PayrollDemo {

    public static void main(String[] args) {
        PayrollProcessor processor = new PayrollProcessor();

        // Create employees covering all types and deduction combinations
        List<Employee> employees = createSampleEmployees();

        System.out.println("=".repeat(60));
        System.out.println("       MONTHLY PAYROLL PROCESSING DEMO");
        System.out.println("=".repeat(60));
        System.out.println();

        // Process payroll for all employees
        List<PaySlip> paySlips = processor.processMonthlyPayroll(employees);

        // Print all pay slips
        for (PaySlip paySlip : paySlips) {
            System.out.println(paySlip);
        }

        // Print summary
        printSummary(paySlips);

        // Demonstrate individual calculations
        System.out.println("\n" + "=".repeat(60));
        System.out.println("       INDIVIDUAL CALCULATION EXAMPLES");
        System.out.println("=".repeat(60));

        // Example: Part-time employee with 80 hours
        Employee partTimer = employees.get(2);
        PaySlip customPaySlip = processor.generatePaySlip(partTimer, 80);
        System.out.println("\nPart-time employee with 80 hours (instead of max 120):");
        System.out.println(customPaySlip);

        // Example: Contractor with 15 days
        Employee contractor = employees.get(4);
        PaySlip contractorPaySlip = processor.generatePaySlip(contractor, 15);
        System.out.println("Contractor with 15 days (instead of default 22):");
        System.out.println(contractorPaySlip);
    }

    /**
     * Creates a sample list of employees covering all types and deduction combinations.
     */
    private static List<Employee> createSampleEmployees() {
        return Arrays.asList(
            // Employee 1: FULL_TIME with all deductions (health, retirement, union)
            new Employee("EMP001", "Alice Johnson", EmployeeType.FULL_TIME,
                    5500.00, true, true),

            // Employee 2: FULL_TIME with health insurance only (no retirement, no union)
            new Employee("EMP002", "Bob Smith", EmployeeType.FULL_TIME,
                    4200.00, false, false),

            // Employee 3: PART_TIME with retirement and union dues
            new Employee("EMP003", "Carol Davis", EmployeeType.PART_TIME,
                    25.00, true, true),

            // Employee 4: PART_TIME with no deductions
            new Employee("EMP004", "David Wilson", EmployeeType.PART_TIME,
                    30.00, false, false),

            // Employee 5: CONTRACTOR with retirement contribution
            new Employee("EMP005", "Eva Martinez", EmployeeType.CONTRACTOR,
                    350.00, false, true),

            // Employee 6: CONTRACTOR with union dues only
            new Employee("EMP006", "Frank Brown", EmployeeType.CONTRACTOR,
                    280.00, true, false),

            // Employee 7: FULL_TIME in lowest tax bracket
            new Employee("EMP007", "Grace Lee", EmployeeType.FULL_TIME,
                    950.00, false, false),

            // Employee 8: PART_TIME with all applicable deductions
            new Employee("EMP008", "Henry Taylor", EmployeeType.PART_TIME,
                    45.00, true, true)
        );
    }

    /**
     * Prints a summary of the payroll processing.
     */
    private static void printSummary(List<PaySlip> paySlips) {
        System.out.println("=".repeat(60));
        System.out.println("                 PAYROLL SUMMARY");
        System.out.println("=".repeat(60));

        double totalGross = 0;
        double totalTax = 0;
        double totalDeductions = 0;
        double totalNet = 0;

        System.out.printf("%-12s %-15s %12s %10s %12s%n",
                "ID", "Name", "Gross", "Tax", "Net Pay");
        System.out.println("-".repeat(60));

        for (PaySlip slip : paySlips) {
            totalGross += slip.getGrossPay();
            totalTax += slip.getTaxAmount();
            totalDeductions += slip.getTotalDeductions();
            totalNet += slip.getNetPay();

            System.out.printf("%-12s %-15s $%,10.2f $%,8.2f $%,10.2f%n",
                    slip.getEmployee().getId(),
                    truncate(slip.getEmployee().getName(), 15),
                    slip.getGrossPay(),
                    slip.getTaxAmount(),
                    slip.getNetPay());
        }

        System.out.println("-".repeat(60));
        System.out.printf("%-28s $%,10.2f $%,8.2f $%,10.2f%n",
                "TOTALS:", totalGross, totalTax, totalNet);
        System.out.printf("%-28s $%,10.2f%n", "Total Deductions:", totalDeductions);
        System.out.println("=".repeat(60));
    }

    private static String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 2) + "..";
    }
}
