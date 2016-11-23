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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;

/**
 * @author Leslie Wong
 */
public class FlakyTestRule {

	public static final int STATUS_BLOCKED = 4;

	public static final int STATUS_DID_NOT_RUN = 6;

	public static final int STATUS_FAILED = 3;

	public static final int STATUS_IN_PROGRESS = 1;

	public static final int STATUS_PASSED = 2;

	public static final int STATUS_RETEST = 5;

	public static final int STATUS_TEST_FIX = 7;

	public static final int STATUS_UNTESTED = 0;

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

		rulePercentage = Double.parseDouble(percentage) / 100;
	}

	public boolean isFlaky(Build build, Project project, String testName)
		throws Exception {

		if (!isMatchingBuild(build)) {
			return false;
		}

		double percentage = getFailurePercentage(build, project, testName);

		if (percentage > rulePercentage) {
			return true;
		}

		return false;
	}

	protected String convertToEnvironmentHash(List<String> testrayFactors)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		for (String testrayFactor : testrayFactors) {
			List<Map<String, Object>> queryResult;

			queryResult = DBUtil.executeQuery(
				"select testrayFactorCategoryId, testrayFactorOptionId from " +
					"TestrayFactorOption where name='" + testrayFactor + "'");

			Map<String, Object> testrayFactorIds = queryResult.get(0);

			sb.append((String)testrayFactorIds.get("testrayFactorCategoryId"));
			sb.append((String)testrayFactorIds.get("testrayFactorOptionId"));
		}

		String testrayFactorsString = sb.toString();

		return String.valueOf(testrayFactorsString.hashCode());
	}

	protected String getBatchName(Build build) {
		String batchName = build.getParameterValue("JOB_VARIANT");

		if ((batchName == null) || batchName.isEmpty()) {
			batchName = build.getParameterValue("JENKINS_JOB_VARIANT");
		}

		return batchName;
	}

	protected double getFailurePercentage(
			Build build, Project project, String testcaseName)
		throws Exception {

		List<Map<String, Object>> testIDQueryResult = DBUtil.executeQuery(
			"select testrayCaseId from TestrayCase where name = '" +
				testcaseName + "'");

		String testID;

		if (testIDQueryResult.isEmpty()) {
			throw new Exception("Cannot find test id of " + testcaseName);
		}

		Map<String, Object> testIDMap = testIDQueryResult.get(0);

		String testrayCaseID = (String)testIDMap.get("testrayCaseId");

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT TestrayCaseResult.status, COUNT(*) ");
		sb.append("FROM TestrayCaseResult JOIN TestrayRun ");
		sb.append("JOIN TestrayBuild ");
		sb.append("JOIN TestrayBuildType ");
		sb.append("WHERE TestrayCaseResult.testrayCaseId = '");
		sb.append(testrayCaseID);
		sb.append("' AND TestrayBuildType.name = '");
		sb.append(project.getProperty("testray.build.type"));
		sb.append("' AND TestrayRun.environmentHash = '");
		sb.append(getTestrayEnvironmentHash(build, project));
		sb.append("' AND TestrayCaseResult.startDate > DATE'");
		sb.append(ruleDuration.toString());
		sb.append("' GROUP BY TestrayCaseResult.status;");

		List<Map<String, Object>> statuses = DBUtil.executeQuery(sb.toString());

		int total = 0;
		int failureCount = 0;

		for (Map<String, Object> status : statuses) {
			Integer statusCount = (Integer)status.get("count(*)");

			if ((Integer)status.get("status") == STATUS_FAILED) {
				failureCount = statusCount;
			}

			total += statusCount;
		}

		return (double)failureCount / (double)total;
	}

	protected String getTestrayEnvironmentHash(Build build, Project project)
		throws Exception {

		String batchName = getBatchName(build);

		List<String> testrayFactors = new ArrayList<>();

		List<String> environmentTypes = Arrays.asList(
			"app.server", "browser", "database", "java.jdk",
			"operating.system");

		for (String environmentType : environmentTypes) {
			boolean match = false;

			String environments = project.getProperty(
				environmentType + ".types");

			for (String environment : environments.split(",")) {
				if (batchName.contains(environment)) {
					int x = batchName.indexOf(environment);

					int y = batchName.indexOf("-", x);

					String batchEnvironment;

					if (y != -1) {
						batchEnvironment = batchName.substring(x, y);
					}
					else {
						batchEnvironment = batchName.substring(x);
					}

					String testrayFactorOption = project.getProperty(
						"env.option." + environmentType + "." +
							batchEnvironment);

					testrayFactors.add(testrayFactorOption);

					match = true;

					break;
				}
			}

			if (!match) {
				String environment = project.getProperty(
					environmentType + ".type");

				String environmentVersion = project.getProperty(
					environmentType + ".version");

				Matcher matcher = majorVersionPattern.matcher(
					environmentVersion);

				String environmentMajorVersion;

				if (matcher.matches()) {
					environmentMajorVersion = matcher.group(1);
				}
				else {
					environmentMajorVersion = environmentVersion;
				}

				environmentMajorVersion = environmentMajorVersion.replace(
					".", "");

				String testrayFactorOption = project.getProperty(
					"env.option." + environmentType + "." + environment +
						environmentMajorVersion);

				testrayFactors.add(testrayFactorOption);
			}
		}

		return convertToEnvironmentHash(testrayFactors);
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

	protected Pattern majorVersionPattern = Pattern.compile(
		"((\\d+)\\.?(\\d+?)).*");

	protected String ruleData;
	protected Date ruleDuration;
	protected Pattern rulePattern;
	protected double rulePercentage;

}