/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

/**
 * Contains financial information for a month.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class FinancialInformation extends PayslipObject {

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
        super(salary, tax, salary - tax, superannuation);
        this.salary = salary;
        this.tax = tax;
        this.superann = superannuation;
    }

    /**
     * Monthly salary.
     *
     * @return Monthly salary.
     */
    public int getSalary() {
        return this.salary;
    }

    /**
     * Monthly income tax.
     *
     * @return Monthly income tax.
     */
    public int getTax() {
        return this.tax;
    }

    /**
     * Monthly net income.
     *
     * @return Monthly net income.
     */
    public int getNetIncome() {
        return this.salary - this.tax;
    }

    /**
     * Monthly superann sum.
     *
     * @return Monthly superann sum.
     */
    public int getSuperannuation() {
        return this.superann;
    }

}
