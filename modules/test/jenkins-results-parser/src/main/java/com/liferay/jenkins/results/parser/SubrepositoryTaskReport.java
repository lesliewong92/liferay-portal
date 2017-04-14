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

import org.apache.tools.ant.Project;

import org.dom4j.Element;

import org.json.JSONObject;

/**
 * @author Leslie Wong
 */
public class SubrepositoryTaskReport extends SubrepositoryTask {

	public SubrepositoryTaskReport(String buildURL) {
		this.buildURL = buildURL;

		try {
			testReportJSONObject = JenkinsResultsParserUtil.toJSONObject(
				JenkinsResultsParserUtil.getLocalURL(
					buildURL + "testReport/api/json?tree=failCount"));

			if (testReportJSONObject.getInt("failCount") > 0) {
				result = "FAILURE";
			}
			else {
				result = "SUCCESS";
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Could not retrieve test report");
		}
	}

	@Override
	public Element getFailureMessageElement() {
		int successCount = 0;
		int failCount = 0;

		return Dom4JUtil.getNewElement(
			"li", null, Dom4JUtil.getNewElement("div", null,
				Dom4JUtil.getNewElement("p", null,
					Integer.toString(successCount),
				JenkinsResultsParserUtil.getNounForm(
					successCount, " Tests", " Test"), " Passed.",
				Dom4JUtil.getNewElement("br"), Integer.toString(failCount),
				JenkinsResultsParserUtil.getNounForm(
					failCount, " Tests", " Test"),
				" Failed.", _getFailedTestListElement())));
	}

	private Element _getFailedTestListElement() {
		Element failedTestListElement = Dom4JUtil.getNewElement("ol");

		return failedTestListElement;

		// Get relevant test results
		// iterate and add until reach 3
		// return
	}

	protected static JSONObject testReportJSONObject;

	protected String buildURL;

}