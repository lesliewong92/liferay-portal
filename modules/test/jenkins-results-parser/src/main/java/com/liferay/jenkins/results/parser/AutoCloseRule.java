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

/**
 * @author Peter Yoo
 */
public class AutoCloseRule {

	public AutoCloseRule(String ruleData) {
		this.ruleData = ruleData;

		String[] ruleDataArray = ruleData.split("\\|");

		rulePattern = Pattern.compile(ruleDataArray[0]);

		if (ruleDataArray[1].endsWith("%")) {
			String percentageRule = ruleDataArray[1];

			maxFailPercentage = Integer.parseInt(
				percentageRule.substring(0, percentageRule.length() - 1)) / 100;
		}
		else {
			maxFailCount = Integer.parseInt(ruleDataArray[1]);
		}
	}

	public List<Build> evaluate(List<Build> downstreamBuilds, Project project)
		throws Exception {

		downstreamBuilds = getMatchingBuilds(downstreamBuilds);

		if (downstreamBuilds.isEmpty()) {
			return Collections.emptyList();
		}

		Set<Build> failedDownstreamBuilds = new HashSet<>(
			downstreamBuilds.size());

		int failLimit = 0;

		if (maxFailPercentage != -1) {
			failLimit = (int)(maxFailPercentage * downstreamBuilds.size());

			if (failLimit > 0) {
				failLimit--;
			}
		}
		else {
			failLimit = maxFailCount;
		}

		for (Build downstreamBuild : downstreamBuilds) {
			String status = downstreamBuild.getStatus();

			if (!status.equals("completed")) {
				continue;
			}

			String result = downstreamBuild.getResult();

			if ((result != null) && !result.equals("SUCCESS")) {
				String testrayTestType = getTestrayTestType(
					downstreamBuild, project);

				if (testrayTestType) {
					List<TestResult> testResults = 
						downstreamBuild.getDownstreamTestResults();

					for (TestResult testResult : testResults) {
						String testName = testResult.getTestName();

						if (!FlakinessEvaluatorUtil.isFlaky(
							downstreamBuild, project, testName)) {

							failedDownstreamBuilds.add(downstreamBuild);

							break;
						}
					}
				}
				else {
					failedDownstreamBuilds.add(downstreamBuild);
				}
			}
		}

		if (failedDownstreamBuilds.size() > failLimit) {
			return failedDownstreamBuilds;
		}

		return Collections.emptySet();
	}

	@Override
	public String toString() {
		return ruleData;
	}

	protected String getBatchName(Build build) {
		String batchName = build.getParameterValue("JOB_VARIANT");

		if ((batchName == null) || batchName.isEmpty()) {
			batchName = build.getParameterValue("JENKINS_JOB_VARIANT");
		}

		return batchName;
	}

	protected List<Build> getMatchingBuilds(List<Build> downstreamBuilds) {
		List<Build> filteredDownstreamBuilds = new ArrayList<>(
			downstreamBuilds.size());

		for (Build downstreamBuild : downstreamBuilds) {
			String batchName = getBatchName(downstreamBuild);

			if ((batchName == null) || batchName.isEmpty()) {
				continue;
			}

			Matcher matcher = rulePattern.matcher(
				getBatchName(downstreamBuild));

			if (matcher.matches()) {
				filteredDownstreamBuilds.add(downstreamBuild);
			}
		}

		return filteredDownstreamBuilds;
	}

	public boolean autoCloseOnCriticalTestFailures(Build topLevelBuild) throws Exception {
		String autoCloseCommentAvailable = project.getProperty("auto.close.comment.available");

		if (autoCloseCommentAvailable.equals("true") || !isAutoCloseOnCriticalTestFailuresActive()) {
			return false;
		}

		String githubReceiverUsername = project.getProperty("env.GITHUB_RECEIVER_USERNAME");
		String githubSenderUsername = project.getProperty("env.GITHUB_SENDER_USERNAME");

		if ((githubReceiverUsername == null) || (githubSenderUsername == null) || githubReceiverUsername.equals(githubSenderUsername)) {
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

			if (!batchName.contains("integration") && !batchName.contains("unit")) {
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

			String subrepositoryPackageNames = project.getProperty("subrepository.package.names");

			if (subrepositoryPackageNames != null) {
				JSONObject jsonObject = JenkinsResultsParserUtil.toJSONObject(buildURL + "testReport/api/json?tree=childReports[child[url],result[suites[cases[className,name,status]]]]");

				JSONArray childReportsJSONArray = jsonObject.getJSONArray("childReports");

				for (String subrepositoryPackageName : subrepositoryPackageNames.split(",")) {
					if (!jenkinsJobFailureURLs.isEmpty()) {
						break;
					}

					for (int i = 0; i < childReportsJSONArray.length(); i++) {
						JSONObject childReportsJSONObject = childReportsJSONArray.get(i);

						JSONObject resultJSONObject = childReportsJSONObject.getJSONObject("result");

						JSONArray suitesJSONArray = resultJSONObject.getJSONArray("suites");

						for (int j = 0; j < suitesJSONArray.length(); j++) {
							JSONObject suitesJSONObject = suitesJSONArray.get(j);

							JSONArray casesJSONArray = suitesJSONObject.getJSONArray("cases");

							for (int k = 0; k < casesJSONArray.length(); k++) {
								JSONObject casesJSONObject = casesJSONArray.get(k);

								String status = casesJSONObject.get("status");

								if (status.equals("FAILED") || status.equals("REGRESSION")) {
									String className = casesJSONObject.get("className");

									int x = className.lastIndexOf(".");

									String packageName = className.substring(0, x);

									if (subrepositoryPackageName.equals(packageName)) {
										String simpleClassName = className.substring(x + 1);
										String methodName = casesJSONObject.get("name");

										String testrayTestcaseName = simpleClassName + methodName;

										if (!FlakinessEvaluatorUtil.isFlaky(downstreamBuild, project, testrayTestcaseName)) {
											failedBuildURL = buildURL;

											failedBuildURL = failedBuildURL.replaceAll("http://", "https://");
											failedBuildURL = failedBuildURL.replaceAll("test-\\d+-\\d+", "$0.liferay.com");

											StringBuilder sb = new StringBuilder();

											JSONObject childJSONObject = childReportsJSONObject.getJSONObject("child");

											sb.append("<a href=\"");
											sb.append(childJSONObject.get("url"));
											sb.append("testReport/junit/");
											sb.append(packageName);
											sb.append("/");
											sb.append(simpleClassName);
											sb.append("/");

											methodName = methodName.replaceAll("\\.", "_");

											sb.append(methodName);
											sb.append("\">");
											sb.append(className);
											sb.append("</a>");

											jenkinsJobFailureURLs.add(sb.toString());
										}
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
			String upstreamBranchName = project.getProperty("env.GITHUB_UPSTREAM_BRANCH_NAME");

			if (upstreamBranchName.equals("master") || upstreamBranchName.equals("7.0.x")) {
				repository = "liferay-portal";
			}

			Map attributes = new HashMap();

			attributes.put("pull.request.number", project.getProperty("env.GITHUB_PULL_REQUEST_NUMBER"));
			attributes.put("repository", repository);
			attributes.put("username", project.getProperty("env.GITHUB_RECEIVER_USERNAME"));

			runTask("close-github-pull-request", attributes);

			StringBuilder sb = new StringBuilder();

			sb.append("<h1>The pull request tester is still running.</h1><p>Please wait until you get the <i><b>final report</b></i> before running 'ci:retest'.</p><p>See this link to check on the status of your test:</p>");

			sb.append("<ul><li><a href=\"");
			sb.append(project.getProperty("env.BUILD_URL"));
			sb.append("\">");
			sb.append(topLevelBuild.getJobName());
			sb.append("</a></li></ul><hr />");

			sb.append("<h1>However, the pull request was closed.</h1><p>The pull request was closed due to the following integration/unit test failures:</p><ul>");

			for (String jenkinsJobFailureURL : jenkinsJobFailureURLs) {
				sb.append("<li>");
				sb.append(jenkinsJobFailureURL);
				sb.append("</li>");
			};

			sb.append("</ul><p>These test failures are a part of a 'module group'/'subrepository' that was changed in this pull request.</p>");
			sb.append("<p auto-close=\"false\"><strong><em>*This pull will no longer automatically close if this comment is available. If you believe this is a mistake please re-open this pull by entering the following command as a comment.</em></strong></p><pre>ci&#58;reopen</pre><hr /><h3>Critical Failure Details:</h3>");

			project.setProperty("build.url", failedBuildURL);

			GitHubJobMessageUtil.getGitHubJobMessage(project);

			sb.append(project.getProperty("report.html.content"));

			attributes.put("comment.body", sb.toString());

			runTask("post-github-comment", attributes);

			return true;
		}

		return false;
	}

	protected String getTestrayTestType(Build build, Project project) {
		String batchName = getBatchName(build);

		String testrayTestTypes = project.getProperty("testray.test.types");

		for (String testrayTestType : testrayTestTypes.split(",")) {
			if (batchName.startsWith(testrayTestType)) {
				return testrayTestType;
			}
		}

		return "";
	}

	protected String getTestrayServerNames(Build build, Project project) {
		StringBuild sb = new StringBuilder();

		sb.append("testray.server.names[");

		TopLevelBuild topLevelJob = build.getTopLevelBuild();

		sb.append(topLevelJob.getJobName());
		sb.append("/");
		sb.append(getTestrayTestType(build, project));
		sb.append("]");

		return project.getProperty(sb.toString());
	}

	protected int maxFailCount = -1;
	protected float maxFailPercentage = -1;
	protected String ruleData;
	protected Pattern rulePattern;

}