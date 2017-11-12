/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.model;

/**
 * A general exception for identifying errors in the Payslip tool.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class PayslipException extends IllegalArgumentException {

    /**
     * Payslip exception constructor.
     *
     * @param message Describing the exception.
     */
    public PayslipException(final String message) {
        super(message);
    }

    /**
     * Payslip exception constructor.
     *
     * @param message Describing the exception.
     * @param cause The underlying case of this exception.
     */
    PayslipException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
