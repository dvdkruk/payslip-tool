/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Arrays;
import java.util.Locale;

/**
 * A parser to convert a string into a PayslipRequest.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipRequestParser {

    /**
     * Parse token length.
     */
    public static final int PARSE_LEN = 5;
    /**
     * Exception message for parsing an invalid amount of elements.
     */
    public static final String INVAL_ELMNT_AMNT = String.format(
        "a payslip request must consist of %s (non empty) elements",
        PayslipRequestParser.PARSE_LEN
    );
    /**
     * Exception message parsing an invalid superannuation rate.
     */
    public static final String INVAL_SUPER_RATE =
        "super rate must have at least 1 number & end with a %";
    /**
     * Exception message for parsing a superannuation rate without percent sign.
     */
    public static final String NO_PERCNT_SUFFX =
        "super rate must be suffixed with a % character";
    /**
     * Exception message for parsing an invalid month.
     */
    public static final String INVAL_MONTH = "%s is an invalid month";

    /**
     * Parse index of forename.
     */
    private static final int FORENAME_I = 0;
    /**
     * Parse index of surname.
     */
    private static final int SURNAME_I = 1;
    /**
     * Parse index of annual salary.
     */
    private static final int ANNUAL_SALARY_I = 2;
    /**
     * Parse index of superannuation rate.
     */
    private static final int SUPER_RATE_I = 3;
    /**
     * Parse index of month.
     */
    private static final int MONTH_I = 4;
    /**
     * Comma.
     */
    private static final String SEPARATOR = ",";

    /**
     * Arg to parse.
     */
    private final String arg;

    /**
     * Payslip request parser.
     *
     * @param arg Argument to parse.
     */
    public PayslipRequestParser(final String arg) {
        this.arg = arg;
    }

    /**
     * Parses the string argument as a payslip request. A parsable string
     * should have the following format: &lt;forename&gt;,&lt;surname&gt;,
     * &lt;annual_salary&gt;,&lt;super_rate&gt;$,&lt;month&gt;
     *
     * @return The payslip request representing by the string argument.
     * @throws PayslipException If the string does not contain a parsable
     *  payslip request.
     */
    public final PayslipRequest toPayslipRequest() {
        if (this.arg == null) {
            throw new PayslipException("null");
        }
        final String[] split = this.arg.split(PayslipRequestParser.SEPARATOR);
        final String[] args = Arrays.stream(split)
            .map(String::trim)
            .filter(e -> !e.isEmpty())
            .toArray(String[]::new);
        if (args.length != PayslipRequestParser.PARSE_LEN) {
            throw new PayslipException(PayslipRequestParser.INVAL_ELMNT_AMNT);
        }
        return PayslipRequestParser.parse(args);
    }

    /**
     * Parse args array into {@code PayslipRequest}.
     *
     * @param args Arguments array to parse from.
     * @return Payslip request.
     */
    private static PayslipRequest parse(final String... args) {
        final Employee employee = parseEmployee(args);
        final BigDecimal rate = parseSuperRate(args);
        final Month month = parseMonth(args);
        return new PayslipRequest(employee, rate, month);
    }

    /**
     * Parse month rate from given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Month.
     */
    private static Month parseMonth(final String... args) {
        final String month = args[PayslipRequestParser.MONTH_I];
        try {
            return Month.valueOf(month.toUpperCase(Locale.getDefault()));
        } catch (final IllegalArgumentException iae) {
            final String msg =
                String.format(PayslipRequestParser.INVAL_MONTH, month);
            throw new PayslipException(msg, iae);
        }
    }

    /**
     * Parse superannuation rate from given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Superannuation rate.
     */
    private static BigDecimal parseSuperRate(final String... args) {
        final String rate = args[PayslipRequestParser.SUPER_RATE_I];
        if (rate.length() < 2) {
            throw new PayslipException(PayslipRequestParser.INVAL_SUPER_RATE);
        }
        if (rate.charAt(rate.length() - 1) != '%') {
            throw new PayslipException(
                PayslipRequestParser.NO_PERCNT_SUFFX
            );
        }
        final String digits = rate.substring(0, rate.length() - 1);
        return parseBigDecimal(digits, "super rate");
    }

    /**
     * Parse employee from given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Employee.
     */
    private static Employee parseEmployee(final String... args) {
        final String forename =
            args[PayslipRequestParser.FORENAME_I];
        final String surname =
            args[PayslipRequestParser.SURNAME_I];
        final BigDecimal salary = parseAnnualSalary(args);
        return new Employee(forename, surname, salary);
    }

    /**
     * Parse annual salary from the given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Annual salary.
     */
    private static BigDecimal parseAnnualSalary(final String... args) {
        final String salary = args[PayslipRequestParser.ANNUAL_SALARY_I];
        return parseBigDecimal(salary, "annual salary");
    }

    /**
     * Parse arg into a decimal.
     *
     * @param arg Argument to parse.
     * @param field Field shown when arg is not parable to a BigDecimal.
     * @return Arg as BigDecimal.
     */
    private static BigDecimal parseBigDecimal(
        final String arg,
        final String field) {
        try {
            return new BigDecimal(arg);
        } catch (final NumberFormatException nfe) {
            final String msg = String.format(
                "cannot parse %s '%s' into a number",
                field,
                arg
            );
            throw new PayslipException(msg, nfe);
        }
    }
}
