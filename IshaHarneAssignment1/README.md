# Employee Payroll Calculator

A Java-based payroll calculator that computes monthly salary for employees based on their type and applicable deductions.

## Project Overview

This system calculates payroll for three types of employees with progressive tax brackets and various deductions. It demonstrates object-oriented design, arithmetic calculations with precision, conditional business logic, and enum/constant usage.

## Project Structure

```
D:\IshaHarneAssignments\IshaHarneAssignment1
├── README.md
├── pom.xml                                    # Maven config (optional)
├── out/                                       # Compiled classes
└── src/
    ├── main/java/com/payroll/
    │   ├── EmployeeType.java                  # Enum: FULL_TIME, PART_TIME, CONTRACTOR
    │   ├── Employee.java                      # Employee data class
    │   ├── PaySlip.java                       # Pay slip with calculations
    │   ├── PayrollProcessor.java              # Core business logic
    │   └── PayrollDemo.java                   # Demo with 8 employees
    └── test/java/com/payroll/
        └── SimpleTestRunner.java              # 49 unit tests
```

## Requirements

- Java JDK 17 or higher

## How to Run (Without Maven)

### 1. Navigate to Project Directory

```bash
cd "/IshaHarneAssignment1"
```

### 2. Compile the Project

```bash
javac -d out src/main/java/com/payroll/*.java src/test/java/com/payroll/SimpleTestRunner.java
```

### 3. Run the Demo

```bash
java -cp out com.payroll.PayrollDemo
```

### 4. Run the Tests

```bash
java -cp out com.payroll.SimpleTestRunner
```

### 5. Run Everything (Single Command)

```bash
cd "IshaHarneAssignment1" && javac -d out src/main/java/com/payroll/*.java src/test/java/com/payroll/SimpleTestRunner.java && java -cp out com.payroll.SimpleTestRunner && java -cp out com.payroll.PayrollDemo
```

## Requirements Fulfillment

### Employee Types

| Type | Calculation | Implementation |
|------|-------------|----------------|
| FULL_TIME | Fixed monthly salary | `PayrollProcessor.java:46-48` |
| PART_TIME | Hourly rate x hours (max 120/month) | `PayrollProcessor.java:50-53` |
| CONTRACTOR | Daily rate x days worked | `PayrollProcessor.java:55-57` |

### Tax Brackets (Progressive)

| Bracket | Rate | Implementation |
|---------|------|----------------|
| $0 - $1,000 | 0% | `PayrollProcessor.java:81-84` |
| $1,001 - $3,000 | 10% | `PayrollProcessor.java:87-94` |
| $3,001 - $5,000 | 20% | `PayrollProcessor.java:96-103` |
| Above $5,000 | 30% | `PayrollProcessor.java:105-106` |

### Deductions

| Deduction | Amount | Condition | Implementation |
|-----------|--------|-----------|----------------|
| Health Insurance | $150 flat | FULL_TIME only | `PayrollProcessor.java:125-128` |
| Retirement | 5% of gross | Optional flag | `PayrollProcessor.java:130-134` |
| Union Dues | $50 flat | Union members | `PayrollProcessor.java:136-139` |

### Required Classes

| Class | Fields | Status |
|-------|--------|--------|
| Employee | id, name, employeeType, payRate, isUnionMember, hasRetirement | Implemented |
| PaySlip | employee, grossPay, taxAmount, deductions (Map), netPay | Implemented |
| PayrollProcessor | All required methods | Implemented |

### PayrollProcessor Methods

| Method | Signature | Status |
|--------|-----------|--------|
| calculateGrossPay | `(Employee, double hoursOrDays)` | Implemented |
| calculateTax | `(double grossPay)` | Implemented |
| calculateDeductions | `(Employee, double grossPay)` | Implemented |
| generatePaySlip | `(Employee, double hoursOrDays)` | Implemented |
| processMonthlyPayroll | `(List<Employee>)` | Implemented |

### Currency Precision

All currency values are rounded to 2 decimal places using `BigDecimal` with `RoundingMode.HALF_UP`.

### Demo Coverage (8 Employees)

| Employee | Type | Health | Retirement | Union | Purpose |
|----------|------|--------|------------|-------|---------|
| Alice Johnson | FULL_TIME | Yes | Yes | Yes | All deductions |
| Bob Smith | FULL_TIME | Yes | No | No | Health only |
| Carol Davis | PART_TIME | No | Yes | Yes | Retirement + Union |
| David Wilson | PART_TIME | No | No | No | No deductions |
| Eva Martinez | CONTRACTOR | No | Yes | No | Retirement only |
| Frank Brown | CONTRACTOR | No | No | Yes | Union only |
| Grace Lee | FULL_TIME | Yes | No | No | Lowest tax bracket ($0 tax) |
| Henry Taylor | PART_TIME | No | Yes | Yes | Higher tax bracket |

## Test Coverage

The project includes 49 unit tests covering:

- **Gross Pay Tests** (7 tests): All employee types, hour capping, edge cases
- **Tax Calculation Tests** (11 tests): All tax brackets, boundary values
- **Deduction Tests** (11 tests): Health insurance, retirement, union dues
- **PaySlip Tests** (4 tests): Correct field values, net pay calculation
- **Integration Tests** (16 tests): End-to-end calculations for all employee types

## Sample Output

### Pay Slip
```
=====================================
           PAY SLIP
=====================================
Employee ID:   EMP001
Employee Name: Alice Johnson
Employee Type: FULL_TIME
-------------------------------------
Gross Pay:     $  5,500.00
Tax:           $    750.00
-------------------------------------
Deductions:
  Health Insurance:  $  150.00
  Retirement (5%):   $  275.00
  Union Dues:        $   50.00
  Total Deductions:  $  475.00
-------------------------------------
NET PAY:       $  4,275.00
=====================================
```

### Test Results
```
============================================================
TEST RESULTS: 49 passed, 0 failed
============================================================
```

## Technical Details

- **Language**: Java 17+
- **Build**: javac (no external build tool required)
- **Testing**: Custom test runner (no JUnit dependency required)
- **Precision**: BigDecimal for currency calculations

## References

- [Java BigDecimal Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html)
- [Java Enum Types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html)
- [Java Collections Framework](https://docs.oracle.com/javase/tutorial/collections/index.html)
- [Progressive Tax Calculation](https://en.wikipedia.org/wiki/Progressive_tax)


