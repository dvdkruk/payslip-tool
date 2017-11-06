/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.ArrayList;
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
     * Max ranges.
     */
    private static final int[] MAX_RANGES = new int[]{
        18200, 37000, 80000, 180000, Integer.MAX_VALUE,
    };
    /**
     * Base taxes.
     */
    private static final int[] BASE_TAXES = new int[]{
        0, 0, 3572, 17547, 54547,
    };
    /**
     * Tax per dollars.
     */
    private static final String[] TAX_PER_DOLLARS = new String[]{
        "0", "0.190", "0.325", "0.37", "0.45",
    };

    /**
     * This is util/helper class.
     */
    private TaxRuleHelper() {
    }

    /**
     * Creates a default set of tax rules.
     *
     * @return A default set of tax rules.
     */
    public static List<TaxRule> getDefaultTaxRules() {
        final int length = TaxRuleHelper.MAX_RANGES.length;
        final List<TaxRule> rules = new ArrayList<>(length);
        for (int index = 0; index < length; ++index) {
            final TaxRule rule = createTaxRuleFor(index);
            rules.add(rule);
        }
        return rules;
    }

    /**
     * Create a tax rule for the given index.
     *
     * @param index Index for selecting tax rule information.
     * @return A TaxRule.
     */
    private static TaxRule createTaxRuleFor(final int index) {
        final int max = TaxRuleHelper.MAX_RANGES[index];
        final int base = TaxRuleHelper.BASE_TAXES[index];
        final String tax = TaxRuleHelper.TAX_PER_DOLLARS[index];
        return new TaxRule(max, base, new BigDecimal(tax));
    }
}
