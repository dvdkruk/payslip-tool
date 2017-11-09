/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Represent a payslip request.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipRequest {

    /**
     * Parse index of forename.
     */
    private static final int FORENAME_INDEX = 0;
    /**
     * Parse index of surname.
     */
    private static final int SURNAME_INDEX = 1;
    /**
     * Parse index of annual salary.
     */
    private static final int ANNUAL_SALARY_INDEX = 2;
    /**
     * Parse index of superannuation rate.
     */
    private static final int SUPER_RATE_INDEX = 3;
    /**
     * Parse index of month.
     */
    private static final int MONTH_INDEX = 4;

    /**
     * Maps fields to parse indexes.
     */
    private static final Map<String, Integer> PARSE_INDEXES =
        new HashMap<String, Integer>() {
        {
            put("forename", PayslipRequest.FORENAME_INDEX);
            put("surname", PayslipRequest.SURNAME_INDEX);
            put("annual salary", PayslipRequest.ANNUAL_SALARY_INDEX);
            put("super rate", PayslipRequest.SUPER_RATE_INDEX);
            put("month", PayslipRequest.MONTH_INDEX);
        }
    };

    /**
     * Separator used in the string representation.
     */
    private static final String SEPARATOR = ",";

    /**
     * Formats BigDecimals with 2 decimals.
     */
    private static final DecimalFormat TWO_DECIMAL_FORMATTER =
        new DecimalFormat() {
        {
            setMaximumFractionDigits(2);
            setMinimumFractionDigits(0);
            setRoundingMode(RoundingMode.HALF_UP);
            setGroupingUsed(false);
        }
    };

    /**
     * Formats BigDecimals to a display string with no decimals.
     */
    private static final DecimalFormat INTEGER_FORMATTER = new DecimalFormat() {
        {
            setMinimumFractionDigits(0);
            setMaximumFractionDigits(0);
            setRoundingMode(RoundingMode.HALF_UP);
            setGroupingUsed(false);
        }
    };

    /**
     * Employee.
     */
    private final Employee employee;

    /**
     * Superannuation rate.
     */
    private final BigDecimal rate;

    /**
     * Calculate payslip for this month.
     */
    private final Month month;

    /**
     * Payslip request constructor.
     *
     * @param employee Employee.
     * @param rate Superannuation rate.
     * @param month Payslip month.
     */
    public PayslipRequest(
        final Employee employee,
        final BigDecimal rate,
        final Month month) {
        this.employee = employee;
        this.rate = rate;
        this.month = month;
    }

    @Override
    public final String toString() {
        return new StringBuilder(this.getForename())
            .append(PayslipRequest.SEPARATOR).append(this.getSurname())
            .append(PayslipRequest.SEPARATOR).append(this.getDisplaySalary())
            .append(PayslipRequest.SEPARATOR).append(this.getDisplaySuperRate())
            .append("%")
            .append(PayslipRequest.SEPARATOR).append(this.getDisplayMonthName())
            .toString();
    }

    /**
     * Employee's full name, concatenation of forename and surname.
     *
     * @return Employee's full name.
     */
    public final String getFullName() {
        return this.employee.getFullName();
    }

    /**
     * Employee's forename.
     *
     * @return Employee's forename.
     */
    public final String getForename() {
        return this.employee.getForename();
    }

    /**
     * Employee's surname.
     *
     * @return Employee's surname.
     */
    public final String getSurname() {
        return this.employee.getSurname();
    }

    /**
     * Annual salary.
     *
     * @return Annual salary.
     */
    public final BigDecimal getAnnualSalary() {
        return this.employee.getAnnualSalary();
    }

    /**
     * Superannuation rate.
     *
     * @return Superannuation rate.
     */
    public final BigDecimal getSuperRate() {
        return this.rate;
    }

    /**
     * Month.
     *
     * @return Month.
     */
    public final Month getMonth() {
        return this.month;
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj) {
            equals = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equals = false;
        } else {
            final PayslipRequest that = (PayslipRequest) obj;
            equals = Objects.equals(this.employee, that.employee)
                && Objects.equals(this.rate, that.rate)
                && this.month == that.month;
        }
        return equals;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.employee, this.rate, this.month);
    }

    /**
     * Parses the string argument as a payslip request. A parsable string
     * should have the following format: &lt;forename&gt;,&lt;surname&gt;,
     * &lt;annual_salary&gt;,&lt;super_rate&gt;$,&lt;month&gt;
     *
     * @param arg A parsable {@code String}.
     * @return The payslip request representing by the string argument.
     * @throws PayslipException If the string does not contain a parsable
     *  payslip request.
     */
    public static PayslipRequest parse(final String arg) {
        if (arg == null) {
            throw new PayslipException("null");
        }
        final String[] args = Arrays.stream(arg.split(SEPARATOR))
            .map(String::trim)
            .filter(e -> !e.isEmpty())
            .toArray(String[]::new);
        if (args.length != PayslipRequest.PARSE_INDEXES.size()) {
            final String msg = String.format(
                "a payslip request must consist of %s (non empty) elements",
                PayslipRequest.PARSE_INDEXES.size()
            );
            throw new PayslipException(msg);
        }
        return parse(args);
    }

    /**
     * Parse args array into {@code PayslipRequest}.
     *
     * @param args Arguments array to parse from.
     * @return Payslip request.
     */
    private static PayslipRequest parse(final String[] args) {
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
    private static Month parseMonth(final String[] args) {
        final String month = args[PayslipRequest.PARSE_INDEXES.get("month")];
        try {
            return Month.valueOf(month.toUpperCase(Locale.getDefault()));
        } catch (final IllegalArgumentException iae) {
            final String msg = String.format("%s is an invalid month", month);
            throw new PayslipException(msg, iae);
        }
    }

    /**
     * Parse superannuation rate from given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Superannuation rate.
     */
    private static BigDecimal parseSuperRate(final String[] args) {
        final String field = "super rate";
        final String rate = args[PayslipRequest.PARSE_INDEXES.get(field)];
        if (rate.length() < 2) {
            throw new PayslipException(
                "super rate must have at least 1 number & end with a %"
            );
        }
        if (rate.charAt(rate.length() - 1) != '%') {
            throw new PayslipException(
                "super rate must be suffixed with a % character"
            );
        }
        final String digits = rate.substring(0, rate.length() - 1);
        return parseBigDecimal(digits, field);
    }

    /**
     * Parse employee from given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Employee.
     */
    private static Employee parseEmployee(final String[] args) {
        final String forename =
            args[PayslipRequest.PARSE_INDEXES.get("forename")];
        final String surname =
            args[PayslipRequest.PARSE_INDEXES.get("surname")];
        final BigDecimal salary = parseAnnualSalary(args);
        return new Employee(forename, surname, salary);
    }

    /**
     * Parse annual salary from the given args array.
     *
     * @param args Arguments array to pick and parse from.
     * @return Annual salary.
     */
    private static BigDecimal parseAnnualSalary(final String[] args) {
        final String field = "annual salary";
        final String salary = args[PayslipRequest.PARSE_INDEXES.get(field)];
        return parseBigDecimal(salary, field);
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

    /**
     * Display annual salary without decimals/cents.
     *
     * @return Display annual salary without decimals/cents.
     */
    private String getDisplaySalary() {
        return INTEGER_FORMATTER.format(this.getAnnualSalary());
    }

    /**
     * Display superannuation with two decimals.
     *
     * @return Display superannuation with two decimals.
     */
    private String getDisplaySuperRate() {
        return TWO_DECIMAL_FORMATTER.format(this.rate);
    }

    /**
     * Full display month name in English.
     *
     * @return Full display month name in English.
     */
    private String getDisplayMonthName() {
        return this.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
