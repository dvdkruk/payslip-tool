/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.CommaSeparatedStringBuilder;
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
        final CommaSeparatedStringBuilder builder =
            new CommaSeparatedStringBuilder();
        builder.append(this.getForename());
        builder.append(this.getSurname());
        builder.append(this.getDisplaySalary());
        builder.append(this.getDisplaySuperRate());
        builder.append(this.getDisplayMonthName());
        return builder.toString();
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
        return createDecimalFormat(0).format(this.getAnnualSalary());
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
