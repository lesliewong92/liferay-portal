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

import com.liferay.jenkins.results.parser.failure.message.generator.FailureMessageGenerator;
import com.liferay.jenkins.results.parser.failure.message.generator.GenericFailureMessageGenerator;
import com.liferay.jenkins.results.parser.failure.message.generator.RebaseFailureMessageGenerator;
import com.liferay.jenkins.results.parser.failure.message.generator.SubrepositorySourceFormatFailureMessageGenerator;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Override
	public Element getGitHubMessageElement() {
		Element rootElement = Dom4JUtil.getNewElement(
			"html", null, getResultElement(), getBuildTimeElement(),
			Dom4JUtil.getNewElement("h4", null, "Base Branch:"),
			getBaseBranchDetailsElement());

		String consoleText = getConsoleText();

		String[] consoleSnippets = consoleText.split(
			"Executing subrepository task ");

		if (consoleSnippets.length > 1) {
			Dom4JUtil.addToElement(
				rootElement,
				Dom4JUtil.getNewElement("h4", null, "Task Summary:"));

			Element taskSummaryListElement = Dom4JUtil.getNewElement(
				"ul", rootElement);

			for (int i = 1; i < consoleSnippets.length; i++) {
				String consoleSnippet = consoleSnippets[i];

				if (consoleSnippet.contains("merge-test-results:")) {
					continue;
				}

				Dom4JUtil.addToElement(
					taskSummaryListElement,
					getTaskSummaryIndexElement(consoleSnippet));
			}
		}
		else {
			Dom4JUtil.addToElement(rootElement, getFailureMessageElement());
		}

		return rootElement;
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

	protected String getTaskResultIcon(String result) {
		if (result.equals("FAILED")) {
			return " :x:";
		}

		if (result.equals("SUCCESS")) {
			return " :white_check_mark:";
		}

		return "";
	}

	protected Element getTaskSummaryIndexElement(String console) {
		String taskName = console.substring(0, console.indexOf("\n"));

		Matcher matcher = _consoleResultPattern.matcher(console);

		String taskResult = "FAILED";

		if (matcher.find()) {
			taskResult = matcher.group(1);
		}

		Element taskSummaryIndexElement = Dom4JUtil.getNewElement("li", null);

		Dom4JUtil.addToElement(
			taskSummaryIndexElement,
			Dom4JUtil.getNewElement("strong", null, taskName), " - ",
			getTaskResultIcon(taskResult));

		if (taskResult.equals("FAILED")) {
			FailureMessageGenerator failureMessageGenerator =
				new GenericFailureMessageGenerator();

			Dom4JUtil.addToElement(
				taskSummaryIndexElement,
				failureMessageGenerator.getMessageElement(console));
		}

		return taskSummaryIndexElement;
	}

	private static final Pattern _consoleResultPattern = Pattern.compile(
		"Subrepository task (FAILED|SUCCESSFUL)");

	private static final FailureMessageGenerator[] _failureMessageGenerators = {
		new RebaseFailureMessageGenerator(),
		new SubrepositorySourceFormatFailureMessageGenerator(),

		new GenericFailureMessageGenerator()
	};

}