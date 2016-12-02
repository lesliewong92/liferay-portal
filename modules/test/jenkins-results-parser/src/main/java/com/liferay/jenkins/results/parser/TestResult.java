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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Leslie Wong
 */
public class TestResult {

	public TestResult(Build build) {
		String buildURL = JenkinsResultsParserUtil.getLocalURL(
			build.getBuildURL());

		JSONObject jsonObject = JenkinsResultsParserUtil.toJSONObject(
			buildURL + "/testReport/api/json");

		
	}

	public String getDuration() {
		return duration;
	}

	public String getTestName() {
		return testName;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public String testName;
	public String testStatus;
	public String duration;
}