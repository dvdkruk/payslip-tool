/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import java.math.BigDecimal;
import java.time.Month;

/**
 * Represent a payslip request.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class PayslipRequest extends PayslipObject {

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
        super(employee, toDisplaySuperRate(rate), toDisplayMonth(month));
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

    /**
     * Display superannuation with two decimals and % character.
     * @param rate Superannuation rate.
     * @return Display superannuation with two decimals and % character.
     */
    private static String toDisplaySuperRate(final BigDecimal rate) {
        return String.format("%s%%", createDecimalFormat(2).format(rate));
    }

}
