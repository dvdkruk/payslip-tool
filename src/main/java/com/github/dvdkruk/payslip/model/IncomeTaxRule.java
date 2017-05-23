package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;

/**
 * Represents an income tax rule for tax calculations used in {@code PayslipProcessor}.
 */
public class IncomeTaxRule {

    private final int maxRange;

    private final int baseTax;

    private final BigDecimal taxPerDollar;

    public IncomeTaxRule(int maxRange, int baseTax, BigDecimal taxPerDollar) {
        this.maxRange = maxRange;
        this.baseTax = baseTax;
        this.taxPerDollar = taxPerDollar;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getBaseTax() {
        return baseTax;
    }

    public BigDecimal getTaxPerDollar() {
        return taxPerDollar;
    }
}
