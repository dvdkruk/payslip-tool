/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.CommaSeparatedStringBuilder;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Represent a payslip result - Result of a successfully processed
 * {@code PayslipRequest} instance.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipResult {

    /**
     * Full name.
     */
    private final String name;

    /**
     * Calculated month.
     */
    private final Month month;

    /**
     * Financial information.
     */
    private final FinancialInformation financial;

    /**
     * Constructor for payslip result.
     *
     * @param name Full name.
     * @param month Calculation month.
     * @param financial Calculated financial information.
     */
    public PayslipResult(
        final String name,
        final Month month,
        final FinancialInformation financial) {
        this.name = name;
        this.month = month;
        this.financial = financial;
    }

    /**
     * Full name.
     *
     * @return Full name.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Calculation month.
     *
     * @return Calculation month.
     */
    public final Month getMonth() {
        return this.month;
    }

    /**
     * Monthly salary.
     *
     * @return Monthly salary.
     */
    public final int getSalary() {
        return this.financial.getSalary();
    }

    /**
     * Monthly income tax.
     *
     * @return Monthly income tax.
     */
    public final int getTax() {
        return this.financial.getTax();
    }

    /**
     * Monthly net income.
     *
     * @return Monthly net income.
     */
    public final int getNetIncome() {
        return this.financial.getNetIncome();
    }

    /**
     * Monthly superannuation.
     *
     * @return Monthly superannuation.
     */
    public final int getSuperannuation() {
        return this.financial.getSuperannuation();
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj) {
            equals = true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            equals = false;
        } else {
            final PayslipResult that = (PayslipResult) obj;
            equals = this.getSalary() == that.getSalary()
                && this.getTax() == that.getTax()
                && this.getSuperannuation() == that.getSuperannuation()
                && Objects.equals(this.name, that.name)
                && this.month == that.month;
        }
        return equals;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(
            this.name,
            this.month,
            this.getSalary(),
            this.getTax(),
            this.getSuperannuation()
        );
    }

    @Override
    public final String toString() {
        return new CommaSeparatedStringBuilder()
            .append(this.getName())
            .append(this.getDisplayMonth())
            .append(this.getSalary())
            .append(this.getTax())
            .append(this.getNetIncome())
            .append(this.getSuperannuation())
            .toString();
    }

    /**
     * Full month name in English.
     *
     * @return Full month name in English.
     */
    private String getMonthName() {
        return this.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /**
     * Display string of {@link PayslipResult#month}.
     *
     * @return Display string of {@link PayslipResult#month}.
     */
    private String getDisplayMonth() {
        return String.format(
            "01 %s - %s %1$s",
            this.getMonthName(),
            this.month.length(Year.now().isLeap())
        );
    }
}

