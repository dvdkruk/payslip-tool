/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import com.github.dvdkruk.payslip.TestAssert;
import java.time.Month;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
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
        final TestAssert<PayslipResult> wrapper =
            new TestAssert<>(PayslipResultTest.RESULT_EMMA);
        wrapper.equalTo(PayslipResultTest.SAME_RESULT_EMMA);
        wrapper.notEqualTo(PayslipResultTest.RESULT_DANIEL);
    }

    /**
     * Tests {@link PayslipResult#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        final TestAssert<Integer> wrapper =
            new TestAssert<>(RESULT_EMMA.hashCode());
        wrapper.isSame(SAME_RESULT_EMMA.hashCode());
        wrapper.isNotSame(RESULT_DANIEL.hashCode());
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
        new TestAssert<>(RESULT_EMMA.getMonth())
            .isSame(Month.FEBRUARY);
    }

    /**
     * Tests {@link PayslipResult#getSalary()}.
     */
    @Test
    public final void getGrossIncome() {
        new TestAssert<>(RESULT_EMMA.getSalary())
            .isSame(PayslipResultTest.SALARY);
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void getIncomeTax() {
        new TestAssert<>(RESULT_EMMA.getTax()).isSame(PayslipResultTest.TAX);
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

}
