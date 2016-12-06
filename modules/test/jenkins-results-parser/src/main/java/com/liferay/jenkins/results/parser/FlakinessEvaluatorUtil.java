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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;

/**
 * @author Leslie Wong
 */
public class FlakinessEvaluatorUtil {

	public static final int STATUS_BLOCKED = 4;

	public static final int STATUS_DID_NOT_RUN = 6;

	public static final int STATUS_FAILED = 3;

	public static final int STATUS_IN_PROGRESS = 1;

	public static final int STATUS_PASSED = 2;

	public static final int STATUS_RETEST = 5;

	public static final int STATUS_TEST_FIX = 7;

	public static final int STATUS_UNTESTED = 0;

	public static boolean isFlaky(Build build, Project project, String testName)
		throws Exception {

		double percentage = getFailurePercentage(
			getBatchName(build), project, testName);

		double maxFailureThreshold = Double.parseDouble(
			project.getProperty("max.failure.threshold"));

		if (percentage > maxFailureThreshold) {
			return true;
		}

		return false;
	}

	protected static String convertToEnvironmentHash(
			List<Environment> environments)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		for (Environment environment : environments) {
			List<Map<String, Object>> queryResult;

			queryResult = DBUtil.executeQuery(
				"select testrayFactorCategoryId, testrayFactorOptionId from " +
					"TestrayFactorOption where name='" +
						environment.getFactor() + "'");

			Map<String, Object> testrayFactorIds = queryResult.get(0);

			sb.append((String)testrayFactorIds.get("testrayFactorCategoryId"));
			sb.append((String)testrayFactorIds.get("testrayFactorOptionId"));
		}

		String testrayFactorsString = sb.toString();

		return String.valueOf(testrayFactorsString.hashCode());
	}

	protected static String getBatchName(Build build) {
		String batchName = build.getParameterValue("JOB_VARIANT");

		if ((batchName == null) || batchName.isEmpty()) {
			batchName = build.getParameterValue("JENKINS_JOB_VARIANT");
		}

		return batchName;
	}

	protected static double getFailurePercentage(
			String batchName, Project project, String testcaseName)
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
		sb.append(getTestrayEnvironmentHash(batchName, project));

		String testrayCheckDuration = project.getProperty(
			"testray.check.duration");

		long timeInMilliseconds = System.currentTimeMillis() -
			(long)(Integer.parseInt(testrayCheckDuration) * 86400000);

		Date duration = new Date(timeInMilliseconds);

		sb.append("' AND TestrayCaseResult.startDate > DATE'");
		sb.append(duration.toString());
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

	protected static String getTestrayEnvironmentHash(
			String batchName, Project project)
		throws Exception {

		List<String> testrayFactors = new ArrayList<>();

		List<String> environmentTypes = Arrays.asList(
			"app.server", "browser", "database", "java.jdk",
			"operating.system");

		List<Environment> environments = new ArrayList<>();

		for (String environmentType : environmentTypes) {
			environments.add(
				new Environment(project, batchName, environmentType));
		}

		return convertToEnvironmentHash(environments);
	}

	protected static final Pattern environmentPattern = Pattern.compile(
		"(?<name>[A-Za-z]+)(?<version>[0-9]*)");
	protected static final Pattern majorVersionPattern = Pattern.compile(
		"((\\d+)\\.?(\\d+?)).*");

}