package com.github.dvdkruk.payslip.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PayslipRequestTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final PayslipRequest leonardo = new PayslipRequest(
        new Employee(
            "Leonardo",
            "DiCaprio",
            new BigDecimal("25000000")
        ),
        new BigDecimal("50"),
        Month.DECEMBER
    );
    private final PayslipRequest meryl0 = new PayslipRequest(
        new Employee(
            "Meryl",
            "Streep",
            new BigDecimal("10000000")
        ),
        new BigDecimal("20"),
        Month.JANUARY
    );
    private final PayslipRequest meryl1 = new PayslipRequest(
        new Employee(
            "Meryl",
            "Streep",
            new BigDecimal("10000000")
        ),
        new BigDecimal("20"),
        Month.JANUARY
    );

    @Test
    public void normalRequest() throws Exception {
        assertEquals("Leonardo", leonardo.getForename());
        assertEquals("DiCaprio", leonardo.getSurname());
        assertEquals("Leonardo DiCaprio", leonardo.getFullName());
        assertEquals(new BigDecimal("25000000"), leonardo.getAnnualSalary());
        assertEquals(new BigDecimal("50"), leonardo.getSuperRate());
        assertEquals(Month.DECEMBER, leonardo.getMonth());
    }

    @Test
    public void testToString() {
        assertEquals("Meryl,Streep,10000000,20%,January", meryl0.toString());
    }

    @Test
    public void hashCodeCheck() {
        assertEquals(meryl0.hashCode(), meryl1.hashCode());
        assertNotEquals(meryl0.hashCode(), leonardo.hashCode());
    }

    @Test
    public void equalsCheck() {
        assertEquals(meryl0, meryl1);
        assertEquals(meryl1, meryl0);
        assertNotEquals(meryl0, leonardo);
    }

    @Test
    public void parseTest() {
        PayslipRequestParser parser = new PayslipRequestParser("Jennifer,Lawrence,1337,10.1%,March");
        PayslipRequest jennifer = parser.toPayslipRequest();
        assertEquals("Jennifer", jennifer.getForename());
        assertEquals("Lawrence", jennifer.getSurname());
        assertEquals(new BigDecimal("1337"), jennifer.getAnnualSalary());
        assertEquals(new BigDecimal("10.1"), jennifer.getSuperRate());
        assertEquals(Month.MARCH, jennifer.getMonth());
    }

    @Test
    public void parseInvalidMonth() {
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Peter is an invalid month");
        new PayslipRequestParser("Jennifer,Lawrence,1337,10.1%,Peter").toPayslipRequest();
    }

    @Test
    public void parseInvalidEmptyArguments() {
        thrown.expect(PayslipException.class);
        thrown.expectMessage("a payslip request must consist of 5 (non empty) elements");
        new PayslipRequestParser(" , , , , ").toPayslipRequest();
    }

    @Test
    public void parseInvalidArgumentAmount() {
        thrown.expect(PayslipException.class);
        thrown.expectMessage("a payslip request must consist of 5 (non empty) elements");
        new PayslipRequestParser("Jennifer,Lawrence,1337,10.1%").toPayslipRequest();
    }
}
