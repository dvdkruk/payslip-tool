package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.Employee;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipRequestParser;
import com.github.dvdkruk.payslip.model.PayslipResult;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class PayslipProcessorTest {

    private static final BigDecimal NEGATIVE = new BigDecimal("-100");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final PayslipProcessor processor = new PayslipProcessor();

    @Test
    public void validateFirstNameNull() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee(null, null, BigDecimal.TEN),
            BigDecimal.TEN,
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("First name is null or empty");
        processor.process(request);
    }

    @Test
    public void validateFirstNameEmpty() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("", "", BigDecimal.TEN),
            BigDecimal.TEN,
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("First name is null or empty");
        processor.process(request);
    }

    @Test
    public void validateLastNameNull() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("Michael", null, BigDecimal.TEN),
            BigDecimal.TEN,
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Last name is null or empty");
        processor.process(request);
    }

    @Test
    public void validateNegativeSalary() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee(
                "Michael",
                "Jackson",
                PayslipProcessorTest.NEGATIVE
            ),
            BigDecimal.TEN,
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Salary must be bigger than zero");
        processor.process(request);
    }

    @Test
    public void validateZeroSalary() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("Michael", "Jackson", BigDecimal.ZERO),
            BigDecimal.TEN,
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Salary must be bigger than zero");
        processor.process(request);
    }

    @Test
    public void validateNegativeSuperRate() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("Michael", "Jackson", BigDecimal.ONE),
            new BigDecimal("-1"),
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Super rate must be between 0% - 50%");
        processor.process(request);
    }

    @Test
    public void validateSuperRateTooBig() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("Michael", "Jackson", BigDecimal.ONE),
            new BigDecimal("50.1"),
            Month.JANUARY
        );
        thrown.expect(PayslipException.class);
        thrown.expectMessage("Super rate must be between 0% - 50%");
        processor.process(request);
    }

    @Test
    public void validateRequest() throws Exception {
        PayslipRequest request = new PayslipRequest(
            new Employee("Michael", "Jackson", BigDecimal.ONE),
            new BigDecimal("50"),
            Month.DECEMBER
        );
        PayslipResult result = processor.process(request);
        assertEquals("Michael Jackson", result.getName());
        assertEquals(Month.DECEMBER, result.getMonth());
    }

    @Test
    public void processExample0() throws Exception {
        PayslipRequest request = new PayslipRequestParser(
            "David,Rudd,60050,9%,March"
        ).toPayslipRequest();
        PayslipResult result = processor.process(request);
        assertEquals("David Rudd", result.getName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(5004, result.getSalary());
        assertEquals(922, result.getTax());
        assertEquals(4082, result.getNetIncome());
        assertEquals(450, result.getSuperannuation());
    }

    @Test
    public void processExample1() throws Exception {
        PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,120000,10%,March"
        ).toPayslipRequest();
        PayslipResult result = processor.process(request);
        assertEquals("Ryan Chen", result.getName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(10000, result.getSalary());
        assertEquals(2696, result.getTax());
        assertEquals(7304, result.getNetIncome());
        assertEquals(1000, result.getSuperannuation());
    }

    @Test
    public void nonTaxableSalary() {
        PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,18200,1%,March"
        ).toPayslipRequest();
        PayslipResult result = processor.process(request);
        assertEquals(1517, result.getSalary());
        assertEquals(0, result.getTax());
        assertEquals(1517, result.getNetIncome());
        assertEquals(15, result.getSuperannuation());
    }

    @Test
    public void highestTaxableSalary() {
        PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,180001,50%,March"
        ).toPayslipRequest();
        PayslipResult result = processor.process(request);
        assertEquals(15000, result.getSalary());
        assertEquals(4546, result.getTax());
        assertEquals(10454, result.getNetIncome());
        assertEquals(7500, result.getSuperannuation());
    }

}