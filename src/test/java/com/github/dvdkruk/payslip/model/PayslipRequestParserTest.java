/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.TestAssert;
import java.math.BigDecimal;
import java.time.Month;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Contains unit tests for {@link PayslipRequestParser}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class PayslipRequestParserTest {
    /**
     * A forename.
     */
    private static final String FORENAME = "Jennifer";
    /**
     * A surname.
     */
    private static final String SURNAME = "Lawrence";
    /**
     * An income tax percentage.
     */
    private static final BigDecimal TAX_PERCNT = new BigDecimal("10.1");
    /**
     * A valid parsable {@link PayslipRequest} line.
     */
    private static final String LINE = "Jennifer,Lawrence,10,10.1%,May";

    /**
     * Checks if the forename is parsed correctly.
     */
    @Test
    public void parseForename() {
        new TestAssert<>(parse().getForename())
            .isSame(PayslipRequestParserTest.FORENAME);
    }

    /**
     * Checks if the surname is parsed correctly.
     */
    @Test
    public void parseSurname() {
        new TestAssert<>(parse().getSurname())
            .isSame(PayslipRequestParserTest.SURNAME);
    }

    /**
     * Checks if the annual salary is parsed correctly.
     */
    @Test
    public void parseAnnualSalary() {
        new TestAssert<>(parse().getAnnualSalary()).isSame(BigDecimal.TEN);
    }

    /**
     * Checks if the income tax is parsed correctly.
     */
    @Test
    public void parseIncomeTax() {
        new TestAssert<>(parse().getSuperRate())
            .isSame(PayslipRequestParserTest.TAX_PERCNT);
    }

    /**
     * Checks if the month is parsed correctly.
     */
    @Test
    public void parseMonth() {
        new TestAssert<>(parse().getMonth()).isSame(Month.MAY);
    }

    /**
     * Tests if a {@link PayslipException} with {@link
     * PayslipRequestParser#INVAL_MONTH} message is thrown when
     * parsing an invalid month.
     */
    @Test
    public void parseInvalidMonth() {
        checkPayslipException(
            "Peter is an invalid month",
            "Jennifer,Lawrence,1337,10.1%,Peter"
        );
    }

    /**
     * Tests if a {@link PayslipException} with {@link
     * PayslipRequestParser#INVAL_ELMNT_AMNT} message is thrown when
     * parsing lines with invalid elements or amounts of elements.
     *
     * @param line A line with invalid elements or amounts of elements.
     */
    @ParameterizedTest
    @ValueSource(strings = {" , , , , ", "Jennifer,Lawrence,1337,10.1%"})
    public void parseInvalidArgumentAmount(final String line) {
        final String msg = PayslipRequestParser.INVAL_ELMNT_AMNT;
        checkPayslipException(msg, line);
    }

    /**
     * Tries to parsing the given {@code line} and checks if a {@link
     * PayslipException} is thrown with the {@code expected} message.
     *
     * @param expected The message of the thrown {@link PayslipException}.
     * @param line The line that needs to be parsed.
     */
    private static void checkPayslipException(
        final String expected,
        final String line) {
        PayslipException exception = null;
        try {
            new PayslipRequestParser(line).toPayslipRequest();
        } catch (final PayslipException pex) {
            exception = pex;
        }
        assert exception != null;
        new TestAssert<>(exception.getMessage()).equalTo(expected);
    }

    /**
     * Parses {@link PayslipRequestParserTest#LINE} into a {@link
     * PayslipRequest}.
     *
     * @return A {@link PayslipRequest}.
     */
    private static PayslipRequest parse() {
        return new PayslipRequestParser(PayslipRequestParserTest.LINE)
            .toPayslipRequest();
    }
}
