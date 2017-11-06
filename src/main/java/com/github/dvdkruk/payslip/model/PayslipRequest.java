package com.github.dvdkruk.payslip.model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * Represent a payslip request.
 */
public class PayslipRequest {

    private static final String SEPARATOR = ",";

    private static final DecimalFormat TWO_DECIMAL_FORMATTER = new DecimalFormat();

    private static final DecimalFormat INTEGER_FORMATTER = new DecimalFormat();

    static {
        TWO_DECIMAL_FORMATTER.setMaximumFractionDigits(2);
        TWO_DECIMAL_FORMATTER.setMinimumFractionDigits(0);
        TWO_DECIMAL_FORMATTER.setRoundingMode(RoundingMode.HALF_UP);
        TWO_DECIMAL_FORMATTER.setGroupingUsed(false);

        INTEGER_FORMATTER.setMinimumFractionDigits(0);
        INTEGER_FORMATTER.setMaximumFractionDigits(0);
        INTEGER_FORMATTER.setRoundingMode(RoundingMode.HALF_UP);
        INTEGER_FORMATTER.setGroupingUsed(false);
    }

    private final String firstName;

    private final String lastName;

    private final BigDecimal annualSalary;

    private final BigDecimal superRate;

    private final Month month;

    private final String string;

    public PayslipRequest(String firstName, String lastName, BigDecimal annualSalary, BigDecimal superRate, Month month) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.annualSalary = annualSalary;
        this.superRate = superRate;
        this.month = month;

        this.string = firstName +
                SEPARATOR + lastName +
                SEPARATOR + INTEGER_FORMATTER.format(annualSalary) +
                SEPARATOR + TWO_DECIMAL_FORMATTER.format(superRate) + "%" +
                SEPARATOR + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /**
     * Parses the string argument as a payslip request. A parsable string should have the following format:
     * &lt;first_name&gt;,&lt;last_name&gt;,&lt;annual_salary&gt;,&lt;super_rate&gt;$,&lt;month&gt;,</>
     *
     * @param arg a parsable {@code String}
     * @return the payslip request representing by the string argument.
     * @throws PayslipException if the string does not contain a parsable payslip request.
     */
    public static PayslipRequest parse(String arg) {
        if (arg == null) {
            throw new PayslipException("null");
        }
        String[] args = Arrays.stream(arg.split(SEPARATOR))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .toArray(String[]::new);

        if (args.length != 5) {
            throw new PayslipException("a payslip request must consist of 5 (non empty) elements");
        }
        return parse(args);
    }

    private static PayslipRequest parse(String[] args) {
        String firstName = args[0];
        String lastName = args[1];
        BigDecimal annualSalary = parseToBigDecimal(args[2], "annual salary");
        if (args[3].length() < 2) {
            throw new PayslipException("super rate must consist of at least one number and a '%' (percent character)");
        }
        if (args[3].charAt(args[3].length() - 1) != '%') {
            throw new PayslipException("super rate must be suffixed with a '%' (percent character)");
        }
        String superRateArg = args[3].substring(0, args[3].length() - 1);
        BigDecimal superRate = parseToBigDecimal(superRateArg, "super rate");

        Month month;
        try {
            month = Month.valueOf(args[4].toUpperCase(Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            throw new PayslipException(args[4] + " is an invalid month", e);
        }
        return new PayslipRequest(firstName, lastName, annualSalary, superRate, month);
    }

    private static BigDecimal parseToBigDecimal(String arg, String fieldName) {
        try {
            return new BigDecimal(arg);
        } catch (NumberFormatException e) {
            throw new PayslipException("cannot parse " + fieldName + " '" + arg + "' into a number", e);
        }
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, annualSalary, superRate, month);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof PayslipRequest) {
            PayslipRequest request = (PayslipRequest) obj;
            return firstName.equals(request.firstName)
                    && lastName.equals(request.lastName)
                    && annualSalary.equals(request.annualSalary)
                    && superRate.equals(request.getSuperRate())
                    && month.equals(request.month);
        }
        return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return annualSalary;
    }

    public BigDecimal getSuperRate() {
        return superRate;
    }

    public Month getMonth() {
        return month;
    }
}
