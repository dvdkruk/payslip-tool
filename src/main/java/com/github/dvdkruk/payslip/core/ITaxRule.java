/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import java.math.BigDecimal;

/**
 * Represents an income tax rule for tax calculations used in
 * {@code PayslipProcessor}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public interface ITaxRule {

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
