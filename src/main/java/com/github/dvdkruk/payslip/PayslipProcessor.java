/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.FinancialInformation;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;
import com.github.dvdkruk.payslip.model.TaxRule;
import com.github.dvdkruk.payslip.model.TaxRuleHelper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
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

    /**
     * Amount of months.
     */
    private static final BigDecimal AMOUNT_OF_MONTHS = new BigDecimal("12");
    /**
     * BigDecimal of 100.
     */
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    /**
     * Max super rate.
     */
    private static final BigDecimal MAX_SUPER_RATE = new BigDecimal("50");
    /**
     * Scale for calculations.
     */
    private static final int SCALE = 99;

    /**
     * List containing all the income tax rules for the calculation.
     */
    private final List<TaxRule> rules = TaxRuleHelper.DEFAULT_RULES;

    /**
     * Processes the request argument as a payslip result.
     *
     * @param request A payslip request.
     * @return The result of the request argument.
     * @throws PayslipException If the request is not valid.
     */
    public final PayslipResult process(final PayslipRequest request) {
        validate(request);
        final String name = request.getFullName();
        final Month month = request.getMonth();
        return new PayslipResult(name, month, this.calculate(request));
    }

    /**
     * Calculates monthly salary/income, income tax and superannuation.
     *
     * @param request With the data for the calculation.
     * @return Monthly financial information.
     */
    private FinancialInformation calculate(final PayslipRequest request) {
        final int income = calculateIncome(request.getAnnualSalary());
        final int tax = this.calculateTax(
            request.getAnnualSalary().intValueExact()
        );
        final int superann = calculateSuper(income, request.getSuperRate());
        return new FinancialInformation(income, tax, superann);
    }

    /**
     * Calculates monthly salary.
     *
     * @param salary Annual salary.
     * @return Monthly salary.
     */
    private static int calculateIncome(final BigDecimal salary) {
        return salary
            .divide(PayslipProcessor.AMOUNT_OF_MONTHS, 0, RoundingMode.HALF_UP)
            .intValueExact();
    }

    /**
     * Validates the given request.
     *
     * @param request The request to validate.
     */
    private static void validate(final PayslipRequest request) {
        if (request == null) {
            throw new PayslipException("Request is null");
        }
        if (isNullOrEmpty(request.getForename())) {
            throw new PayslipException("First name is null or empty");
        }
        if (isNullOrEmpty(request.getSurname())) {
            throw new PayslipException("Last name is null or empty");
        }
        if (request.getAnnualSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayslipException("Salary must be bigger than zero");
        }
        if (request.getSuperRate() == null) {
            throw new PayslipException("Super rate is null");
        }
        final BigDecimal rate = request.getSuperRate();
        final BigDecimal min = BigDecimal.ZERO;
        if (isBetween(rate, min)) {
            throw new PayslipException("Super rate must be between 0% - 50%");
        }
    }

    /**
     * Checks if rate is between the given min and max.
     *
     * @param rate The rate to check.
     * @param min Minimal value for the check.
     * @return True when rate is between min and max, else false is returned.
     */
    private static boolean isBetween(
        final BigDecimal rate,
        final BigDecimal min) {
        return rate.compareTo(min) < 0
            || rate.compareTo(PayslipProcessor.MAX_SUPER_RATE) > 0;
    }

    /**
     * Check if the given string is null or empty.
     *
     * @param string The string to check.
     * @return True when string is null or empty, else false is returned.
     */
    private static boolean isNullOrEmpty(final String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Calculate super for the given income and rate.
     *
     * @param income The income for the super calculation.
     * @param rate The rate used in the calculation.
     * @return Monthly super in complete/whole digits.
     */
    private static int calculateSuper(final int income, final BigDecimal rate) {
        final int rounding = BigDecimal.ROUND_HALF_UP;
        return rate
            .divide(PayslipProcessor.HUNDRED, PayslipProcessor.SCALE, rounding)
            .multiply(BigDecimal.valueOf(income))
            .toBigInteger()
            .intValueExact();
    }

    /**
     * Calculates tax for the given salary.
     *
     * @param salary The salary used in the tax calculation.
     * @return Tax in complete/whole digits.
     */
    private int calculateTax(final int salary) {
        final TaxRule rule = this.rules.get(0);
        final int tax;
        if (salary > rule.getMax()) {
            tax = this.calculateTax(salary, 1, rule.getMax());
        } else {
            tax = calculateTax(salary, rule);
        }
        return tax;
    }

    /**
     * Calculates tax for the given salary using the rule corresponding to the
     * given index.
     *
     * @param salary The salary for the tax calculation.
     * @param index The index of the rule for usage in the calculation.
     * @param subtract The subtract this amount before tax calculation.
     * @return Tax in complete/whole digits.
     */
    private int calculateTax(
        final int salary,
        final int index,
        final int subtract) {
        if (index >= this.rules.size()) {
            final String msg = String.format(
                "No tax rule found for annual salary '%s'",
                salary
            );
            throw new NoSuchElementException(msg);
        }
        final TaxRule rule = this.rules.get(index);
        final int tax;
        if (salary > rule.getMax()) {
            tax = this.calculateTax(salary, index + 1, rule.getMax());
        } else {
            final int taxable = salary - subtract;
            tax = calculateTax(taxable, rule);
        }
        return tax;
    }

    /**
     * Calculates tax in complete dollars for the given salary using the given
     * rule.
     *
     * @param salary The salary for the tax calculation.
     * @param rule The rule for calculating the tax.
     * @return Tax in complete/whole dollars.
     */
    private static int calculateTax(final int salary, final TaxRule rule) {
        final RoundingMode rounding = RoundingMode.HALF_UP;
        return BigDecimal.valueOf(salary)
            .multiply(rule.getTax())
            .add(BigDecimal.valueOf(rule.getBase()))
            .divide(PayslipProcessor.AMOUNT_OF_MONTHS, 0, rounding)
            .intValueExact();
    }
}
