/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;

/**
 * A class for wrapping objects for tests.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @param <T> Type of te wrapped object.
 * @since 1.0
 */
public final class TestAssert<T> {

    /**
     * Wrapped {@link Object} used in the tests.
     */
    private final T obj;

    /**
     * Wraps the given {@code obj} for tests.
     *
     * @param obj An {@link T};
     */
    public TestAssert(final T obj) {
        this.obj = obj;
    }

    /**
     * Assert that the wrapper object is not equal to {@code that}.
     *
     * @param that Another object used in the not equals test.
     */
    public void notEqualTo(final T that) {
        MatcherAssert.assertThat(this.obj, IsNot.not(IsEqual.equalTo(that)));
        MatcherAssert.assertThat(that, IsNot.not(IsEqual.equalTo(this.obj)));
    }

    /**
     * Assert that the wrapper object is equal to {@code that}.
     *
     * @param that Another object used in the equals test.
     */
    public void equalTo(final T that) {
        MatcherAssert.assertThat(this.obj, IsEqual.equalTo(that));
        MatcherAssert.assertThat(that, IsEqual.equalTo(this.obj));
    }

    /**
     * Asserts that the wrapped object is the same as {@code that}.
     *
     * @param that Another object.
     */
    public void isSame(final T that) {
        MatcherAssert.assertThat(this.obj, Is.is(that));
    }

    /**
     * Asserts that the wrapped object is not the same as {@code that}.
     *
     * @param that Another object.
     */
    public void isNotSame(final T that) {
        MatcherAssert.assertThat(this.obj, IsNot.not(that));
    }

}
