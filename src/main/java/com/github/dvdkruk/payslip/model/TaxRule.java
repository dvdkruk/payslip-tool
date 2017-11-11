/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;

/**
 * Represents an income tax rule for tax calculations used in
 * {@code PayslipProcessor}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class TaxRule {

    /**
     * Max/upper bound of this rule.
     */
    private final int max;

    /**
     * Tax base up to this rule.
     */
    private final int base;

    /**
     * Tax rule percentage.
     */
    private final BigDecimal tax;

    /**
     * Constructor for tax rule.
     *
     * @param max Max/upper bound of this rule.
     * @param base Base tax up to this rule.
     * @param tax Tax rule percentage.
     */
    public TaxRule(final int max, final int base, final BigDecimal tax) {
        this.max = max;
        this.base = base;
        this.tax = tax;
    }

    /**
     * Max/upper rule bound.
     *
     * @return Max/upper rule bound.
     */
    public final int getMax() {
        return this.max;
    }

    /**
     * Base tax amount up to this rule.
     *
     * @return Base tax amount up to this rule.
     */
    public final int getBase() {
        return this.base;
    }

    /**
     * Tax percentage.
     *
     * @return Tax percentage.
     */
    public final BigDecimal getTax() {
        return this.tax;
    }
}
