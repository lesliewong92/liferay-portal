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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;

public class AutoCloseUtil {

	public List<AutoCloseRule> getAutoCloseRules(Project project) throws Exception {
		List<AutoCloseRule> autoCloseRules = new ArrayList<>();

		String propertyNameTemplate = "test.batch.names.auto.close[" +
			project.getProperty("repository") +"?]";

		String repositoryBranchAutoClosePropertyName =
			propertyNameTemplate.replace(
				"?", "-" + project.getProperty("branch.name"));

		String testBatchNamesAutoClose = project.getProperty(
			repositoryBranchAutoClosePropertyName);

		if (testBatchNamesAutoClose == null) {
			String repositoryAutoClosePropertyName =
				propertyNameTemplate.replace("?", "");

			testBatchNamesAutoClose =
				project.getProperty(repositoryAutoClosePropertyName);
		}

		if (testBatchNamesAutoClose != null) {
			String[] autoCloseRuleDataArray = StringUtils.split(
				testBatchNamesAutoClose, ",");

			for (String autoCloseRuleData : autoCloseRuleDataArray) {
				autoCloseRules.add(new AutoCloseRule(autoCloseRuleData));
			}
		}

		return autoCloseRules;
	}

	public boolean isAutoClose(Build topLevelBuild, Project project)
		throws Exception {

		List rules = getAutoCloseRules(project);

		List<Build> downstreamBuilds = topLevelBuild.getDownstreamBuilds(null);


	}

	public boolean autoCloseOnCriticalBatchFailures(Build topLevelBuild)
		throws Exception {

		String autoCloseCommentAvailable = project.getProperty(
			"auto.close.comment.available");

		if (autoCloseCommentAvailable.equals("true")) {
			return false;
		}

		String githubReceiverUsername = project.getProperty(
			"env.GITHUB_RECEIVER_USERNAME");

		String githubSenderUsername = project.getProperty(
			"env.GITHUB_SENDER_USERNAME");

		if ((githubReceiverUsername == null) ||
			(githubSenderUsername == null) ||
			githubReceiverUsername.equals(githubSenderUsername)) {

			return false;
		}

		List rules = getAutoCloseRules();

		for (AutoCloseRule rule : rules) {
			List downstreamBuilds = topLevelBuild.getDownstreamBuilds(null);

			List failedDownstreamBuilds = rule.evaluate(downstreamBuilds);

			if (failedDownstreamBuilds.isEmpty()) {
				continue;
			}

			String repository = project.getProperty("repository");

			Map attributes = new HashMap();

			attributes.put("pull.request.number", project.getProperty(
				"env.GITHUB_PULL_REQUEST_NUMBER"));

			attributes.put("repository", repository);

			attributes.put(
				"username",
				project.getProperty("env.GITHUB_RECEIVER_USERNAME"));

			runTask("close-github-pull-request", attributes);

			StringBuilder sb = new StringBuilder();

			sb.append("<h1>The pull request tester is still running.</h1>");
			sb.append("<p>Please wait until you get the <i><b>final report</b>");
			sb.append("</i> before running 'ci:retest'.</p>");
			sb.append("<p>See this link to check on the status of your test:</p>");

			sb.append("<ul><li><a href=\"");
			sb.append(project.getProperty("env.BUILD_URL"));
			sb.append("\">");
			sb.append(topLevelBuild.getJobName());
			sb.append("</a></li></ul><p>@");
			sb.append(project.getProperty("github.sender.username"));
			sb.append("</p><hr />");

			sb.append("<h1>However, the pull request was closed.</h1>");
			sb.append("<p>The pull request was closed because the following ");
			sb.append("critical batches had failed:</p><ul>");

			String failureBuildURL = "";

			for (Build failedDownstreamBuild : failedDownstreamBuilds) {
				failureBuildURL = failedDownstreamBuild.getBuildURL();

				failureBuildURL = failureBuildURL.replaceAll(
					"http://", "https://");

				failureBuildURL = failureBuildURL.replaceAll(
					"test-\\d+-\\d+", "$0.liferay.com");

				sb.append("<li><a href=\"");
				sb.append(failureBuildURL);
				sb.append("\">");
				sb.append(getBatchName(failedDownstreamBuild));
				sb.append("</a></li>");
			}

			sb.append("</ul><p>For information as to why we automatically ");
			sb.append("close out certain pull requests see this ");
			sb.append("<a href=\"https://in.liferay.com/web/");
			sb.append("global.engineering/wiki/-/wiki/Quality+Assurance+Main/");
			sb.append("Test+Batch+Automatic+Close+List\">article</a>.</p>");
			sb.append("<p auto-close=\"false\"><strong><em>*This pull will ");
			sb.append("no longer automatically close if this comment is ");
			sb.append("available. If you believe this is a mistake please ");
			sb.append("re-open this pull by entering the following command ");
			sb.append("as a comment.</em></strong></p><pre>ci&#58;reopen");
			sb.append("</pre><hr /><h3>Critical Failure Details:</h3>");

			project.setProperty("build.url", failureBuildURL);

			GitHubJobMessageUtil.getGitHubJobMessage(project);

			sb.append(project.getProperty("report.html.content"));

			attributes.put("comment.body", sb.toString());

			runTask("post-github-comment", attributes);

			return true;
		}

		return false;
	}

	public boolean autoCloseOnCriticalTestFailures(Build topLevelBuild)
		throws Exception {

		String autoCloseCommentAvailable = project.getProperty(
			"auto.close.comment.available");

		if (autoCloseCommentAvailable.equals("true") ||
			!isAutoCloseOnCriticalTestFailuresActive()) {

			return false;
		}

		String githubReceiverUsername = project.getProperty(
			"env.GITHUB_RECEIVER_USERNAME");

		String githubSenderUsername = project.getProperty(
			"env.GITHUB_SENDER_USERNAME");

		if ((githubReceiverUsername == null) ||
			(githubSenderUsername == null) ||
			githubReceiverUsername.equals(githubSenderUsername)) {

			return false;
		}

		String failedBuildURL = "";

		List jenkinsJobFailureURLs = new ArrayList();

		List downstreamBuilds = topLevelBuild.getDownstreamBuilds(null);

		for (Build downstreamBuild : downstreamBuilds) {
			String batchName = getBatchName(downstreamBuild);

			if (batchName == null) {
				continue;
			}

			if (!batchName.contains("integration") &&
			 	!batchName.contains("unit")) {

				continue;
			}

			String status = downstreamBuild.getStatus();

			if (!status.equals("completed")) {
				continue;
			}

			String result = downstreamBuild.getResult();

			if ((result == null) || !result.equals("UNSTABLE")) {
				continue;
			}

			String buildURL = downstreamBuild.getBuildURL();

			String subrepositoryPackageNames = project.getProperty(
				"subrepository.package.names");

			if (subrepositoryPackageNames != null) {
				JSONObject jsonObject = JenkinsResultsParserUtil.toJSONObject(
					buildURL +
						"testReport/api/json?tree=childReports[child[url]," +
							"result[suites[cases[className,name,status]]]]");

				JSONArray childReportsJSONArray = jsonObject.getJSONArray(
					"childReports");

				for (String subrepositoryPackageName : subrepositoryPackageNames.split(",")) {
					if (!jenkinsJobFailureURLs.isEmpty()) {
						break;
					}

					for (int i = 0; i < childReportsJSONArray.length(); i++) {
						JSONObject childReportsJSONObject =
							childReportsJSONArray.get(i);

						JSONObject resultJSONObject =
							childReportsJSONObject.getJSONObject("result");

						JSONArray suitesJSONArray =
							resultJSONObject.getJSONArray("suites");

						for (int j = 0; j < suitesJSONArray.length(); j++) {
							JSONObject suitesJSONObject =
								suitesJSONArray.get(j);

							JSONArray casesJSONArray =
								suitesJSONObject.getJSONArray("cases");

							for (int k = 0; k < casesJSONArray.length(); k++) {
								JSONObject casesJSONObject =
									casesJSONArray.get(k);

								String status = casesJSONObject.get("status");

								if (status.equals("FAILED") ||
									status.equals("REGRESSION")) {

									String className = casesJSONObject.get(
										"className");

									int x = className.lastIndexOf(".");

									String packageName = className.substring(
										0, x);

									if (subrepositoryPackageName.equals(
										packageName)) {

										failedBuildURL = buildURL;

										failedBuildURL =
											failedBuildURL.replaceAll(
												"http://", "https://");

										failedBuildURL =
											failedBuildURL.replaceAll(
												"test-\\d+-\\d+",
												"$0.liferay.com");

										StringBuilder sb = new StringBuilder();

										String simpleClassName =
											className.substring(x + 1);

										JSONObject childJSONObject = childReportsJSONObject.getJSONObject("child");

										sb.append("<a href=\"");
										sb.append(childJSONObject.get("url"));
										sb.append("testReport/junit/");
										sb.append(packageName);
										sb.append("/");
										sb.append(simpleClassName);
										sb.append("/");

										String methodName = casesJSONObject.get(
											"name");

										methodName = methodName.replaceAll(
											"\\.", "_");

										sb.append(methodName);
										sb.append("\">");
										sb.append(className);
										sb.append("</a>");

										jenkinsJobFailureURLs.add(
											sb.toString());
									}
								}
							}
						}
					}
				}
			}
		}

		if (!jenkinsJobFailureURLs.isEmpty()) {
			String repository = "liferay-portal-ee";
			String upstreamBranchName = project.getProperty(
				"env.GITHUB_UPSTREAM_BRANCH_NAME");

			if (upstreamBranchName.equals("master") ||
				upstreamBranchName.equals("7.0.x")) {

				repository = "liferay-portal";
			}

			Map attributes = new HashMap();

			attributes.put(
				"pull.request.number",
				project.getProperty("env.GITHUB_PULL_REQUEST_NUMBER"));

			attributes.put("repository", repository);
			attributes.put(
				"username",
				project.getProperty("env.GITHUB_RECEIVER_USERNAME"));

			runTask("close-github-pull-request", attributes);

			StringBuilder sb = new StringBuilder();

			sb.append("<h1>The pull request tester is still running.</h1>");
			sb.append("<p>Please wait until you get the <i><b>final report");
			sb.append("</b></i> before running 'ci:retest'.</p><p>See this ");
			sb.append("link to check on the status of your test:</p>");

			sb.append("<ul><li><a href=\"");
			sb.append(project.getProperty("env.BUILD_URL"));
			sb.append("\">");
			sb.append(topLevelBuild.getJobName());
			sb.append("</a></li></ul>@");
			sb.append(project.getProperty("github.sender.username"));
			sb.append("</p><hr />");

			sb.append("<h1>However, the pull request was closed.</h1>");
			sb.append("<p>The pull request was closed due to the following ");
			sb.append("integration/unit test failures:</p><ul>");

			for (String jenkinsJobFailureURL : jenkinsJobFailureURLs) {
				sb.append("<li>");
				sb.append(jenkinsJobFailureURL);
				sb.append("</li>");
			};

			sb.append("</ul><p>These test failures are a part of a ");
			sb.append("'module group'/'subrepository' that was changed in ");
			sb.append("this pull request.</p>");
			sb.append("<p auto-close=\"false\"><strong><em>*This pull will ");
			sb.append("no longer automatically close if this comment is ");
			sb.append("available. If you believe this is a mistake please ");
			sb.append("re-open this pull by entering the following command ");
			sb.append("as a comment.</em></strong></p><pre>ci&#58;reopen");
			sb.append("</pre><hr /><h3>Critical Failure Details:</h3>");

			project.setProperty("build.url", failedBuildURL);

			GitHubJobMessageUtil.getGitHubJobMessage(project);

			sb.append(project.getProperty("report.html.content"));

			attributes.put("comment.body", sb.toString());

			runTask("post-github-comment", attributes);

			return true;
		}

		return false;
	}

}