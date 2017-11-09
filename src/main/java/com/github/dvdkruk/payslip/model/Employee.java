/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contains employee data.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class Employee {

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

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder("Employee{");
        builder.append("forename='").append(this.forename).append('\'');
        builder.append(", surname='").append(this.surname).append('\'');
        builder.append(", salary='").append(this.salary).append('\'');
        builder.append('}');
        return builder.toString();
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj)  {
            equals = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equals = false;
        } else {
            final Employee that = (Employee) obj;
            return Objects.equals(this.forename, that.forename)
                && Objects.equals(this.surname, that.surname)
                && Objects.equals(this.salary, that.salary);
        }
        return equals;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.forename, this.surname, this.salary);
    }

    /**
     * Employee's forename.
     *
     * @return Employee's forename.
     */
    final String getForename() {
        return this.forename;
    }

    /**
     * Employee's surname name.
     *
     * @return Employee's surname name.
     */
    final String getSurname() {
        return this.surname;
    }

    /**
     * Employee's full name.
     *
     * @return Employee's full name.
     */
    final String getFullName() {
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
    final BigDecimal getAnnualSalary() {
        return this.salary;
    }
}
