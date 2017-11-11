/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.time.Month;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Test;

/**
 * Unit tests for {@link PayslipResult}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipResultTest {
    /**
     * An example {@link PayslipResult} for Emma Stone.
     */
    private static final PayslipResult RESULT_EMMA =
        createPayslipResult(PayslipResultTest.FULL_NAME_EMMA);
    /**
     * An example {@link PayslipResult} for Emma Stone, should be equal to
     * {@link PayslipResultTest#RESULT_EMMA}.
     */
    private static final PayslipResult SAME_RESULT_EMMA =
        createPayslipResult(PayslipResultTest.FULL_NAME_EMMA);
    /**
     * An example {@link PayslipResult} for Daniel Craig.
     */
    private static final PayslipResult RESULT_DANIEL =
        createPayslipResult("Daniel Craig");
    /**
     * Fore- and surname of Emma Stone.
     */
    private static final String FULL_NAME_EMMA = "Emma Stone";
    /**
     * An example monthly salary.
     */
    private static final int SALARY = 5004;
    /**
     * An example income tax amount.
     */
    private static final int TAX = 992;
    /**
     * An example monthly superannuation contribution.
     */
    private static final int SUPER = 450;

    /**
     * Tests {@link PayslipResult#equals(Object)}.
     */
    @Test
    public final void equalsCheck() {
        equalsCheck(
            PayslipResultTest.RESULT_EMMA,
            PayslipResultTest.SAME_RESULT_EMMA
        );
        notEqualsCheck(
            PayslipResultTest.RESULT_EMMA,
            PayslipResultTest.RESULT_DANIEL
        );
    }

    /**
     * Tests {@link PayslipResult#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        MatcherAssert.assertThat(
            RESULT_EMMA.hashCode(),
            Is.is(SAME_RESULT_EMMA.hashCode())
        );
        MatcherAssert.assertThat(
            RESULT_EMMA.hashCode(),
            IsNot.not(RESULT_DANIEL.hashCode())
        );
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void toStringCheck() {
        MatcherAssert.assertThat(
            RESULT_EMMA.toString(),
            Is.is("Emma Stone,01 February - 28 February,5004,992,4012,450")
        );
    }

    /**
     * Tests {@link PayslipResult#getName()}.
     */
    @Test
    public final void getFullName() {
        MatcherAssert.assertThat(
            RESULT_EMMA.getName(),
            Is.is(PayslipResultTest.FULL_NAME_EMMA)
        );
    }

    /**
     * Tests {@link PayslipResult#getMonth()}.
     */
    @Test
    public final void getMonth() {
        MatcherAssert.assertThat(RESULT_EMMA.getMonth(), Is.is(Month.FEBRUARY));
    }

    /**
     * Tests {@link PayslipResult#getSalary()}.
     */
    @Test
    public final void getGrossIncome() {
        MatcherAssert.assertThat(
            RESULT_EMMA.getSalary(),
            Is.is(PayslipResultTest.SALARY)
        );
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void getIncomeTax() {
        MatcherAssert.assertThat(
            RESULT_EMMA.getTax(),
            Is.is(PayslipResultTest.TAX)
        );
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void getNetIncome() {
        final int expected = PayslipResultTest.SALARY - PayslipResultTest.TAX;
        MatcherAssert.assertThat(RESULT_EMMA.getNetIncome(), Is.is(expected));
    }

    /**
     * Tests {@link PayslipResult#getSuperannuation()}.
     */
    @Test
    public final void getMonthlySuper() {
        final int superann = PayslipResultTest.RESULT_EMMA.getSuperannuation();
        MatcherAssert.assertThat(superann, Is.is(PayslipResultTest.SUPER));
    }

    /**
     * Create a {@link PayslipResult} with the given {@code name}.
     * @param name The name used in the {@link PayslipResult}.
     * @return A {@link PayslipResult}.
     */
    private static PayslipResult createPayslipResult(final String name) {
        return new PayslipResult(
            name,
            Month.FEBRUARY,
            createFinancialInformation()
        );
    }

    /**
     * Creates an example {@link FinancialInformation} result set.
     * @return An example {@link FinancialInformation} result set.
     */
    private static FinancialInformation createFinancialInformation() {
        return new FinancialInformation(
            PayslipResultTest.SALARY,
            PayslipResultTest.TAX,
            PayslipResultTest.SUPER
        );
    }

    /**
     * Checks if {@code obj} is not equal to {@code that}.
     *
     * @param obj Object used in the not equals test.
     * @param that Another object used in the not equals test.
     */
    private static void notEqualsCheck(final Object obj, final Object that) {
        MatcherAssert.assertThat(obj, IsNot.not(IsEqual.equalTo(that)));
        MatcherAssert.assertThat(that, IsNot.not(IsEqual.equalTo(obj)));
    }

    /**
     * Checks if {@code obj} is equal to {@code that}.
     *
     * @param obj Object used in the equals test.
     * @param that Another object used in the equals test.
     */
    private static void equalsCheck(final Object obj, final Object that) {
        MatcherAssert.assertThat(obj, IsEqual.equalTo(that));
        MatcherAssert.assertThat(that, IsEqual.equalTo(obj));
    }

}
