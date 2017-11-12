/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.core.ITaxRule;
import java.math.BigDecimal;

/**
 * Represents an income tax rule for tax calculations used in
 * {@code PayslipProcessor}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class TaxRule implements ITaxRule {

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

    @Override
    public final int getMax() {
        return this.max;
    }

    @Override
    public final int getBase() {
        return this.base;
    }

    @Override
    public final BigDecimal getTax() {
        return this.tax;
    }
}
