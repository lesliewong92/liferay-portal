/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

import java.sql.Date;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kevin Yen
 */
public class FlakyTestRuleTest {

	@Test
	public void testInitializeFlakyTestRule() {
		FlakyTestRule flakyTestRule = new FlakyTestRule(
			"modules-integration*|7|10%");

		Pattern rulePattern = flakyTestRule.getRulePattern();

		Assert.assertEquals("modules-integration*", rulePattern.toString());

		Date ruleDate = flakyTestRule.getRuleDuration();

		Assert.assertEquals("2016-11-17", ruleDate.toString());

		Assert.assertEquals(0.1, flakyTestRule.getRulePercentage(), 0.01);
	}

}