package com.github.dvdkruk.payslip.model;

import org.junit.Test;

import java.time.Month;

import static org.junit.Assert.*;

public class PayslipResultTest {

    private final PayslipResult emma0 = new PayslipResult(
        "Emma Stone",
        Month.FEBRUARY,
        new FinancialInformation(5004, 922, 450)
    );
    private final PayslipResult emma1 = new PayslipResult(
        "Emma Stone",
        Month.FEBRUARY,
        new FinancialInformation(5004, 922, 450)
    );
    private final PayslipResult daniel0 = new PayslipResult(
        "Daniel Craig",
        Month.FEBRUARY,
        new FinancialInformation(5004, 922, 450)
    );

    @Test
    public void equals() throws Exception {
        assertTrue(emma0.equals(emma1));
        assertTrue(emma1.equals(emma0));
        assertFalse(emma0.equals(daniel0));
    }

    @Test
    public void hashCodeCheck() throws Exception {
        assertEquals(emma0.hashCode(), emma1.hashCode());
        assertNotEquals(emma0.hashCode(), daniel0.hashCode());
    }

    @Test
    public void toStringCheck() throws Exception {
        assertEquals("Emma Stone,01 February - 28 February,5004,922,4082,450", emma0.toString());
    }

    @Test
    public void getFullName() throws Exception {
        assertEquals("Emma Stone", emma0.getName());
    }

    @Test
    public void getMonth() throws Exception {
        assertEquals(Month.FEBRUARY, emma0.getMonth());
    }

    @Test
    public void getGrossIncome() throws Exception {
        assertEquals(5004, emma0.getSalary());
    }

    @Test
    public void getIncomeTax() throws Exception {
        assertEquals(922, emma0.getTax());
    }

    @Test
    public void getNetIncome() throws Exception {
        assertEquals(5004 - 922, emma0.getNetIncome());
    }

    @Test
    public void getMonthlySuper() throws Exception {
        assertEquals(450, emma0.getSuperannuation());
    }

}