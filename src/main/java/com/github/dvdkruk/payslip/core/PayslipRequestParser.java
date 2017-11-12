/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.core;

import com.github.dvdkruk.payslip.model.Employee;
import com.github.dvdkruk.payslip.model.PayslipRequest;
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
    private static final String COMMA = ",";

    /**
     * Line to parse.
     */
    private final String line;

    /**
     * Payslip request parser for {@code line}.
     *
     * @param line Parse this line.
     */
    public PayslipRequestParser(final String line) {
        this.line = line;
    }

    /**
     * Parses {@link PayslipRequestParser#line} to a {@link PayslipRequest}.
     * Allowed format: {@code
     * <forename>,<surname>,<annual_salary>,<super_rate>%,<month>};
     *
     * @return A {@link PayslipRequest} that represents {@link
     *  PayslipRequestParser#line}.
     * @throws PayslipException When {@link PayslipRequestParser#line} is not
     *  parsable.
     */
    public final PayslipRequest toPayslipRequest() {
        if (this.line == null) {
            throw new PayslipException("null");
        }
        final String[] elements = this.line.split(PayslipRequestParser.COMMA);
        final String[] element = Arrays.stream(elements)
            .map(String::trim)
            .filter(e -> !e.isEmpty())
            .toArray(String[]::new);
        if (element.length != PayslipRequestParser.PARSE_LEN) {
            throw new PayslipException(PayslipRequestParser.INVAL_ELMNT_AMNT);
        }
        return PayslipRequestParser.parse(element);
    }

    /**
     * Parse {@code elements} into {@code PayslipRequest}.
     *
     * @param elements Parse this array.
     * @return A {@link PayslipRequest}.
     */
    private static PayslipRequest parse(final String... elements) {
        final Employee employee = parseEmployee(elements);
        final BigDecimal rate = parseSuperRate(elements);
        final Month month = parseMonth(elements);
        return new PayslipRequest(employee, rate, month);
    }

    /**
     * Parse month from {@code elements}.
     *
     * @param elements Pick and parse month from this array.
     * @return Month.
     */
    private static Month parseMonth(final String... elements) {
        final String month = elements[PayslipRequestParser.MONTH_I];
        try {
            return Month.valueOf(month.toUpperCase(Locale.getDefault()));
        } catch (final IllegalArgumentException iae) {
            final String msg =
                String.format(PayslipRequestParser.INVAL_MONTH, month);
            throw new PayslipException(msg, iae);
        }
    }

    /**
     * Parse superannuation rate from {@code elements}.
     *
     * @param elements Pick and parse superannuation rate from this array.
     * @return Superannuation rate.
     */
    private static BigDecimal parseSuperRate(final String... elements) {
        final String rate = elements[PayslipRequestParser.SUPER_RATE_I];
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
     * Parse employee from given {@code elements}.
     *
     * @param elements Pick and parse employee from this array.
     * @return Employee.
     */
    private static Employee parseEmployee(final String... elements) {
        final String forename =
            elements[PayslipRequestParser.FORENAME_I];
        final String surname =
            elements[PayslipRequestParser.SURNAME_I];
        final BigDecimal salary = parseAnnualSalary(elements);
        return new Employee(forename, surname, salary);
    }

    /**
     * Parses the annual salary from {@code elements}.
     *
     * @param elements Pick and parse the annual salary from this array.
     * @return Annual salary.
     */
    private static BigDecimal parseAnnualSalary(final String... elements) {
        final String salary = elements[PayslipRequestParser.ANNUAL_SALARY_I];
        return parseBigDecimal(salary, "annual salary");
    }

    /**
     * Parses {@code element} into a decimal.
     *
     * @param element Parses this element into {@link BigDecimal}.
     * @param field When {@code element} is not parable into {@link
     *  BigDecimal}, this field is shown in the exception message.
     * @return A {@link BigDecimal} with the value of {@code element}.
     */
    private static BigDecimal parseBigDecimal(
        final String element,
        final String field) {
        try {
            return new BigDecimal(element);
        } catch (final NumberFormatException nfe) {
            final String msg = String.format(
                "cannot parse %s '%s' into a number",
                field,
                element
            );
            throw new PayslipException(msg, nfe);
        }
    }
}
