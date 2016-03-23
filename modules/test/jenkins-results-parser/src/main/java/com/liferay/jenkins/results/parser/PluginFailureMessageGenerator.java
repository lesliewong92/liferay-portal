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

import org.json.JSONObject;

/**
 * @author Peter Yoo
 */
public class PluginFailureMessageGenerator extends BaseFailureMessageGenerator {

	@Override
	public String getMessage(
			String buildURL, String consoleOutput, Project project)
		throws Exception {

		if (!buildURL.contains("portal-acceptance")) {
			return null;
		}

		JSONObject jsonObject = JenkinsResultsParserUtil.toJSONObject(
			JenkinsResultsParserUtil.getLocalURL(buildURL + "api/json"));

		String jobVariant = JenkinsResultsParserUtil.getJobVariant(jsonObject);

		if (!buildURL.contains("plugins") && !jobVariant.contains("plugins")) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		sb.append(project.getProperty("top.level.shared.dir"));
		sb.append("/");
		sb.append(_getJobName(buildURL));
		sb.append("/");
		sb.append(jobVariant);
		sb.append("/plugins-compile-failure");

		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());
		System.out.println(sb.toString());

		File file = new File(sb.toString());

		sb = new StringBuilder();

		if (file.exists()) {
			String content = JenkinsResultsParserUtil.read(file);

			String[] pluginNames = content.split(",");

			sb.append("<p>");
			sb.append(pluginNames.length);
			sb.append("plugin");

			if (pluginNames.length != 1) {
				sb.append("s");
			}

			sb.append(" failed to compile:</p>");
			sb.append("<ul>");

			int count = 0;

			for (String pluginName : pluginNames) {
				if (count == 5) {
					sb.append("<li>...</li>");

					break;
				}

				sb.append("<li>");
				sb.append(pluginName);
				sb.append("</li>");

				count++;
			}

			sb.append("</ul>");
		}
		else {
			sb.append("<p>To include a plugin fix for this pull request, please ");
			sb.append("edit your <a href=\"https://github.com/");
			sb.append(project.getProperty("github.pull.request.head.username"));
			sb.append("/");
			sb.append(project.getProperty("portal.repository"));
			sb.append("/blob/");
			sb.append(project.getProperty("github.pull.request.head.branch"));
			sb.append("/git-commit-plugins\">git-commit-plugins</a>. ");

			sb.append("Click <a href=\"https://in.liferay.com/web/");
			sb.append("global.engineering/blog/-/blogs/new-tests-for-the-pull-");
			sb.append("request-tester-\">here</a> for more details.</p>");

			int end = consoleOutput.indexOf("merge-test-results:");

			sb.append(getConsoleOutputSnippet(consoleOutput, true, end));
		}

		return sb.toString();
	}

	public static String fixFilePath(String filePath) {
		filePath = filePath.replace("%28", "(");
		filePath = filePath.replace("%29", ")");
		filePath = filePath.replace("%5B", "[");
		filePath = filePath.replace("%5D", "]");

		return filePath;
	}

	private String _getJobName(String buildURL) {
		String jobName = "";

		Pattern pattern = Pattern.compile(".*\\/([^\\/]+\\/.*)\\/\\d+");

		Matcher matcher = pattern.matcher(buildURL);

		if (matcher.find()) {
			jobName = matcher.group(1);
		}

		return fixFilePath(jobName);
	}

}