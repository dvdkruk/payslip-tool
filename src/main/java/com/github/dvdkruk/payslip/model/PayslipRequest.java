/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Represent a payslip request.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipRequest {
    /**
     * Separator used in the string representation.
     */
    private static final String SEPARATOR = ",";

    /**
     * Employee.
     */
    private final Employee employee;
    /**
     * Superannuation rate.
     */
    private final BigDecimal rate;
    /**
     * Calculate payslip for this month.
     */
    private final Month month;

    /**
     * Payslip request constructor.
     *
     * @param employee Employee.
     * @param rate Superannuation rate.
     * @param month Payslip month.
     */
    public PayslipRequest(
        final Employee employee,
        final BigDecimal rate,
        final Month month) {
        this.employee = employee;
        this.rate = rate;
        this.month = month;
    }

    @Override
    public final String toString() {
        return new StringBuilder(this.getForename())
            .append(PayslipRequest.SEPARATOR).append(this.getSurname())
            .append(PayslipRequest.SEPARATOR).append(this.getDisplaySalary())
            .append(PayslipRequest.SEPARATOR).append(this.getDisplaySuperRate())
            .append("%")
            .append(PayslipRequest.SEPARATOR).append(this.getDisplayMonthName())
            .toString();
    }

    /**
     * Employee's full name, concatenation of forename and surname.
     *
     * @return Employee's full name.
     */
    public final String getFullName() {
        return this.employee.getFullName();
    }

    /**
     * Employee's forename.
     *
     * @return Employee's forename.
     */
    public final String getForename() {
        return this.employee.getForename();
    }

    /**
     * Employee's surname.
     *
     * @return Employee's surname.
     */
    public final String getSurname() {
        return this.employee.getSurname();
    }

    /**
     * Annual salary.
     *
     * @return Annual salary.
     */
    public final BigDecimal getAnnualSalary() {
        return this.employee.getAnnualSalary();
    }

    /**
     * Superannuation rate.
     *
     * @return Superannuation rate.
     */
    public final BigDecimal getSuperRate() {
        return this.rate;
    }

    /**
     * Month.
     *
     * @return Month.
     */
    public final Month getMonth() {
        return this.month;
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj) {
            equals = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equals = false;
        } else {
            final PayslipRequest that = (PayslipRequest) obj;
            equals = Objects.equals(this.employee, that.employee)
                && Objects.equals(this.rate, that.rate)
                && this.month == that.month;
        }
        return equals;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.employee, this.rate, this.month);
    }

    /**
     * Display annual salary without decimals/cents.
     *
     * @return Display annual salary without decimals/cents.
     */
    private String getDisplaySalary() {
        return getIntegerFormatter().format(this.getAnnualSalary());
    }

    /**
     * Display superannuation with two decimals.
     *
     * @return Display superannuation with two decimals.
     */
    private String getDisplaySuperRate() {
        return getTwoDecimalFormat().format(this.rate);
    }

    /**
     * Full display month name in English.
     *
     * @return Full display month name in English.
     */
    private String getDisplayMonthName() {
        return this.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /**
     * A two decimal formatter.
     *
     * @return A two decimal formatter.
     */
    private static DecimalFormat getTwoDecimalFormat() {
        final DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format;
    }

    /**
     * A integer/whole number formatter.
     *
     * @return A integer/whole number formatter.
     */
    private static DecimalFormat getIntegerFormatter() {
        final DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format;
    }
}
