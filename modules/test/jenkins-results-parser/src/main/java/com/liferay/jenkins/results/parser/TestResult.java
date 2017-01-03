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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.Project;

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

	public long getDuration() {
		return duration;
	}

	public String getStatus() {
		return status;
	}

	public String getTestName() {
		return testName;
	}

	public boolean isFlaky(Project project) {
		try {
			Properties buildProperties =
				JenkinsResultsParserUtil.getBuildProperties();

			if (failurePercentage == -1) {
				calculateFailurePercentage(project);
			}

			int failureThreshold = Integer.parseInt(
				buildProperties.getProperty("testray.failure.threshold"));

			if (failurePercentage > failureThreshold) {
				return true;
			}

			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	protected void calculateFailurePercentage(Project project)
		throws Exception {

		List<Map<String, Object>> testIDQueryResult = DBUtil.executeQuery(
			"select testrayCaseId from TestrayCase where name = '" + testName +
				"'");

		String testID;

		if (testIDQueryResult.isEmpty()) {
			failurePercentage = 0;
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
		sb.append(axisBuild.getEnvironmentHash(project));

		Properties buildProperties =
			JenkinsResultsParserUtil.getBuildProperties();

		String history = project.getProperty("testray.check.history");

		long timeInMilliseconds =
			System.currentTimeMillis() -
				(long)(Integer.parseInt(history) * 86400000);

		Date historyDate = new Date(timeInMilliseconds);

		sb.append("' AND TestrayCaseResult.startDate > DATE'");
		sb.append(historyDate.toString());
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

		failurePercentage = (failureCount / total) * 100;
	}

	protected static final int STATUS_FAILED = 3;

	protected AxisBuild axisBuild;
	protected String className;
	protected long duration;
	protected int failurePercentage = -1;
	protected String packageName;
	protected String simpleClassName;
	protected String status;
	protected String testName;

}