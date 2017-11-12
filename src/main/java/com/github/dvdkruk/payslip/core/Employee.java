/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import java.math.BigDecimal;

/**
 * Contains employee data.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Employee extends PayslipObject {

    /**
     * Employee's forename.
     */
    private final String forename;

    /**
     * Employee's surname.
     */
    private final String surname;

    /**
     * Employee's annual salary.
     */
    private final BigDecimal salary;

    /**
     * Employee constructor.
     *
     * @param forename Employee's forename.
     * @param surname Employee's surname.
     * @param salary Employee's annual salary.
     */
    public Employee(
        final String forename,
        final String surname,
        final BigDecimal salary) {
        super(forename, surname, toDisplaySalary(salary));
        this.forename = forename;
        this.surname = surname;
        this.salary = salary;
    }

    /**
     * Employee's forename.
     *
     * @return Employee's forename.
     */
    public String getForename() {
        return this.forename;
    }

    /**
     * Employee's surname name.
     *
     * @return Employee's surname name.
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Employee's full name.
     *
     * @return Employee's full name.
     */
    public String getFullName() {
        return new StringBuilder(this.forename)
            .append(" ")
            .append(this.surname)
            .toString();
    }

    /**
     * Annual salary.
     *
     * @return Annual salary.
     */
    public BigDecimal getAnnualSalary() {
        return this.salary;
    }

    /**
     * Display annual salary without decimals/cents.
     * @param salary Annual salary.
     * @return Display annual salary without decimals/cents.
     */
    private static String toDisplaySalary(final BigDecimal salary) {
        return createDecimalFormat(0).format(salary);
    }

}
