package com.github.dvdkruk.payslip.model;

public class PayslipRequestFormatException extends IllegalArgumentException {

    PayslipRequestFormatException(String message) {
        super(message);
    }

    PayslipRequestFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
