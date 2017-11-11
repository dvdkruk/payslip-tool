package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.Employee;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipRequestParser;
import com.github.dvdkruk.payslip.model.PayslipResult;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PayslipProcessorTest {

    private static final String FORENAME = "Michael";

    private static final String SURNAME = "Jackson";

    private static final Employee EMPLOYEE = new Employee(
        PayslipProcessorTest.FORENAME,
        PayslipProcessorTest.SURNAME,
        BigDecimal.TEN
    );

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final PayslipProcessor processor = new PayslipProcessor();

    @ParameterizedTest
    @ValueSource( strings = {"", " "})
    public final void invalidForenameTest(final String forename) {
        this.checkPayslipExceptionMessage(
            PayslipProcessor.INVALID_FORENAME,
            new Employee(forename, PayslipProcessorTest.SURNAME, BigDecimal.TEN)
        );
    }

    @ParameterizedTest
    @ValueSource( strings = {"", " "})
    public final void invalidSurnameTest(final String surname) {
        this.checkPayslipExceptionMessage(
            PayslipProcessor.INVALID_SURNAME,
            new Employee(PayslipProcessorTest.FORENAME, surname, BigDecimal.TEN)
        );
    }

    @ParameterizedTest
    @ValueSource( strings = {"-1", "0"})
    public final void invalidSalaryTest(final String salary) {
        this.checkPayslipExceptionMessage(
            PayslipProcessor.INVALID_SALARY,
            new Employee(
                PayslipProcessorTest.FORENAME,
                PayslipProcessorTest.SURNAME,
                new BigDecimal(salary)
            )
        );
    }

    @ParameterizedTest
    @ValueSource( strings = {"-1", "50.1"})
    public final void invalidSuperRateTest(final String rate) {
        checkPayslipExceptionMessage(
            PayslipProcessor.INVALID_SUPER_RATE,
            new PayslipRequest(
                PayslipProcessorTest.EMPLOYEE,
                new BigDecimal(rate),
                Month.JANUARY
            )
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'David,Rudd,60050,9%,March','David Rudd,March,5004,922,4082,450'",
    })
    public final void validateRequest(final String input, final String output) {
        final PayslipRequest request = new PayslipRequestParser(input)
            .toPayslipRequest();
        final PayslipResult result = this.processor.process(request);
        assertEquals(output, result.toString());
    }

    @Test
    public final void validateRequest() throws Exception {
        final PayslipRequest request = new PayslipRequest(
            new Employee("Michael", "Jackson", BigDecimal.ONE),
            new BigDecimal("50"),
            Month.DECEMBER
        );
        final PayslipResult result = this.processor.process(request);
        assertEquals("Michael Jackson", result.getName());
        assertEquals(Month.DECEMBER, result.getMonth());
    }

    @Test
    public final void processExample0() throws Exception {
        final PayslipRequest request = new PayslipRequestParser(
            "David,Rudd,60050,9%,March"
        ).toPayslipRequest();
        final PayslipResult result = this.processor.process(request);
        assertEquals("David Rudd", result.getName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(5004, result.getSalary());
        assertEquals(922, result.getTax());
        assertEquals(4082, result.getNetIncome());
        assertEquals(450, result.getSuperannuation());
    }

    @Test
    public final void processExample1() throws Exception {
        final PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,120000,10%,March"
        ).toPayslipRequest();
        final PayslipResult result = this.processor.process(request);
        assertEquals("Ryan Chen", result.getName());
        assertEquals(Month.MARCH, result.getMonth());
        assertEquals(10000, result.getSalary());
        assertEquals(2696, result.getTax());
        assertEquals(7304, result.getNetIncome());
        assertEquals(1000, result.getSuperannuation());
    }

    @Test
    public final void nonTaxableSalary() {
        final PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,18200,1%,March"
        ).toPayslipRequest();
        final PayslipResult result = this.processor.process(request);
        assertEquals(1517, result.getSalary());
        assertEquals(0, result.getTax());
        assertEquals(1517, result.getNetIncome());
        assertEquals(15, result.getSuperannuation());
    }

    @Test
    public final void highestTaxableSalary() {
        final PayslipRequest request = new PayslipRequestParser(
            "Ryan,Chen,180001,50%,March"
        ).toPayslipRequest();
        final PayslipResult result = this.processor.process(request);
        assertEquals(15000, result.getSalary());
        assertEquals(4546, result.getTax());
        assertEquals(10454, result.getNetIncome());
        assertEquals(7500, result.getSuperannuation());
    }

    private void checkPayslipExceptionMessage(
        final String expected,
        final Employee employee) {
        final PayslipRequest request = new PayslipRequest(
            employee,
            BigDecimal.TEN,
            Month.JANUARY
        );
        checkPayslipExceptionMessage(expected, request);
    }

    private void checkPayslipExceptionMessage(
        final String expected,
        final PayslipRequest request) {
        PayslipException exception = null;
        try {
            this.processor.process(request);
        } catch (final PayslipException psx) {
            exception = psx;
        }
        assertNotNull(exception);
        assertEquals(expected, exception.getMessage());
    }

}
