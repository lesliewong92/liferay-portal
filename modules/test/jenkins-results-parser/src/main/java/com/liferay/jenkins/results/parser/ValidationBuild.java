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

import java.util.Collections;
import java.util.List;

import org.dom4j.Element;

import org.json.JSONObject;

/**
 * @author Leslie Wong
 */
public class ValidationBuild extends TopLevelBuild {

	@Override
	public List<TestResult> getTestResults(String testStatus) {
		String status = getStatus();

		if (!status.equals("completed")) {
			return Collections.emptyList();
		}

		JSONObject testReportJSONObject = getTestReportJSONObject();

		return TestResult.getTestResults(
			this, testReportJSONObject.getJSONArray("suites"), testStatus);
	}

	protected ValidationBuild(String url) {
		this(url, null);
	}

	protected ValidationBuild(String url, TopLevelBuild topLevelBuild) {
		super(url, topLevelBuild);
	}

	@Override
	protected Element getResultElement() {
		Element resultElement = Dom4JUtil.getNewElement("h1");

		String result = getResult();

		if (!result.equals("SUCCESS")) {
			resultElement.addText("Validation FAILED.");
		}
		else {
			resultElement.addText("Validation PASSED. Running batch tests.");
		}

		return resultElement;
	}

	protected Element getTopGitHubMessageElement() {
		update();

		Element rootElement = Dom4JUtil.getNewElement(
			"html", null, getResultElement(), getBuildTimeElement(),
			Dom4JUtil.getNewElement("h4", null, "Base Branch:"),
			getBaseBranchDetailsElement());

		return rootElement;
	}

}