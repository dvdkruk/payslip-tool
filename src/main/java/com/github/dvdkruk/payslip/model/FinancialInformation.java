/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.util.Objects;

/**
 * Contains financial information for a month.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class FinancialInformation {

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

    @Override
    public boolean equals(final Object obj) {
        final boolean equal;
        if (this == obj) {
            equal = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equal = false;
        } else {
            final FinancialInformation that = (FinancialInformation) obj;
            equal = this.salary == that.salary
                && this.tax == that.tax
                && this.superann == that.superann;
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.salary, this.tax, this.superann);
    }

    @Override
    public String toString() {
        return new CommaSeparatedStringBuilder()
            .append(this.salary)
            .append(this.tax)
            .append(this.superann)
            .toString();
    }
}
