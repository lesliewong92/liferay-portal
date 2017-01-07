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

import java.io.IOException;

import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Leslie Wong
 */
public class TestResult {

	public static List<TestResult> getTestResults(
		AxisBuild axisBuild, JSONArray suitesJSONArray, String testStatus) {

		List<TestResult> testResults = new ArrayList<>();

		for (int i = 0; i < suitesJSONArray.length(); i++) {
			JSONObject suiteJSONObject = suitesJSONArray.getJSONObject(i);

			JSONArray casesJSONArray = suiteJSONObject.getJSONArray("cases");

			for (int j = 0; j < casesJSONArray.length(); j++) {
				TestResult testResult = new TestResult(
					axisBuild, casesJSONArray.getJSONObject(j));

				if ((testStatus == null) ||
					testStatus.equals(testResult.getStatus())) {

					testResults.add(testResult);
				}
			}
		}

		return testResults;
	}

	public TestResult(AxisBuild axisBuild, JSONObject caseJSONObject) {
		if (axisBuild == null) {
			throw new IllegalArgumentException("Axis build may not be null");
		}

		this.axisBuild = axisBuild;

		className = caseJSONObject.getString("className");

		duration = (long)(caseJSONObject.getDouble("duration") * 1000d);

		int x = className.lastIndexOf(".");

		simpleClassName = className.substring(x + 1);

		packageName = className.substring(0, x);

		testName = caseJSONObject.getString("name");

		status = caseJSONObject.getString("status");
	}

	public AxisBuild getAxisBuild() {
		return axisBuild;
	}

	public String getClassName() {
		return className;
	}

	public String getConsoleOutputURL() {
		StringBuilder sb = new StringBuilder();

		sb.append(axisBuild.getTestRayLogsURL());
		sb.append("/jenkins-console.txt.gz");

		return sb.toString();
	}

	public String getDisplayName() {
		if (testName.startsWith("test[")) {
			return testName.substring(5, testName.length() - 1);
		}

		return simpleClassName + "." + testName;
	}

	public long getDuration() {
		return duration;
	}

	public String getLiferayLogURL() {
		StringBuilder sb = new StringBuilder();

		String name = getDisplayName();

		sb.append(axisBuild.getTestRayLogsURL());
		sb.append("/");
		sb.append(name.replace("#", "_"));
		sb.append("/liferay-log.txt.gz");

		return sb.toString();
	}

	public String getPoshiReportURL() {
		StringBuilder sb = new StringBuilder();

		String name = getDisplayName();

		sb.append(axisBuild.getTestRayLogsURL());
		sb.append("/");
		sb.append(name.replace("#", "_"));
		sb.append("/index.html.gz");

		return sb.toString();
	}

	public String getPoshiSummaryURL() {
		StringBuilder sb = new StringBuilder();

		String name = getDisplayName();

		sb.append(axisBuild.getTestRayLogsURL());
		sb.append("/");
		sb.append(name.replace("#", "_"));
		sb.append("/summary.html.gz");

		return sb.toString();
	}

	public String getStatus() {
		return status;
	}

	public String getTestName() {
		return testName;
	}

	public String getTestReportURL() {
		StringBuilder sb = new StringBuilder();

		sb.append(axisBuild.getBuildURL());
		sb.append("/testReport/");
		sb.append(packageName);
		sb.append("/");
		sb.append(simpleClassName);
		sb.append("/");

		String encodedTestName = testName;

		encodedTestName = encodedTestName.replace("[", "_");
		encodedTestName = encodedTestName.replace("]", "_");
		encodedTestName = encodedTestName.replace("#", "_");

		if (simpleClassName.equals("junit.framework")) {
			encodedTestName = encodedTestName.replace(".", "_");
		}

		sb.append(encodedTestName);

		return sb.toString();
	}

	public boolean hasLiferayLog() {
		String liferayLog = null;

		try {
			liferayLog = JenkinsResultsParserUtil.toString(
				getLiferayLogURL(), false, 0, 0, 0);
		}
		catch (IOException ioe) {
			return false;
		}

		return !liferayLog.isEmpty();
	}

	protected void calculateFailurePercentage() throws Exception {
		Properties buildProperties;

		try {
			buildProperties = JenkinsResultsParserUtil.getBuildProperties();
		}
		catch (IOException ioe) {
			throw new RuntimeException("Unable to get build.properties.", ioe);
		}

		List<Object> arguments = new ArrayList<>();

		arguments.add(simpleClassName + "#" + testName);

		List<Map<String, Object>> testCaseIDs = DBUtil.executeQuery(
			buildProperties.getProperty(
				"testray.integration.testcase.id.query"),
			arguments);

		Map<String, Object> testCaseId = testCaseIDs.get(0);

		long testrayCaseId = (Long)testCaseId.get("testrayCaseId");

		arguments.clear();

		arguments.add(buildProperties.getProperty("testray.build.type"));

		List<Map<String, Object>> buildTypes = DBUtil.executeQuery(
			buildProperties.getProperty(
				"testray.integration.build.type.id.query"),
			arguments);

		Map<String, Object> buildType = buildTypes.get(0);

		long buildTypeId = (Long)buildType.get("testrayBuildTypeId");

		arguments.clear();

		arguments.add(testrayCaseId);
		arguments.add(buildTypeId);
		arguments.add(getEnvironmentHash(buildProperties));

		long durationInMillis = TimeUnit.DAYS.toMillis(
			Long.valueOf(
				buildProperties.getProperty("flakiness.assertion.duration")));

		arguments.add(new Date(System.currentTimeMillis() - durationInMillis));

		List<Map<String, Object>> statuses = DBUtil.executeQuery(
			buildProperties.getProperty("testray.integration.status.query"),
			arguments);

		if (statuses.isEmpty()) {
			failurePercentage = 0;

			return;
		}

		int total = 0;
		int failureCount = 0;

		for (Map<String, Object> status : statuses) {
			Long count = (Long)status.get("COUNT(*)");

			int statusCount = count.intValue();

			if ((Integer)status.get("status") == STATUS_FAILED) {
				failureCount = statusCount;
			}

			total += statusCount;
		}

		failurePercentage = ((double)failureCount / (double)total) * 100;
	}

	protected String getEnvironmentHash(Properties properties)
		throws Exception {

		List<Map<String, Object>> environmentOptions = DBUtil.executeQuery(
			properties.getProperty("testray.integration.environment.query"),
			new ArrayList<>());

		List<String> environments = new ArrayList<>();

		environments.add(axisBuild.getAppServer());
		environments.add(axisBuild.getBrowser());
		environments.add(axisBuild.getDatabase());
		environments.add(axisBuild.getJDK());
		environments.add(axisBuild.getOperatingSystem());

		StringBuilder sb = new StringBuilder();

		for (String environment : environments) {
			for (Map<String, Object> environmentOption : environmentOptions) {
				String name = (String)environmentOption.get("name");

				if (environment.equals(name)) {
					sb.append(environmentOption.get("testrayFactorCategoryId"));
					sb.append(environmentOption.get("testrayFactorOptionId"));

					break;
				}
			}
		}

		String testrayFactors = sb.toString();

		return String.valueOf(testrayFactors.hashCode());
	}

	protected static final int STATUS_FAILED = 3;

	protected AxisBuild axisBuild;
	protected String className;
	protected long duration;
	protected double failurePercentage = -1.0;
	protected String packageName;
	protected String simpleClassName;
	protected String status;
	protected String testName;

}