/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;
import com.github.dvdkruk.payslip.model.TaxRule;
import com.github.dvdkruk.payslip.model.TaxRuleHelper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Payslip processor - Processes {@code PayslipRequest} instance to {@code
 * PayslipResult}.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipProcessor {

    private static final BigDecimal AMOUNT_OF_MONTHS = new BigDecimal("12");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal MIN_SUPER_RATE = new BigDecimal("0");
    private static final BigDecimal MAX_SUPER_RATE = new BigDecimal("50");
    private static final int SCALE = 99;

    private final List<TaxRule> rules = TaxRuleHelper.getDefaultTaxRules();

    /**
     * Processes the request argument as a payslip result.
     *
     * @param request A payslip request.
     * @return The result of the request argument.
     * @throws PayslipException If the request is not valid.
     */
    public final PayslipResult process(final PayslipRequest request) {
        validate(request);
        final int income = request.getAnnualSalary()
            .divide(AMOUNT_OF_MONTHS, 0, RoundingMode.HALF_UP)
            .intValueExact();
        final int tax = this.calculateTax(
            request.getAnnualSalary().intValueExact()
        );
        final int superann = calculateSuper(income, request.getSuperRate());
        return new PayslipResult(
            request.getFullName(), request.getMonth(),
            income, tax, superann
        );
    }

    private static void validate(final PayslipRequest request) {
        if (request == null) {
            throw new PayslipException("Request is null");
        }
        if (request.getFirstName() == null
            || request.getFirstName().isEmpty()) {
            throw new PayslipException("First name is null or empty");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new PayslipException("Last name is null or empty");
        }
        if (request.getAnnualSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayslipException("Salary must be bigger than zero");
        }
        if (request.getSuperRate() == null) {
            throw new PayslipException("Super rate is null");
        }
        if (request.getSuperRate().compareTo(MIN_SUPER_RATE) < 0
            || request.getSuperRate().compareTo(MAX_SUPER_RATE) > 0) {
            throw new PayslipException("Super rate must be between 0% - 50%");
        }
    }

    private static int calculateSuper(final int income, final BigDecimal rate) {
        return rate.divide(HUNDRED, SCALE, BigDecimal.ROUND_HALF_UP)
            .multiply(BigDecimal.valueOf(income))
            .toBigInteger()
            .intValueExact();
    }

    private int calculateTax(final int salary) {
        final TaxRule rule = this.rules.get(0);
        final int tax;
        if (salary > rule.getMaxRange()) {
            tax = calculateTax(salary, 1);
        } else {
            tax = calculateTax(salary, rule);
        }
        return tax;
    }

    private int calculateTax(final int salary, final int index) {
        if (index >= this.rules.size()) {
            final String msg = String.format(
                "No tax rule found for annual salary '%s'",
                salary
            );
            throw new NoSuchElementException(msg);
        }
        final TaxRule rule = this.rules.get(index);
        final int tax;
        if (salary > rule.getMaxRange()) {
            tax = calculateTax(salary, index + 1);
        } else {
            final TaxRule previous = this.rules.get(index - 1);
            tax = calculateTax(salary, rule, previous);
        }
        return tax;
    }

    private static int calculateTax(final int salary, final TaxRule... rules) {
        if (rules.length > 2) {
            throw new IllegalArgumentException("Only 2 rules are allowed");
        }
        final int taxable = salary - rules[1].getMaxRange();
        return calculateTax(taxable, rules[0]);
    }

    private static int calculateTax(final int salary, final TaxRule rule) {
        return BigDecimal.valueOf(salary)
            .multiply(rule.getTaxPerDollar())
            .add(BigDecimal.valueOf(rule.getBaseTax()))
            .divide(AMOUNT_OF_MONTHS, 0, RoundingMode.HALF_UP)
            .intValueExact();
    }
}
