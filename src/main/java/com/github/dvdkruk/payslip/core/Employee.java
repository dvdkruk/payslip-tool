/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import com.github.dvdkruk.payslip.utils.CommaSeparatedStringBuilder;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contains employee data.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Employee {

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

    @Override
    public String toString() {
        return new CommaSeparatedStringBuilder()
            .append(this.forename)
            .append(this.surname)
            .append(this.salary.toString())
            .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj)  {
            equals = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equals = false;
        } else {
            final Employee that = (Employee) obj;
            equals = Objects.equals(this.forename, that.forename)
                && Objects.equals(this.surname, that.surname)
                && Objects.equals(this.salary, that.salary);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.forename, this.surname, this.salary);
    }

}
