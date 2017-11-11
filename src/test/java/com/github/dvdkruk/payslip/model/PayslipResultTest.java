/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

import java.time.Month;
import org.junit.Assert;
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
    public final void equals() {
        Assert.assertTrue(RESULT_EMMA.equals(SAME_RESULT_EMMA));
        Assert.assertTrue(SAME_RESULT_EMMA.equals(RESULT_EMMA));
        Assert.assertFalse(RESULT_EMMA.equals(RESULT_DANIEL));
    }

    /**
     * Tests {@link PayslipResult#hashCode()}.
     */
    @Test
    public final void hashCodeCheck() {
        Assert.assertEquals(
            RESULT_EMMA.hashCode(),
            SAME_RESULT_EMMA.hashCode()
        );
        Assert.assertNotEquals(
            RESULT_EMMA.hashCode(),
            RESULT_DANIEL.hashCode()
        );
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void toStringCheck() {
        Assert.assertEquals(
            "Emma Stone,01 February - 28 February,5004,992,4012,450",
            RESULT_EMMA.toString()
        );
    }

    /**
     * Tests {@link PayslipResult#getName()}.
     */
    @Test
    public final void getFullName() {
        Assert.assertEquals(
            PayslipResultTest.FULL_NAME_EMMA,
            RESULT_EMMA.getName()
        );
    }

    /**
     * Tests {@link PayslipResult#getMonth()}.
     */
    @Test
    public final void getMonth() {
        Assert.assertEquals(Month.FEBRUARY, RESULT_EMMA.getMonth());
    }

    /**
     * Tests {@link PayslipResult#getSalary()}.
     */
    @Test
    public final void getGrossIncome() {
        Assert.assertEquals(PayslipResultTest.SALARY, RESULT_EMMA.getSalary());
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void getIncomeTax() {
        Assert.assertEquals(PayslipResultTest.TAX, RESULT_EMMA.getTax());
    }

    /**
     * Tests {@link PayslipResult#getNetIncome()}.
     */
    @Test
    public final void getNetIncome() {
        final int expected = PayslipResultTest.SALARY - PayslipResultTest.TAX;
        Assert.assertEquals(expected, RESULT_EMMA.getNetIncome());
    }

    /**
     * Tests {@link PayslipResult#getSuperannuation()}.
     */
    @Test
    public final void getMonthlySuper() {
        Assert.assertEquals(
            PayslipResultTest.SUPER,
            RESULT_EMMA.getSuperannuation()
        );
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
