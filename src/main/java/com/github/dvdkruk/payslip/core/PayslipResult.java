/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import java.time.Month;
import java.time.Year;

/**
 * Represent a payslip result - Result of a successfully processed
 * {@code PayslipRequest} instance.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class PayslipResult extends PayslipObject {

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
        super(name, toDisplayMonthRange(month), financial);
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

    /**
     * Display string of {@link PayslipResult#month}.
     *
     * @param month Month.
     * @return Display string of {@link PayslipResult#month}.
     */
    private static String toDisplayMonthRange(final Month month) {
        return String.format(
            "01 %s - %s %1$s",
            toDisplayMonth(month),
            month.length(Year.now().isLeap())
        );
    }

}

