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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leslie Wong
 */
public class FlakyTestRule {

	public FlakyTestRule(String ruleData) {
		this.ruleData = ruleData;

		String[] ruleDataArray = ruleData.split("\\|");

		rulePattern = Pattern.compile(ruleDataArray[0]);

		int duration = Integer.parseInt(ruleDataArray[1]);

		ruleDuration = new Date(
			System.currentTimeMillis() - (long)(duration * 86400000));

		String percentage = ruleDataArray[2];

		if (percentage.endsWith("%")) {
			percentage = percentage.substring(0, percentage.length() - 1);
		}

		rulePercentage = Integer.parseInt(percentage) / 100;
	}

	protected String getBatchName(Build build) {
		String batchName = build.getParameterValue("JOB_VARIANT");

		if ((batchName == null) || batchName.isEmpty()) {
			batchName = build.getParameterValue("JENKINS_JOB_VARIANT");
		}

		return batchName;
	}

	protected boolean isMatchingBuild(Build build) {
		String batchName = getBatchName(build);

		if ((batchName == null) || batchName.isEmpty()) {
			return false;
		}

		Matcher matcher = rulePattern.matcher(batchName);

		if (matcher.matches()) {
			return true;
		}

		return false;
	}

	protected String ruleData;
	protected Date ruleDuration;
	protected Pattern rulePattern;
	protected int rulePercentage;

}