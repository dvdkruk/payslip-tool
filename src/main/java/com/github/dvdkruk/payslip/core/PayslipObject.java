/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import com.github.dvdkruk.payslip.utils.CommaSeparatedStringBuilder;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * PayslipObject.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
class PayslipObject {

    /**
     * Values.
     */
    private final List<Object> values;

    /**
     * PayslipObject.
     * @param values Values.
     */
    PayslipObject(final Object... values) {
        this.values = Arrays.asList(values);
    }

    @Override
    public String toString() {
        final CommaSeparatedStringBuilder builder =
            new CommaSeparatedStringBuilder();
        this.values.forEach(val -> builder.append(String.valueOf(val)));
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean equals;
        if (this == obj)  {
            equals = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            equals = false;
        } else {
            final PayslipObject that = (PayslipObject) obj;
            equals = Objects.equals(this.values, that.values);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.values.toArray());
    }

    /**
     * A {@link DecimalFormat} with {@code max} set as maximum fraction digits
     * amount.
     *
     * @param max Maximum fraction digits amount.
     * @return A {@link DecimalFormat}.
     */
    protected static DecimalFormat createDecimalFormat(final int max) {
        final DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(max);
        format.setMinimumFractionDigits(0);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format;
    }

    /**
     * Full display month name in English.
     * @param month Month.
     * @return Full display month name in English.
     */
    protected static String toDisplayMonth(final Month month) {
        return month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
