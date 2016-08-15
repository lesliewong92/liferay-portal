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

import java.io.File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;

/**
 * @author Leslie Wong
 */
public class GitHubSubRepositoryMessageUtil {

	public static void getGitHubMessage(Project project) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("<h1>");
		sb.append(project.getProperty("top.level.result.message"));
		sb.append("</h1>");

		sb.append("<p>Build Time: ");
		sb.append(project.getProperty("top.level.build.time"));
		sb.append("</p>");

		String rebaseBranchGitCommit = project.getProperty(
			"rebase.branch.git.commit");

		if (!rebaseBranchGitCommit.equals("")) {
			sb.append("<h4>Base Branch:</h4>");
			sb.append("<p>Branch Name: ");
			sb.append("<a href=\"https://github.com/liferay/");
			sb.append(project.getProperty("repository"));
			sb.append("/tree/");
			sb.append(project.getProperty("branch.name"));
			sb.append("\">");
			sb.append(project.getProperty("branch.name"));
			sb.append("</a><br />");
			sb.append("Branch GIT ID: <a href=\"https://github.com/liferay/");
			sb.append(project.getProperty("repository"));
			sb.append("/commit/");
			sb.append(rebaseBranchGitCommit);
			sb.append("\">");
			sb.append(rebaseBranchGitCommit);
			sb.append("</a></p>");
		}

		sb.append("<h4>Task Summary:</h4>");
		sb.append("<ul>");

		String buildURL = project.getProperty("build.url");

		String progressiveText = JenkinsResultsParserUtil.toString(
			JenkinsResultsParserUtil.getLocalURL(
				buildURL + "/logText/progressiveText"), false);

		Matcher matcher = pattern.matcher(progressiveText);

		while (matcher.find()) {
			sb.append("<li><strong><a href=\"");
			sb.append(project.getProperty("top.level.shared.dir.url"));
			sb.append("/");

			String taskName = matcher.group(1);

			sb.append(taskName);
			sb.append(".log");
			sb.append("\">");
			sb.append(taskName);
			sb.append("</a></strong> ");
			sb.append("- ");

			String console = matcher.group(0);

			SubRepositoryTask subRepositoryTask;

			if (console.contains("merge-test-results:")) {
				subRepositoryTask = new SubRepositoryTaskReport(
					buildURL, taskName);
			}
			else {
				subRepositoryTask = new SubRepositoryTaskNoReport(
					console, taskName);
			}

			String result = subRepositoryTask.getResult();

			sb.append(result);

			if (result.equals("SUCCESS")) {
				sb.append(" :white_check_mark:");
			}
			else {
				if (result.equals("ABORTED")) {
					sb.append(" :no_entry:");
				}
				else if (result.equals("FAILURE")) {
					sb.append(" :x:");
				}

				sb.append(subRepositoryTask.getGitHubMessage());
			}

			sb.append("</li>");
		}

		sb.append("</ul>");

		project.setProperty("report.html.content", sb.toString());
	}

	private static final Pattern pattern = Pattern.compile(
		"Executing task ([\\w-]+)[\\s\\S]+?Task (SUCCESSFUL|FAILED)");

}