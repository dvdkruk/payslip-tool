/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */

package com.github.dvdkruk.payslip.core;

import java.util.List;

/**
 * ITaxRuleFactory.
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public interface ITaxRuleFactory {

    /**
     * Create a list of tax rules.
     *
     * @return A list of tax rules.
     */
    List<ITaxRule> getTaxRules();

}
