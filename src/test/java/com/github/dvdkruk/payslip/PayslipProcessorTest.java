package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class PayslipProcessorTest {

    private final PayslipProcessor processor = new PayslipProcessor();

    @Test
    public void validateNullRequest() throws Exception {
        assertEquals("Request is null", processor.validate(null));
    }

    @Test
    public void validateFirstNameNull() throws Exception {
        PayslipRequest request = new PayslipRequest(null, null, BigDecimal.TEN, BigDecimal.TEN, Month.JANUARY);
        assertEquals("First name is null or empty", processor.validate(request));
    }

    @Test
    public void validateFirstNameEmpty() throws Exception {
        PayslipRequest request = new PayslipRequest("", "", BigDecimal.TEN, BigDecimal.TEN, Month.JANUARY);
        assertEquals("First name is null or empty", processor.validate(request));
    }

    @Test
    public void validateLastNameNull() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", null, BigDecimal.TEN, BigDecimal.TEN, Month.JANUARY);
        assertEquals("Last name is null or empty", processor.validate(request));
    }

    @Test
    public void validateNegativeSalary() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", "Jackson", new BigDecimal("-100"), BigDecimal.TEN, Month.JANUARY);
        assertEquals("Annual salary must be bigger than zero", processor.validate(request));
    }

    @Test
    public void validateZeroSalary() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", "Jackson", BigDecimal.ZERO, BigDecimal.TEN, Month.JANUARY);
        assertEquals("Annual salary must be bigger than zero", processor.validate(request));
    }

    @Test
    public void validateNegativeSuperRate() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", "Jackson", BigDecimal.ONE, new BigDecimal("-1"), Month.JANUARY);
        assertEquals("Super rate must be between 0% - 50%", processor.validate(request));
    }

    @Test
    public void validateSuperRateTooBig() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", "Jackson", BigDecimal.ONE, new BigDecimal("50.1"), Month.JANUARY);
        assertEquals("Super rate must be between 0% - 50%", processor.validate(request));
    }

    @Test
    public void validateRequest() throws Exception {
        PayslipRequest request = new PayslipRequest("Michael", "Jackson", BigDecimal.ONE, new BigDecimal("50"), Month.DECEMBER);
        assertEquals("", processor.validate(request));
    }

    @Test
    public void processExample0() throws Exception {
        PayslipRequest request = PayslipRequest.parse("David,Rudd,60050,9%,March");
        PayslipResult result = processor.process(request);
        assertEquals("David Rudd", result.getFullName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(5004, result.getGrossIncome());
        assertEquals(922, result.getIncomeTax());
        assertEquals(4082, result.getNetIncome());
        assertEquals(450, result.getMonthlySuper());
    }

    @Test
    public void processExample1() throws Exception {
        PayslipRequest request = PayslipRequest.parse("Ryan,Chen,120000,10%,March");
        PayslipResult result = processor.process(request);
        assertEquals("Ryan Chen", result.getFullName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(10000, result.getGrossIncome());
        assertEquals(2696, result.getIncomeTax());
        assertEquals(7304, result.getNetIncome());
        assertEquals(1000, result.getMonthlySuper());
    }

    @Test
    public void nonTaxableSalary() {
        PayslipRequest request = PayslipRequest.parse("Ryan,Chen,18200,1%,March");
        PayslipResult result = processor.process(request);
        assertEquals(1517, result.getGrossIncome());
        assertEquals(0, result.getIncomeTax());
        assertEquals(1517, result.getNetIncome());
        assertEquals(15, result.getMonthlySuper());
    }

    @Test
    public void highestTaxableSalary() {
        PayslipRequest request = PayslipRequest.parse("Ryan,Chen,180001,50%,March");
        PayslipResult result = processor.process(request);
        assertEquals(15000, result.getGrossIncome());
        assertEquals(4546, result.getIncomeTax());
        assertEquals(10454, result.getNetIncome());
        assertEquals(7500, result.getMonthlySuper());
    }

}