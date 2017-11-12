/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import com.github.dvdkruk.payslip.utils.CommaSeparatedStringBuilder;
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
public final class PayslipRequest {

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

    /**
     * Get employee of this request.
     *
     * @return A {@link Employee}.
     */
    public Employee getEmployee() {
        return this.employee;
    }

    /**
     * Superannuation rate.
     *
     * @return Superannuation rate.
     */
    public BigDecimal getSuperRate() {
        return this.rate;
    }

    /**
     * Month.
     *
     * @return Month.
     */
    public Month getMonth() {
        return this.month;
    }

    @Override
    public String toString() {
        final CommaSeparatedStringBuilder builder =
            new CommaSeparatedStringBuilder();
        this.appendEmployee(builder);
        builder.append(this.getDisplaySuperRate());
        builder.append(this.getDisplayMonthName());
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
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
    public int hashCode() {
        return Objects.hash(this.employee, this.rate, this.month);
    }

    /**
     * Display annual salary without decimals/cents.
     *
     * @return Display annual salary without decimals/cents.
     */
    private String getDisplaySalary() {
        return createDecimalFormat(0).format(this.employee.getAnnualSalary());
    }

    /**
     * Display superannuation with two decimals and % character.
     *
     * @return Display superannuation with two decimals and % character.
     */
    private String getDisplaySuperRate() {
        return String.format("%s%%", createDecimalFormat(2).format(this.rate));
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
     * Adds {@link PayslipRequest#employee} information to {@code builder}.
     *
     * @param builder A {@link CommaSeparatedStringBuilder}.
     */
    private void appendEmployee(final CommaSeparatedStringBuilder builder) {
        builder.append(this.employee.getForename());
        builder.append(this.employee.getSurname());
        builder.append(this.getDisplaySalary());
    }

    /**
     * A {@link DecimalFormat} with {@code max} set as maximum fraction digits
     * amount.
     *
     * @param max Maximum fraction digits amount.
     * @return A {@link DecimalFormat}.
     */
    private static DecimalFormat createDecimalFormat(final int max) {
        final DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(max);
        format.setMinimumFractionDigits(0);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format;
    }

}
