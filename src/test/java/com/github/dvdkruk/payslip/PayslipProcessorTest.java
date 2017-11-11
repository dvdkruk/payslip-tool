/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip;

import com.github.dvdkruk.payslip.model.Employee;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipRequestParser;
import java.math.BigDecimal;
import java.time.Month;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for the {@link PayslipProcessor} class.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipProcessorTest {

    /**
     * A valid forename.
     */
    private static final String FORENAME = "Michael";

    /**
     * A valid surname.
     */
    private static final String SURNAME = "Jackson";

    /**
     * A valid {@link Employee} object.
     */
    private static final Employee EMPLOYEE = new Employee(
        PayslipProcessorTest.FORENAME,
        PayslipProcessorTest.SURNAME,
        BigDecimal.TEN
    );

    /**
     * A default {@link PayslipProcessor} object.
     */
    private static final PayslipProcessor PROCESSOR = new PayslipProcessor();

    /**
     * Checks that a {@link PayslipException} with message {@link
     * PayslipProcessor#INVAL_FORENAME} is thrown for invalid forenames.
     *
     * @param forename An invalid forename.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public final void invalidForenameTest(final String forename) {
        final Employee employee = new Employee(
            forename,
            PayslipProcessorTest.SURNAME,
            BigDecimal.TEN
        );
        checkPayslipExceptionMsg(PayslipProcessor.INVAL_FORENAME, employee);
    }

    /**
     * Checks that a {@link PayslipException} with message {@link
     * PayslipProcessor#INVAL_SURNAME} is thrown for invalid surnames.
     *
     * @param surname An invalid surname.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public final void invalidSurnameTest(final String surname) {
        final Employee employee = new Employee(
            PayslipProcessorTest.FORENAME,
            surname,
            BigDecimal.TEN
        );
        checkPayslipExceptionMsg(PayslipProcessor.INVAL_SURNAME, employee);
    }

    /**
     * Checks that a {@link PayslipException} with message {@link
     * PayslipProcessor#INVAL_SALARY} is thrown for invalid salaries.
     *
     * @param salary An invalid salary.
     */
    @ParameterizedTest
    @ValueSource(strings = {"-1", "0"})
    public final void invalidSalaryTest(final String salary) {
        final Employee employee =  new Employee(
            PayslipProcessorTest.FORENAME,
            PayslipProcessorTest.SURNAME,
            new BigDecimal(salary)
        );
        checkPayslipExceptionMsg(PayslipProcessor.INVAL_SALARY, employee);
    }

    /**
     * Checks that a {@link PayslipException} with message {@link
     * PayslipProcessor#INVAL_SUPER_RATE} is thrown for invalid superannuation
     * rates.
     *
     * @param rate An invalid superannuation rate.
     */
    @ParameterizedTest
    @ValueSource(strings = {"-1", "50.1"})
    public final void invalidSuperRateTest(final String rate) {
        final PayslipRequest request = new PayslipRequest(
            PayslipProcessorTest.EMPLOYEE,
            new BigDecimal(rate),
            Month.JANUARY
        );
        checkPayslipExceptionMsg(PayslipProcessor.INVAL_SUPER_RATE, request);
    }

    /**
     * Checks if {@code input} is processed by into {@code output}. Values are
     * provided by {@code PayslipProcessorExamples.csv} file.
     *
     * @param input String that needs to be processed.
     * @param output Expected output.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "PayslipProcessorExamples.csv")
    public final void validateRequest(final String input, final String output) {
        final PayslipRequest request = new PayslipRequestParser(input)
            .toPayslipRequest();
        new TestAssert<>(
            PayslipProcessorTest.PROCESSOR.process(request).toString()
        ).equalTo(output);
    }

    /**
     * Creates a {@link PayslipRequest} with the given {@code employee} and
     * tries to processes it and check if a {@link PayslipException} is thrown
     * with the {@code expected} message.
     *
     * @param expected Message that should be of the thrown
     *  {@link PayslipException}.
     * @param employee The {@link Employee} object used in the
     *  {@link PayslipRequest}.
     */
    private static void checkPayslipExceptionMsg(
        final String expected,
        final Employee employee) {
        final PayslipRequest request = new PayslipRequest(
            employee,
            BigDecimal.TEN,
            Month.JANUARY
        );
        checkPayslipExceptionMsg(expected, request);
    }

    /**
     * Tries to processes the given {@code request} and checks if a {@link
     * PayslipException} is thrown with the {@code expected} message.
     *
     * @param expected Message expected by the thrown {@link PayslipException}.
     * @param request The request that needs to processed.
     */
    private static void checkPayslipExceptionMsg(
        final String expected,
        final PayslipRequest request) {
        PayslipException exception = null;
        try {
            new PayslipProcessor().process(request);
        } catch (final PayslipException psx) {
            exception = psx;
        }
        assert exception != null;
        new TestAssert<>(exception.getMessage()).equalTo(expected);
    }

}
