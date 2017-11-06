package com.github.dvdkruk.payslip.model;

public class PayslipException extends IllegalArgumentException {

    public PayslipException(final String message) {
        super(message);
    }

    PayslipException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
