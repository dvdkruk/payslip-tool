/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.utils;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CommaSeparatedStringBuilder}.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class CommaSeparatedStringBuilderTest {

    /**
     * A single element string.
     */
    private static final String ONE_ELEMENT = "test";

    /**
     * A two elements string.
     */
    private static final String TWO_ELEMENTS = "test,test";

    /**
     * A {@link CommaSeparatedStringBuilder}.
     */
    private CommaSeparatedStringBuilder builder;

    /**
     * Initializes a {@link CommaSeparatedStringBuilder} for every test.
     */
    @BeforeEach
    public void initialize() {
        this.builder = new CommaSeparatedStringBuilder();
    }

    /**
     * Tests string building of one string.
     */
    @Test
    public void appendOneString() {
        this.builder.append(CommaSeparatedStringBuilderTest.ONE_ELEMENT);
        final Matcher<String> expected =
            IsEqual.equalTo(CommaSeparatedStringBuilderTest.ONE_ELEMENT);
        MatcherAssert.assertThat(this.builder.toString(), expected);
    }

    /**
     * Tests string build of multiple strings.
     */
    @Test
    public void appendMultipleStrings() {
        this.builder.append(CommaSeparatedStringBuilderTest.ONE_ELEMENT);
        this.builder.append(CommaSeparatedStringBuilderTest.ONE_ELEMENT);
        final Matcher<String> expected =
            IsEqual.equalTo(CommaSeparatedStringBuilderTest.TWO_ELEMENTS);
        MatcherAssert.assertThat(this.builder.toString(), expected);
    }

    /**
     * Tests string build of an int.
     */
    @Test
    public void appendInt() {
        this.builder.append(0);
        final Matcher<String> expected = IsEqual.equalTo("0");
        MatcherAssert.assertThat(this.builder.toString(), expected);
    }
}
