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
import java.io.FileOutputStream;
import java.io.StringReader;

import java.net.URL;

import java.util.Hashtable;
import java.util.Properties;

/**
 * @author Leslie Wong
 */
public abstract class BaseBuildTestCase extends BaseJenkinsResultsParserTestCase {

	@Override
	protected void downloadSample(File sampleDir, URL url) throws Exception {
		Build build = BuildFactory.newBuild(
			JenkinsResultsParserUtil.getLocalURL(url.toExternalForm()), null);

		build.archive(getSimpleClassName() + "/" + sampleDir.getName());
	}

	protected void downloadSample(
			String sampleKey, String buildNumber, String jobName,
			String hostName)
		throws Exception {

		String urlString =
			"https://${hostName}.liferay.com/job/${jobName}/${buildNumber}/";

		urlString = replaceToken(urlString, "buildNumber", buildNumber);
		urlString = replaceToken(urlString, "hostName", hostName);
		urlString = replaceToken(urlString, "jobName", jobName);

		URL url = JenkinsResultsParserUtil.createURL(urlString);

		downloadSample(sampleKey, url);
    }

	protected Properties loadProperties(String sampleName) throws Exception {
		Class<?> clazz = getClass();

		Properties properties = new Properties();

		String content = JenkinsResultsParserUtil.toString(
			JenkinsResultsParserUtil.getLocalURL(
				JenkinsResultsParserUtil.combine(
					"${dependencies.url}", clazz.getSimpleName(), "/",
					sampleName, "/sample.properties")));

		properties.load(new StringReader(content));

		return properties;
	}

	protected void saveProperties(File file, Properties properties)
		throws Exception {

		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			properties.store(fileOutputStream, null);
		}
	}

	@Override
	protected void writeExpectedMessage(File sampleDir) throws Exception {
		File expectedMessageFile = new File(sampleDir, "expected_message.html");

		Build build = BuildFactory.newBuildFromArchive(
			"BuildTest/" + sampleDir.getName());

		build.setCompareToUpstream(false);

		String expectedMessage = fixMessage(
			Dom4JUtil.format(build.getGitHubMessageElement()));

		JenkinsResultsParserUtil.write(expectedMessageFile, expectedMessage);
	}

}