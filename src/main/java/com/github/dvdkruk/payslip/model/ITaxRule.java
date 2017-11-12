/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an income tax rule for tax calculations used in
 * {@code PayslipProcessor}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public interface ITaxRule {

    /**
     * Default Australia tax rule set, year 2017.
     */
    List<ITaxRule> DEFAULT = Collections.unmodifiableList(
        Arrays.asList(
            new TaxRule(18200, 0, BigDecimal.ZERO),
            new TaxRule(37000, 0, new BigDecimal("0.190")),
            new TaxRule(80000, 3572, new BigDecimal("0.325")),
            new TaxRule(180000, 17547, new BigDecimal("0.37")),
            new TaxRule(Integer.MAX_VALUE, 54547, new BigDecimal("0.45"))
        )
    );

    /**
     * Max/upper rule bound.
     *
     * @return Max/upper rule bound.
     */
    int getMax();

    /**
     * Base tax amount up to this rule.
     *
     * @return Base tax amount up to this rule.
     */
    int getBase();

    /**
     * Tax percentage.
     *
     * @return Tax percentage.
     */
    BigDecimal getTax();

}
