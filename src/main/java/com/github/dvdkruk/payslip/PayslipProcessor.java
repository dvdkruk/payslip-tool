/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.Employee;
import com.github.dvdkruk.payslip.model.FinancialInformation;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;
import com.github.dvdkruk.payslip.model.TaxRule;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
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
public final class PayslipProcessor {
    /**
     * Exception message for null request.
     */
    public static final String REQUEST_NULL = "Request is null";
    /**
     * Exception message for invalid forename.
     */
    public static final String INVAL_FORENAME = "First name is null or empty";
    /**
     * Exception message for invalid surname.
     */
    public static final String INVAL_SURNAME = "Last name is null or empty";
    /**
     * Exception message for invalid salary.
     */
    public static final String INVAL_SALARY =
        "Salary must be bigger than zero";
    /**
     * Exception message for null superannuation rate.
     */
    public static final String SUPER_RATE_NULL = "Super rate is null";
    /**
     * Exception message for invalid superannuation rate range.
     */
    public static final String INVAL_SUPER_RATE =
        "Super rate must be between 0% - 50%";
    /**
     * Default Australia tax rule set, year 2017.
     */
    public static final List<TaxRule> DEFAULT_RULES =
        Collections.unmodifiableList(
            Arrays.asList(
                new TaxRule(18200, 0, BigDecimal.ZERO),
                new TaxRule(37000, 0, new BigDecimal("0.190")),
                new TaxRule(80000, 3572, new BigDecimal("0.325")),
                new TaxRule(180000, 17547, new BigDecimal("0.37")),
                new TaxRule(Integer.MAX_VALUE, 54547, new BigDecimal("0.45"))
            )
        );

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
    private final List<TaxRule> rules;

    /**
     * Create a {@link PayslipProcessor} with the {@link
     *  PayslipProcessor#DEFAULT_RULES}.
     */
    public PayslipProcessor() {
        this.rules = PayslipProcessor.DEFAULT_RULES;
    }

    /**
     * Processes the request into a {@link PayslipResult}.
     *
     * @param request A payslip request.
     * @return The result of the request argument.
     * @throws PayslipException If the request is not valid.
     */
    public PayslipResult process(final PayslipRequest request) {
        validate(request);
        final String name = request.getEmployee().getFullName();
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
        final Employee employee = request.getEmployee();
        final int income = calculateIncome(employee.getAnnualSalary());
        final int tax = this.calculateTax(
            employee.getAnnualSalary().intValueExact()
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
     * Validates {@code request}.
     *
     * @param request Valid this request.
     */
    private static void validate(final PayslipRequest request) {
        if (request == null || request.getEmployee() == null) {
            throw new PayslipException(PayslipProcessor.REQUEST_NULL);
        }
        validate(request.getEmployee());
        if (request.getSuperRate() == null) {
            throw new PayslipException(PayslipProcessor.SUPER_RATE_NULL);
        }
        final BigDecimal rate = request.getSuperRate();
        if (isBetween(rate, BigDecimal.ZERO, PayslipProcessor.MAX_SUPER_RATE)) {
            throw new PayslipException(PayslipProcessor.INVAL_SUPER_RATE);
        }
    }

    /**
     * Validates {@code employee}.
     *
     * @param employee Valid this employee.
     */
    private static void validate(final Employee employee) {
        if (isNullOrEmpty(employee.getForename())) {
            throw new PayslipException(PayslipProcessor.INVAL_FORENAME);
        }
        if (isNullOrEmpty(employee.getSurname())) {
            throw new PayslipException(PayslipProcessor.INVAL_SURNAME);
        }
        if (employee.getAnnualSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayslipException(PayslipProcessor.INVAL_SALARY);
        }
    }

    /**
     * Checks if rate is between the given min and max.
     *
     * @param rate The rate to check.
     * @param min Lower bound limit.
     * @param max Upper bound limit.
     * @return True when rate is between min and max, else false is returned.
     */
    private static boolean isBetween(
        final BigDecimal rate,
        final BigDecimal min,
        final BigDecimal max) {
        return rate.compareTo(min) < 0 || rate.compareTo(max) > 0;
    }

    /**
     * Check if the given string is null or empty.
     *
     * @param string The string to check.
     * @return True when string is null or empty, else false is returned.
     */
    private static boolean isNullOrEmpty(final String string) {
        return string == null || string.trim().isEmpty();
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
