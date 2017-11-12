/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A builder to create comma separated strings.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
final class CommaSeparatedStringBuilder {

    /**
     * Default size for {@link  CommaSeparatedStringBuilder#elements}.
     */
    private static final int DEFAULT_SIZE = 5;

    /**
     * Create an comma separated string for these elements.
     */
    private final List<String> elements;

    /**
     * Create a {@link CommaSeparatedStringBuilder}.
     */
    CommaSeparatedStringBuilder() {
        this.elements =
            new ArrayList<>(CommaSeparatedStringBuilder.DEFAULT_SIZE);
    }

    /**
     * Appends {@code element} to the comma separated string.
     *
     * @param element Append this string to the comma separated string.
     * @return This builder.
     */
    public CommaSeparatedStringBuilder append(final String element) {
        this.elements.add(element);
        return this;
    }

    /**
     * Appends {@code element} to the comma separated string.
     *
     * @param element Append this value to the comma separated string.
     * @return This builder.
     */
    public CommaSeparatedStringBuilder append(final int element) {
        return this.append(String.valueOf(element));
    }

    @Override
    public String toString() {
        return this.elements.stream().collect(Collectors.joining(","));
    }
}
