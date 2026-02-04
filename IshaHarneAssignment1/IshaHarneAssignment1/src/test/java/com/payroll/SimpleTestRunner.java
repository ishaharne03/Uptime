package com.payroll;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Simple test runner that doesn't require JUnit.
 * Verifies all payroll calculations work correctly.
 */
public class SimpleTestRunner {

    private static int passed = 0;
    private static int failed = 0;
    private static PayrollProcessor processor = new PayrollProcessor();

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("         PAYROLL CALCULATOR TEST SUITE");
        System.out.println("=".repeat(60));
        System.out.println();

        // Gross Pay Tests
        runGrossPayTests();

        // Tax Calculation Tests
        runTaxTests();

        // Deduction Tests
        runDeductionTests();

        // PaySlip Tests
        runPaySlipTests();

        // Integration Tests
        runIntegrationTests();

        // Print summary
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.printf("TEST RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println("=".repeat(60));

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void runGrossPayTests() {
        System.out.println("--- Gross Pay Tests ---");

        // FULL_TIME gets fixed salary
        Employee ft = new Employee("E001", "Test", EmployeeType.FULL_TIME, 5000.00, false, false);
        assertEqual("FULL_TIME fixed salary", 5000.00, processor.calculateGrossPay(ft, 0));
        assertEqual("FULL_TIME ignores hours", 5000.00, processor.calculateGrossPay(ft, 160));

        // PART_TIME hourly calculation
        Employee pt = new Employee("E002", "Test", EmployeeType.PART_TIME, 25.00, false, false);
        assertEqual("PART_TIME 100 hours", 2500.00, processor.calculateGrossPay(pt, 100));
        assertEqual("PART_TIME capped at 120", 3000.00, processor.calculateGrossPay(pt, 150));
        assertEqual("PART_TIME zero hours", 0.00, processor.calculateGrossPay(pt, 0));

        // CONTRACTOR daily calculation
        Employee ct = new Employee("E003", "Test", EmployeeType.CONTRACTOR, 300.00, false, false);
        assertEqual("CONTRACTOR 22 days", 6600.00, processor.calculateGrossPay(ct, 22));
        assertEqual("CONTRACTOR zero days", 0.00, processor.calculateGrossPay(ct, 0));

        System.out.println();
    }

    private static void runTaxTests() {
        System.out.println("--- Tax Calculation Tests ---");

        // Tax bracket tests
        assertEqual("No tax on $0", 0.00, processor.calculateTax(0.00));
        assertEqual("No tax on $500", 0.00, processor.calculateTax(500.00));
        assertEqual("No tax on $1000", 0.00, processor.calculateTax(1000.00));

        // 10% bracket ($1001-$3000)
        assertEqual("Tax on $1001", 0.10, processor.calculateTax(1001.00));
        assertEqual("Tax on $2000", 100.00, processor.calculateTax(2000.00));
        assertEqual("Tax on $3000", 200.00, processor.calculateTax(3000.00));

        // 20% bracket ($3001-$5000)
        assertEqual("Tax on $4000", 400.00, processor.calculateTax(4000.00));
        assertEqual("Tax on $5000", 600.00, processor.calculateTax(5000.00));

        // 30% bracket (above $5000)
        assertEqual("Tax on $6000", 900.00, processor.calculateTax(6000.00));
        assertEqual("Tax on $10000", 2100.00, processor.calculateTax(10000.00));
        assertEqual("Tax on $100000", 29100.00, processor.calculateTax(100000.00));

        System.out.println();
    }

    private static void runDeductionTests() {
        System.out.println("--- Deduction Tests ---");

        // Health insurance only for FULL_TIME
        Employee ftNoFlags = new Employee("E001", "Test", EmployeeType.FULL_TIME, 5000.00, false, false);
        Map<String, Double> ftDed = processor.calculateDeductions(ftNoFlags, 5000.00);
        assertTrue("FULL_TIME has health insurance", ftDed.containsKey("Health Insurance"));
        assertEqual("Health insurance is $150", 150.00, ftDed.get("Health Insurance"));

        Employee ptNoFlags = new Employee("E002", "Test", EmployeeType.PART_TIME, 25.00, false, false);
        Map<String, Double> ptDed = processor.calculateDeductions(ptNoFlags, 3000.00);
        assertTrue("PART_TIME no health insurance", !ptDed.containsKey("Health Insurance"));

        Employee ctNoFlags = new Employee("E003", "Test", EmployeeType.CONTRACTOR, 300.00, false, false);
        Map<String, Double> ctDed = processor.calculateDeductions(ctNoFlags, 6600.00);
        assertTrue("CONTRACTOR no health insurance", !ctDed.containsKey("Health Insurance"));

        // Retirement deduction
        Employee withRetirement = new Employee("E004", "Test", EmployeeType.FULL_TIME, 5000.00, false, true);
        Map<String, Double> retDed = processor.calculateDeductions(withRetirement, 5000.00);
        assertTrue("Has retirement deduction", retDed.containsKey("Retirement (5%)"));
        assertEqual("Retirement is 5%", 250.00, retDed.get("Retirement (5%)"));

        Employee noRetirement = new Employee("E005", "Test", EmployeeType.FULL_TIME, 5000.00, false, false);
        Map<String, Double> noRetDed = processor.calculateDeductions(noRetirement, 5000.00);
        assertTrue("No retirement when not opted in", !noRetDed.containsKey("Retirement (5%)"));

        // Union dues
        Employee unionMember = new Employee("E006", "Test", EmployeeType.PART_TIME, 25.00, true, false);
        Map<String, Double> unionDed = processor.calculateDeductions(unionMember, 3000.00);
        assertTrue("Union member has dues", unionDed.containsKey("Union Dues"));
        assertEqual("Union dues is $50", 50.00, unionDed.get("Union Dues"));

        Employee nonUnion = new Employee("E007", "Test", EmployeeType.PART_TIME, 25.00, false, false);
        Map<String, Double> nonUnionDed = processor.calculateDeductions(nonUnion, 3000.00);
        assertTrue("Non-union no dues", !nonUnionDed.containsKey("Union Dues"));

        // All deductions for FULL_TIME with all flags
        Employee allFlags = new Employee("E008", "Test", EmployeeType.FULL_TIME, 5000.00, true, true);
        Map<String, Double> allDed = processor.calculateDeductions(allFlags, 5000.00);
        assertEqual("FULL_TIME all deductions count", 3, allDed.size());

        System.out.println();
    }

    private static void runPaySlipTests() {
        System.out.println("--- PaySlip Tests ---");

        Employee emp = new Employee("E001", "Test", EmployeeType.FULL_TIME, 5000.00, false, false);
        PaySlip slip = processor.generatePaySlip(emp, 0);

        assertEqual("PaySlip gross pay", 5000.00, slip.getGrossPay());
        assertEqual("PaySlip tax amount", 600.00, slip.getTaxAmount());
        assertEqual("PaySlip employee ID", "E001", slip.getEmployee().getId());

        // Net pay with all deductions
        Employee allFlags = new Employee("E002", "Test", EmployeeType.FULL_TIME, 5000.00, true, true);
        PaySlip allSlip = processor.generatePaySlip(allFlags, 0);
        // Gross: $5000, Tax: $600, Deductions: $150 + $250 + $50 = $450
        // Net: $5000 - $600 - $450 = $3950
        assertEqual("PaySlip net pay with deductions", 3950.00, allSlip.getNetPay());

        System.out.println();
    }

    private static void runIntegrationTests() {
        System.out.println("--- Integration Tests ---");

        // Full calculation for FULL_TIME employee
        Employee alice = new Employee("EMP001", "Alice", EmployeeType.FULL_TIME, 5500.00, true, true);
        PaySlip aliceSlip = processor.generatePaySlip(alice, 0);
        assertEqual("Alice gross", 5500.00, aliceSlip.getGrossPay());
        assertEqual("Alice tax", 750.00, aliceSlip.getTaxAmount());
        assertEqual("Alice total deductions", 475.00, aliceSlip.getTotalDeductions());
        assertEqual("Alice net", 4275.00, aliceSlip.getNetPay());

        // Full calculation for PART_TIME employee
        Employee bob = new Employee("EMP002", "Bob", EmployeeType.PART_TIME, 25.00, true, true);
        PaySlip bobSlip = processor.generatePaySlip(bob, 100);
        assertEqual("Bob gross (100hrs)", 2500.00, bobSlip.getGrossPay());
        assertEqual("Bob tax", 150.00, bobSlip.getTaxAmount());
        // Deductions: $125 (5% of 2500) + $50 (union) = $175
        assertEqual("Bob total deductions", 175.00, bobSlip.getTotalDeductions());
        assertEqual("Bob net", 2175.00, bobSlip.getNetPay());

        // Full calculation for CONTRACTOR
        Employee carol = new Employee("EMP003", "Carol", EmployeeType.CONTRACTOR, 350.00, false, true);
        PaySlip carolSlip = processor.generatePaySlip(carol, 20);
        assertEqual("Carol gross (20 days)", 7000.00, carolSlip.getGrossPay());
        assertEqual("Carol tax", 1200.00, carolSlip.getTaxAmount());
        // Deductions: $350 (5% of 7000)
        assertEqual("Carol total deductions", 350.00, carolSlip.getTotalDeductions());
        assertEqual("Carol net", 5450.00, carolSlip.getNetPay());

        // Monthly payroll processing
        List<Employee> employees = Arrays.asList(
                new Employee("E001", "FT", EmployeeType.FULL_TIME, 5000.00, false, false),
                new Employee("E002", "PT", EmployeeType.PART_TIME, 25.00, false, false),
                new Employee("E003", "CT", EmployeeType.CONTRACTOR, 300.00, false, false)
        );
        List<PaySlip> slips = processor.processMonthlyPayroll(employees);
        assertEqual("Monthly payroll count", 3, slips.size());
        assertEqual("FT monthly gross", 5000.00, slips.get(0).getGrossPay());
        assertEqual("PT monthly gross (120hrs)", 3000.00, slips.get(1).getGrossPay());
        assertEqual("CT monthly gross (22 days)", 6600.00, slips.get(2).getGrossPay());

        System.out.println();
    }

    private static void assertEqual(String testName, double expected, double actual) {
        if (Math.abs(expected - actual) < 0.01) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s - Expected: %.2f, Got: %.2f%n", testName, expected, actual);
            failed++;
        }
    }

    private static void assertEqual(String testName, int expected, int actual) {
        if (expected == actual) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s - Expected: %d, Got: %d%n", testName, expected, actual);
            failed++;
        }
    }

    private static void assertEqual(String testName, String expected, String actual) {
        if (expected.equals(actual)) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s - Expected: %s, Got: %s%n", testName, expected, actual);
            failed++;
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s - Condition was false%n", testName);
            failed++;
        }
    }
}
