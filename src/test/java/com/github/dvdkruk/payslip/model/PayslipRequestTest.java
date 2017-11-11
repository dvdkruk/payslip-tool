/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.math.BigDecimal;
import java.time.Month;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit test for {@link PayslipRequest}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipRequestTest {

    /**
     * A surname.
     */
    private static final String SURNAME = "DiCaprio";

    /**
     * The forename George.
     */
    private static final String GEORGE = "George";

    /**
     * The forename Leonardo.
     */
    private static final String LEONARDO = "Leonardo";

    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#LEONARDO}.
     */
    private static final PayslipRequest LEONARDO_REQ =
        createPayslipRequest(createDiCaprioEmployee(LEONARDO));

    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#GEORGE}.
     */
    private static final PayslipRequest GEORGE_REQ = createRequestFor(GEORGE);

    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#GEORGE}, which should be equal to {@link
     * PayslipRequestTest#GEORGE_REQ}.
     */
    private static final PayslipRequest SAME_GEORGE_REQ =
        createRequestFor(GEORGE);

    /**
     * Expected request line representation for {@link
     * PayslipRequestTest#GEORGE_REQ}.
     */
    private static final String GEORGE_LINE = "George,DiCaprio,10,1%,January";

    /**
     * Test for {@link PayslipRequest#getMonth()}.
     */
    @Test
    public final void getMonth() {
        Assert.assertEquals(Month.JANUARY, LEONARDO_REQ.getMonth());
    }

    /**
     * Test for {@link PayslipRequest#getSuperRate()}.
     */
    @Test
    public final void getSuperRate() {
        Assert.assertEquals(BigDecimal.ONE, LEONARDO_REQ.getSuperRate());
    }

    /**
     * Test for {@link PayslipRequest#getAnnualSalary()}.
     */
    @Test
    public final void getAnnualSalary() {
        Assert.assertEquals(BigDecimal.TEN, LEONARDO_REQ.getAnnualSalary());
    }

    /**
     * Test for {@link PayslipRequest#getFullName()}.
     */
    @Test
    public final void getFullName() {
        final String name = String.format("%s %s", LEONARDO, SURNAME);
        Assert.assertEquals(name, LEONARDO_REQ.getFullName());
    }

    /**
     * Test for {@link PayslipRequest#getSurname()}.
     */
    @Test
    public final void getSurname() {
        Assert.assertEquals(SURNAME, LEONARDO_REQ.getSurname());
    }

    /**
     * Test for {@link PayslipRequest#getForename()}.
     */
    @Test
    public final void getForename() {
        Assert.assertEquals(LEONARDO, LEONARDO_REQ.getForename());
    }

    /**
     * Test for {@link PayslipRequest#toString()}.
     */
    @Test
    public final void toStringTest() {
        Assert.assertEquals(GEORGE_LINE, GEORGE_REQ.toString());
    }

    /**
     * Test for {@link PayslipRequest#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        Assert.assertEquals(GEORGE_REQ.hashCode(), SAME_GEORGE_REQ.hashCode());
        Assert.assertNotEquals(GEORGE_REQ.hashCode(), LEONARDO_REQ.hashCode());
    }

    /**
     * Tests for {@link PayslipRequest#equals(Object)}.
     */
    @Test
    public final void equalsCheck() {
        Assert.assertEquals(GEORGE_REQ, SAME_GEORGE_REQ);
        Assert.assertEquals(SAME_GEORGE_REQ, GEORGE_REQ);
        Assert.assertNotEquals(GEORGE_REQ, LEONARDO_REQ);
    }

    /**
     * Parses the {@link PayslipRequestTest#GEORGE} request line and checks if
     * all getters return the right values.
     */
    @Test
    public final void parseTest() {
        final PayslipRequest request = new PayslipRequestParser(GEORGE_LINE)
            .toPayslipRequest();
        Assert.assertEquals(GEORGE, request.getForename());
        Assert.assertEquals(SURNAME, request.getSurname());
        Assert.assertEquals(BigDecimal.TEN, request.getAnnualSalary());
        Assert.assertEquals(BigDecimal.ONE, request.getSuperRate());
        Assert.assertEquals(Month.JANUARY, request.getMonth());
    }

    /**
     * Tests if a {@link PayslipException} with {@link
     * PayslipRequestParser#INVALID_MONTH} message is thrown when
     * parsing an invalid month.
     */
    @Test
    public final void parseInvalidMonth() {
        checkPayslipException(
            "Peter is an invalid month",
            "Jennifer,Lawrence,1337,10.1%,Peter"
        );
    }

    /**
     * Tests if a {@link PayslipException} with {@link
     * PayslipRequestParser#INVALID_ELEMENT_AMOUNT} message is thrown when
     * parsing lines with invalid elements or amounts of elements.
     *
     * @param line A line with invalid elements or amounts of elements.
     */
    @ParameterizedTest
    @ValueSource(strings = {" , , , , ", "Jennifer,Lawrence,1337,10.1%"})
    public final void parseInvalidArgumentAmount(final String line) {
        final String msg = PayslipRequestParser.INVALID_ELEMENT_AMOUNT;
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
        Assert.assertNotNull(exception);
        Assert.assertEquals(expected, exception.getMessage());
    }

    /**
     * Creates a {@link PayslipRequest} for an employee with the given {@code
     * forename}.
     *
     * @param forename The forename used in the {@link PayslipRequest}.
     * @return A {@link PayslipRequest}.
     */
    private static PayslipRequest createRequestFor(final String forename) {
        return createPayslipRequest(createDiCaprioEmployee(forename));
    }

    /**
     * Create a {@link PayslipRequest} with the given {@code employee},
     * {@link BigDecimal#ONE} as superannuation rate and {@link Month#JANUARY}.
     *
     * @param employee The {@link Employee} used in the {@link PayslipRequest}.
     * @return A {@link PayslipRequest}.
     */
    private static PayslipRequest createPayslipRequest(
        final Employee employee) {
        return new PayslipRequest(employee, BigDecimal.ONE, Month.JANUARY);
    }

    /**
     * Create a {@link Employee} with the given {@code forname},
     * {@link PayslipRequestTest#SURNAME} and {@link BigDecimal#TEN} as salary.
     *
     * @param forename The forename used in the {@link Employee}.
     * @return An {@link Employee}.
     */
    private static Employee createDiCaprioEmployee(final String forename) {
        return new Employee(forename, SURNAME, BigDecimal.TEN);
    }
}
