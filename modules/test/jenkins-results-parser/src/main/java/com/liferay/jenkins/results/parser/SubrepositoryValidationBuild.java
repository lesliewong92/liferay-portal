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

	public ValidationBuild(String url) {
		super(url);
	}

	public ValidationBuild(String url, Build parentBuild) {
		super(url, parentBuild);
	}

	@Override
	public void findDownstreamBuilds() {
	}

	@Override
	public JSONObject getTestReportJSONObject() {
		return null;
	}

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

	@Override
	public List<TestResult> getTestResults(String testStatus) {
		return Collections.emptyList();
	}

	@Override
	protected FailureMessageGenerator[] getFailureMessageGenerators() {
		return _FAILURE_MESSAGE_GENERATORS;
	}

	@Override
	public Element getGitHubMessageElement() {
		Element rootElement = getRootElement();

		String consoleText = getConsoleText();

		String[] consoleSnippets = consoleText.split(
			"Executing subrepository task ");

		if (consoleSnippets.length > 1) {
			Dom4JUtil.addToElement(
				rootElement, getTaskSummaryListElement(consoleSnippets));
		}
		else {
			Dom4JUtil.addToElement(rootElement, getFailureMessageElement());
		}

		return rootElement;
	}

	protected Element getBaseBranchDetailsElement() {
		String baseBranchURL =
			"https://github.com/liferay/" + getBaseRepositoryName() + "/tree/" +
				getBranchName();

		String baseRepositoryName = getBaseRepositoryName();

		String baseRepositorySHA = getBaseRepositorySHA(baseRepositoryName);

		String baseRepositoryCommitURL =
			"https://github.com/liferay/" + baseRepositoryName + "/commit/" +
				baseRepositorySHA;

		Element baseBranchDetailsElement = Dom4JUtil.getNewElement(
			"p", null, "Branch Name: ",
			Dom4JUtil.getNewAnchorElement(baseBranchURL, getBranchName()));

		if (baseRepositorySHA != null) {
			Dom4JUtil.addToElement(
				baseBranchDetailsElement, Dom4JUtil.getNewElement("br"),
				"Branch GIT ID: ",
				Dom4JUtil.getNewAnchorElement(
					baseRepositoryCommitURL, baseRepositorySHA));
		}

		return baseBranchDetailsElement;
	}

	@Override
	protected Element getGitHubMessageJobResultsElement() {
		return null;
	}

	protected Element getResultElement() {
		Element resultElement = Dom4JUtil.getNewElement("h1");

		String result = getResult();

		if (!result.equals("SUCCESS")) {
			resultElement.addText("Validation PASSED. Running batch tests.");
		}
		else {
			resultElement.addText("Validation FAILED.");
		}

		return resultElement;
	}

	protected Element getBuildTimeElement() {
		return Dom4JUtil.getNewElement(
			"p", null, "Build Time: ",
			JenkinsResultsParserUtil.toDurationString(getDuration()));
	}

	protected SubrepositoryTask getSubrepositoryTask(String console) {
		if (console.contains(
				"A report with all the test results can be found at " +
					"test-results/html/index.html")) {

			return new SubrepositoryTaskReport(getBuildURL());
		}
		else {
			return new SubrepositoryTaskNoReport(console);
		}
	}

	protected Element getTaskSummaryListElement(String[] consoleSnippets) {

		Element taskSummaryListElement = Dom4JUtil.getNewElement("ul");

		for (int i = 1; i < consoleSnippets.length; i++) {
			SubrepositoryTask subrepositoryTask = getSubrepositoryTask(
				consoleSnippets[i]);

			Element taskSummaryListIndexElement = Dom4JUtil.getNewElement(
				"li", null, subrepositoryTask.getTaskSummaryListIndexElement());
		}

		return taskSummaryListElement;
	}

	private static final FailureMessageGenerator[] _FAILURE_MESSAGE_GENERATORS =
		{
			new GenericFailureMessageGenerator()
		};

}