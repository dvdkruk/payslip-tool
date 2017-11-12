/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.TestAssert;
import java.math.BigDecimal;
import java.time.Month;
import org.junit.Test;

/**
 * Unit tests for {@link PayslipRequest}.
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
        new TestAssert<>(LEONARDO_REQ.getMonth()).equalTo(Month.JANUARY);
    }

    /**
     * Test for {@link PayslipRequest#getSuperRate()}.
     */
    @Test
    public final void getSuperRate() {
        new TestAssert<>(LEONARDO_REQ.getSuperRate()).equalTo(BigDecimal.ONE);
    }

    /**
     * Test for {@link Employee#getAnnualSalary()}.
     */
    @Test
    public final void getAnnualSalary() {
        new TestAssert<>(LEONARDO_REQ.getEmployee().getAnnualSalary())
            .equalTo(BigDecimal.TEN);
    }

    /**
     * Test for {@link Employee#getFullName()}.
     */
    @Test
    public final void getFullName() {
        new TestAssert<>(LEONARDO_REQ.getEmployee().getFullName())
            .equalTo(PayslipRequestTest.FULL_NAME);
    }

    /**
     * Test for {@link Employee#getSurname()}.
     */
    @Test
    public final void getSurname() {
        new TestAssert<>(LEONARDO_REQ.getEmployee().getSurname())
            .equalTo(PayslipRequestTest.SURNAME);
    }

    /**
     * Test for {@link Employee#getForename()}.
     */
    @Test
    public final void getForename() {
        new TestAssert<>(LEONARDO_REQ.getEmployee().getForename())
            .equalTo(PayslipRequestTest.LEONARDO);
    }

    /**
     * Test for {@link PayslipRequest#toString()}.
     */
    @Test
    public final void toStringTest() {
        new TestAssert<>(GEORGE_REQ.toString())
            .equalTo(PayslipRequestTest.GEORGE_LINE);
    }

    /**
     * Test for {@link PayslipRequest#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        final TestAssert<Integer> request =
            new TestAssert<>(GEORGE_REQ.hashCode());
        request.equalTo(SAME_GEORGE_REQ.hashCode());
        request.notEqualTo(LEONARDO_REQ.hashCode());
    }

    /**
     * Tests for {@link PayslipRequest#equals(Object)}.
     */
    @Test
    public final void equalsCheck() {
        final TestAssert<PayslipRequest> request =
            new TestAssert<>(PayslipRequestTest.GEORGE_REQ);
        request.equalTo(PayslipRequestTest.SAME_GEORGE_REQ);
        request.notEqualTo(PayslipRequestTest.LEONARDO_REQ);
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
