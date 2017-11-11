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
     * Leonardo's full name.
     */
    private static final String FULL_NAME =
        new StringBuilder(PayslipRequestTest.LEONARDO)
            .append(" ")
            .append(PayslipRequestTest.SURNAME)
            .toString();
    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#LEONARDO}.
     */
    private static final PayslipRequest LEONARDO_REQ =
        createRequestFor(PayslipRequestTest.LEONARDO);
    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#GEORGE}.
     */
    private static final PayslipRequest GEORGE_REQ =
        createRequestFor(PayslipRequestTest.GEORGE);
    /**
     * A {@link PayslipRequest} for the employee with the name {@link
     * PayslipRequestTest#GEORGE}, which should be equal to {@link
     * PayslipRequestTest#GEORGE_REQ}.
     */
    private static final PayslipRequest SAME_GEORGE_REQ =
        createRequestFor(PayslipRequestTest.GEORGE);
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
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getMonth())
            .equalTo(Month.JANUARY);
    }

    /**
     * Test for {@link PayslipRequest#getSuperRate()}.
     */
    @Test
    public final void getSuperRate() {
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getSuperRate())
            .equalTo(BigDecimal.ONE);
    }

    /**
     * Test for {@link PayslipRequest#getAnnualSalary()}.
     */
    @Test
    public final void getAnnualSalary() {
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getAnnualSalary())
            .equalTo(BigDecimal.TEN);
    }

    /**
     * Test for {@link PayslipRequest#getFullName()}.
     */
    @Test
    public final void getFullName() {
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getFullName())
            .equalTo(PayslipRequestTest.FULL_NAME);
    }

    /**
     * Test for {@link PayslipRequest#getSurname()}.
     */
    @Test
    public final void getSurname() {
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getSurname())
            .equalTo(PayslipRequestTest.SURNAME);
    }

    /**
     * Test for {@link PayslipRequest#getForename()}.
     */
    @Test
    public final void getForename() {
        new TestAssert<>(PayslipRequestTest.LEONARDO_REQ.getForename())
            .equalTo(PayslipRequestTest.LEONARDO);
    }

    /**
     * Test for {@link PayslipRequest#toString()}.
     */
    @Test
    public final void toStringTest() {
        new TestAssert<>(PayslipRequestTest.GEORGE_REQ.toString())
            .equalTo(PayslipRequestTest.GEORGE_LINE);
    }

    /**
     * Test for {@link PayslipRequest#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        final TestAssert<Integer> wrapper =
            new TestAssert<>(PayslipRequestTest.GEORGE_REQ.hashCode());
        wrapper.equalTo(PayslipRequestTest.SAME_GEORGE_REQ.hashCode());
        wrapper.notEqualTo(PayslipRequestTest.LEONARDO_REQ.hashCode());
    }

    /**
     * Tests for {@link PayslipRequest#equals(Object)}.
     */
    @Test
    public final void equalsCheck() {
        final TestAssert<PayslipRequest> wrapper =
            new TestAssert<>(PayslipRequestTest.GEORGE_REQ);
        wrapper.equalTo(PayslipRequestTest.SAME_GEORGE_REQ);
        wrapper.notEqualTo(PayslipRequestTest.LEONARDO_REQ);
    }

    /**
     * Parses the {@link PayslipRequestTest#GEORGE} request line and checks if
     * all getters return the right values.
     */
    @Test
    public final void parseTest() {
        final PayslipRequest request =
            new PayslipRequestParser(PayslipRequestTest.GEORGE_LINE)
            .toPayslipRequest();
        new TestAssert<>(request.getForename())
            .isSame(PayslipRequestTest.GEORGE);
        new TestAssert<>(request.getSurname())
            .isSame(PayslipRequestTest.SURNAME);
        new TestAssert<>(request.getAnnualSalary()).isSame(BigDecimal.TEN);
        new TestAssert<>(request.getSuperRate()).isSame(BigDecimal.ONE);
        new TestAssert<>(request.getMonth()).isSame(Month.JANUARY);
    }

    /**
     * Tests if a {@link PayslipException} with {@link
     * PayslipRequestParser#INVAL_MONTH} message is thrown when
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
     * PayslipRequestParser#INVAL_ELMNT_AMNT} message is thrown when
     * parsing lines with invalid elements or amounts of elements.
     *
     * @param line A line with invalid elements or amounts of elements.
     */
    @ParameterizedTest
    @ValueSource(strings = {" , , , , ", "Jennifer,Lawrence,1337,10.1%"})
    public final void parseInvalidArgumentAmount(final String line) {
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
     * Create a {@link Employee} with the given {@code forename},
     * {@link PayslipRequestTest#SURNAME} and {@link BigDecimal#TEN} as salary.
     *
     * @param forename The forename used in the {@link Employee}.
     * @return An {@link Employee}.
     */
    private static Employee createDiCaprioEmployee(final String forename) {
        return new Employee(
            forename,
            PayslipRequestTest.SURNAME,
            BigDecimal.TEN
        );
    }

}
