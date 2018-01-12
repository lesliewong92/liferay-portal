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

package com.liferay.jenkins.results.parser.failure.message.generator;

import com.liferay.jenkins.results.parser.Build;
import com.liferay.jenkins.results.parser.Dom4JUtil;
import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.TopLevelBuild;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;

/**
 * @author Peter Yoo
 */
public class RebaseFailureMessageGenerator extends BaseFailureMessageGenerator {

	@Override
	public Element getMessageElement(Build build) {
		String consoleText = build.getConsoleText();

		if (!consoleText.contains(_TOKEN_REBASE_START)) {
			return null;
		}

		int end = consoleText.indexOf(_TOKEN_REBASE_END);

		end = consoleText.lastIndexOf("\n", end);

		int start = consoleText.lastIndexOf(_TOKEN_REBASE_START, end);

		start = consoleText.lastIndexOf("\n", start) +  1;

		Map<String, String> repositoryGitDetails = getRepositoryGitDetails(
			build, consoleText.substring(start, end));

		return Dom4JUtil.getNewElement(
			"div", null,
			Dom4JUtil.getNewElement(
				"p", null, "Please fix ",
				Dom4JUtil.getNewElement("strong", null, "rebase errors"),
				" on ",
				Dom4JUtil.getNewElement(
					"strong", null,
					getBaseBranchAnchorElement(repositoryGitDetails)),
				getConsoleTextSnippetElement(consoleText, false, start, end)));
	}

	protected Map<String, String> getRepositoryGitDetails(
		Build build, String consoleText) {

		TopLevelBuild topLevelBuild = build.getTopLevelBuild();

		int x = consoleText.indexOf("\n");

		String errorMessage = consoleText.substring(0, x);

		Matcher matcher = _errorMessagePattern.matcher(errorMessage);

		if (matcher.matches()) {
			String repositoryName = matcher.group("repositoryName");

			Properties buildProperties = null;

			try {
				buildProperties = JenkinsResultsParserUtil.getBuildProperties();
			}
			catch (IOException ioe) {
				throw new RuntimeException("Unable to get build properties");
			}

			String repositoryType = buildProperties.getProperty(
				"repository.type[" + repositoryName + "]");

			if (repositoryType == null) {
				repositoryType = repositoryName;
			}

			Map<String, String> gitRepositoryDetailsMap = new HashMap<>(
				topLevelBuild.getGitRepositoryDetailsTempMap(repositoryType));

			gitRepositoryDetailsMap.put(
				"github.base.repository.name", repositoryName);

			return gitRepositoryDetailsMap;
		}

		return new HashMap<>();
	}

	private static final String _TOKEN_REBASE_END = "BUILD FAILED";

	private static final String _TOKEN_REBASE_START = "Unable to rebase";

	private static final Pattern _errorMessagePattern = Pattern.compile(
		".*Unable to rebase \\S+ to \\S+ in (?<repositoryName>.+)");

}