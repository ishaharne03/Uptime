package com.payroll;

/**
 * Enum representing the different types of employees in the payroll system.
 * Each type has different pay calculation rules.
 */
public enum EmployeeType {
    FULL_TIME,    // Fixed monthly salary
    PART_TIME,    // Hourly rate × hours worked (max 120 hours/month)
    CONTRACTOR    // Daily rate × days worked
}
