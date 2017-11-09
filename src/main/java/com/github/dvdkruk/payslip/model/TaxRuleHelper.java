/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Tax rule helper.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class TaxRuleHelper {

    /**
     * Default Australia tax rule set, year 2017.
     */
    public static final List<TaxRule> DEFAULT_RULES = Arrays.asList(
        new TaxRule(18200, 0, BigDecimal.ZERO),
        new TaxRule(37000, 0, new BigDecimal("0.190")),
        new TaxRule(80000, 3572, new BigDecimal("0.325")),
        new TaxRule(180000, 17547, new BigDecimal("0.37")),
        new TaxRule(Integer.MAX_VALUE, 54547, new BigDecimal("0.45"))
    );

    /**
     * This is util/helper class.
     */
    private TaxRuleHelper() {
    }
}
