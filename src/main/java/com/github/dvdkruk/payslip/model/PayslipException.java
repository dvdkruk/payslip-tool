package com.github.dvdkruk.payslip.model;

public class PayslipException extends IllegalArgumentException {

    public PayslipException(String message) {
        super(message);
    }

    PayslipException(String message, Throwable cause) {
        super(message, cause);
    }
}
