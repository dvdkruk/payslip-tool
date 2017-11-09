/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

/**
 * Contains financial information for a month.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class FinancialInformation {

    /**
     * Monthly salary.
     */
    private final int salary;

    /**
     * Monthly income tax.
     */
    private final int tax;

    /**
     * Monthly superann sum.
     */
    private final int superann;

    /**
     * Financial information constructor.
     *
     * @param salary Monthly salary.
     * @param tax Monthly income tax.
     * @param superannuation Monthly superann sum.
     */
    public FinancialInformation(
        final int salary,
        final int tax,
        final int superannuation) {
        this.salary = salary;
        this.tax = tax;
        this.superann = superannuation;
    }

    /**
     * Monthly salary.
     *
     * @return Monthly salary.
     */
    final int getSalary() {
        return this.salary;
    }

    /**
     * Monthly income tax.
     *
     * @return Monthly income tax.
     */
    final int getTax() {
        return this.tax;
    }

    /**
     * Monthly net income.
     *
     * @return Monthly net income.
     */
    final int getNetIncome() {
        return this.salary - this.tax;
    }

    /**
     * Monthly superann sum.
     *
     * @return Monthly superann sum.
     */
    final int getSuperannuation() {
        return this.superann;
    }
}
