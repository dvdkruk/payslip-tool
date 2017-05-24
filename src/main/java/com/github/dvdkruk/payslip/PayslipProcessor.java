package com.github.dvdkruk.payslip;


import com.github.dvdkruk.payslip.model.IncomeTaxRule;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Payslip processor - Processes {@code PayslipRequest} instance to {@code PayslipResult}.
 */
public class PayslipProcessor {

    private static final BigDecimal AMOUNT_OF_MONTHS = new BigDecimal("12");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal MIN_SUPER_RATE = new BigDecimal("0");
    private static final BigDecimal MAX_SUPER_RATE = new BigDecimal("50");
    private final List<IncomeTaxRule> taxRules = new ArrayList<>();

    public PayslipProcessor() {
        //init tax rules
        taxRules.add(new IncomeTaxRule(18200, 0, BigDecimal.ZERO));
        taxRules.add(new IncomeTaxRule(37000, 0, new BigDecimal("0.190")));
        taxRules.add(new IncomeTaxRule(80000, 3572, new BigDecimal("0.325")));
        taxRules.add(new IncomeTaxRule(180000, 17547, new BigDecimal("0.37")));
        taxRules.add(new IncomeTaxRule(Integer.MAX_VALUE, 54547, new BigDecimal("0.45")));
    }

    /**
     * Processes the request argument as a payslip result.
     *
     * @param request a payslip request.
     * @return the result of the request argument.
     * @throws PayslipException if the request is not valid.
     */
    public PayslipResult process(PayslipRequest request) {
        validate(request);

        int grossIncome = request.getAnnualSalary().divide(AMOUNT_OF_MONTHS, 0, RoundingMode.HALF_UP).intValueExact();
        int incomeTax = calculateIncomeTax(request.getAnnualSalary().intValueExact());
        int monthlySuper = calculateMonthlySuper(grossIncome, request.getSuperRate());

        return new PayslipResult(request.getFullName(), request.getMonth(), grossIncome, incomeTax, monthlySuper);
    }

    private void validate(PayslipRequest request) {
        if (request == null) {
            throw new PayslipException("Request is null");
        }
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new PayslipException("First name is null or empty");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new PayslipException("Last name is null or empty");
        }
        if (request.getAnnualSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayslipException("Annual salary must be bigger than zero");
        }
        if (request.getSuperRate() == null) {
            throw new PayslipException("Super rate is null");
        }
        if (request.getSuperRate().compareTo(MIN_SUPER_RATE) < 0 || request.getSuperRate().compareTo(MAX_SUPER_RATE) > 0) {
            throw new PayslipException("Super rate must be between 0% - 50%");
        }
    }

    private int calculateMonthlySuper(int grossIncome, BigDecimal superRate) {
        BigDecimal monthlySuper = superRate.divide(HUNDRED, 99, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(grossIncome));
        return monthlySuper.toBigInteger().intValueExact();
    }

    private int calculateIncomeTax(int annualSalary) {
        int previousRange = 0;
        for (IncomeTaxRule taxRule : taxRules) {
            if (annualSalary <= taxRule.getMaxRange()) {
                BigDecimal taxableOverBase = BigDecimal.valueOf(annualSalary - (long) previousRange);
                BigDecimal incomeTax = taxableOverBase.multiply(taxRule.getTaxPerDollar())
                        .add(BigDecimal.valueOf(taxRule.getBaseTax()))
                        .divide(AMOUNT_OF_MONTHS, 0, RoundingMode.HALF_UP);
                return incomeTax.intValueExact();
            }
            previousRange = taxRule.getMaxRange();
        }
        throw new NoSuchElementException("No tax rule found for annual salary '" + annualSalary + "'");
    }
}
