/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.core.CommaSeparatedStringBuilder;
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
public final class PayslipResult {

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
    public String getName() {
        return this.name;
    }

    /**
     * Calculation month.
     *
     * @return Calculation month.
     */
    public Month getMonth() {
        return this.month;
    }

    /**
     * Monthly salary.
     *
     * @return Monthly salary.
     */
    public int getSalary() {
        return this.financial.getSalary();
    }

    /**
     * Monthly income tax.
     *
     * @return Monthly income tax.
     */
    public int getTax() {
        return this.financial.getTax();
    }

    /**
     * Monthly net income.
     *
     * @return Monthly net income.
     */
    public int getNetIncome() {
        return this.financial.getNetIncome();
    }

    /**
     * Monthly superannuation.
     *
     * @return Monthly superannuation.
     */
    public int getSuperannuation() {
        return this.financial.getSuperannuation();
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj) {
            equals = true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            equals = false;
        } else {
            final PayslipResult that = (PayslipResult) obj;
            equals = Objects.equals(this.name, that.name)
                && Objects.equals(this.financial, that.financial)
                && this.month == that.month;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.month, this.financial);
    }

    @Override
    public String toString() {
        final CommaSeparatedStringBuilder builder =
            new CommaSeparatedStringBuilder();
        builder.append(this.name);
        builder.append(this.getDisplayMonth());
        this.appendFinancialInformation(builder);
        return builder.toString();
    }

    /**
     * Adds {@link PayslipResult#financial} information to {@code builder}.
     *
     * @param builder A {@link CommaSeparatedStringBuilder}.
     */
    private void appendFinancialInformation(
        final CommaSeparatedStringBuilder builder) {
        builder.append(this.getSalary());
        builder.append(this.getTax());
        builder.append(this.getNetIncome());
        builder.append(this.getSuperannuation());
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

