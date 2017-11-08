/*
 *  Copyright (c) 2017, Damiaan van der Kruk.
 *
 */

package com.github.dvdkruk.payslip.model;

public class FinancialInformation {
    final int salary;
    final int tax;
    final int superannuation;

    public FinancialInformation(
        final int salary,
        final int tax,
        final int superannuation) {
        this.salary = salary;
        this.tax = tax;
        this.superannuation = superannuation;
    }

    final int getSalary() {
        return this.salary;
    }

    final int getTax() {
        return this.tax;
    }

    final int getNetIncome() {
        return this.salary - this.tax;
    }

    public final int getSuperannuation() {
        return this.superannuation;
    }
}