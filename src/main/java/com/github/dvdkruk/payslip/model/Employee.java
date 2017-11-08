/*
 *  Copyright (c) 2017, Damiaan van der Kruk.
 *
 */

package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Employee {

    private final String forename;

    private final String surname;

    private final BigDecimal salary;

    public Employee(
        final String forename,
        final String surname,
        final BigDecimal salary) {
        this.forename = forename;
        this.surname = surname;
        this.salary = salary;
    }

    final String getForename() {
        return this.forename;
    }

    final String getSurname() {
        return this.surname;
    }

    final String getFullName() {
        return this.forename + " " + this.surname;
    }

    final BigDecimal getAnnualSalary() {
        return this.salary;
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("forename='").append(this.forename).append('\'');
        sb.append(", surname='").append(this.surname).append('\'');
        sb.append(", salary='").append(this.salary).append('\'');
        sb.append('}');
        return sb.toString();
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
}

