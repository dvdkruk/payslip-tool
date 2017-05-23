package com.github.dvdkruk.payslip.model;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Represent a payslip result - Result of a successfully processed {@code PayslipRequest} instance;
 */
public class PayslipResult {

    private static final String SEPARATOR = ",";

    private final String fullName;

    private final Month month;

    private final int grossIncome;

    private final int incomeTax;

    private final int netIncome;

    private final int monthlySuper;

    private final String displayString;

    public PayslipResult(String fullName, Month month, int grossIncome, int incomeTax, int monthlySuper) {
        this.fullName = fullName;
        this.month = month;
        this.grossIncome = grossIncome;
        this.incomeTax = incomeTax;
        this.netIncome = grossIncome - incomeTax;
        this.monthlySuper = monthlySuper;

        String monthDisplayName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        displayString = fullName +
                SEPARATOR + "01 " + monthDisplayName +
                " - " + month.length(Year.now().isLeap()) +
                " " + monthDisplayName +
                SEPARATOR + grossIncome +
                SEPARATOR + incomeTax +
                SEPARATOR + netIncome +
                SEPARATOR + monthlySuper;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof PayslipResult) {
            PayslipResult result = (PayslipResult) obj;
            return fullName.equals(result.fullName)
                    && month.equals(result.month)
                    && grossIncome == result.grossIncome
                    && incomeTax == result.incomeTax
                    && monthlySuper == result.monthlySuper;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, month, grossIncome, incomeTax, monthlySuper);
    }

    @Override
    public String toString() {
        return displayString;
    }

    public String getFullName() {
        return fullName;
    }

    public Month getMonth() {
        return month;
    }

    public int getGrossIncome() {
        return grossIncome;
    }

    public int getIncomeTax() {
        return incomeTax;
    }

    public int getNetIncome() {
        return netIncome;
    }

    public int getMonthlySuper() {
        return monthlySuper;
    }
}

