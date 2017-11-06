package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxRuleHelper {
    
    public static List<TaxRule> getDefaultTaxRules() {
        final List<TaxRule> taxRules = new ArrayList<>(5);
        taxRules.add(new TaxRule(18200, 0, BigDecimal.ZERO));
        taxRules.add(new TaxRule(37000, 0, new BigDecimal("0.190")));
        taxRules.add(new TaxRule(80000, 3572, new BigDecimal("0.325")));
        taxRules.add(new TaxRule(180000, 17547, new BigDecimal("0.37")));
        taxRules.add(new TaxRule(Integer.MAX_VALUE, 54547, new BigDecimal("0.45")));
        return taxRules;
    }
}
