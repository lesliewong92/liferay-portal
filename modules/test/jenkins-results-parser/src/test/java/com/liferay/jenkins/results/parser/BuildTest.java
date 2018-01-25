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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Peter Yoo
 */
@RunWith(Parameterized.class)
public class BuildTest extends BaseJenkinsResultsParserTestCase {

	@Parameters(name = "{3}")
	public static List<String[]> getList() throws Exception {
		List<String[]> samplesList = new ArrayList<>();

		samplesList.add(
			new String[] {
				"117", "test-1-17", "test-jenkins-acceptance-pullrequest",
				"test-jenkins-acceptance-pullrequest_passed"
			});
		samplesList.add(
			new String[] {
				"66", "test-1-8",
				"test-plugins-acceptance-pullrequest(ee-6.2.x)",
				"test-plugins-acceptance-pullrequest(ee-6.2.x)_passed"
			});
		samplesList.add(
			new String[] {
				"103", "test-1-14", "test-portal-acceptance-pullrequest(7.0.x)",
				"test-portal-acceptance-pullrequest(7.0.x)" +
					"_unresolved-req-failure"
			});
		samplesList.add(
			new String[] {
				"1375", "test-1-1",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_generic-failure"
			});
		samplesList.add(
			new String[] {
				"999", "test-1-21",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_modules-compile-failure"
			});
		samplesList.add(
			new String[] {
				"446", "test-1-8", "test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_passed"
			});
		samplesList.add(
			new String[] {
				"1268", "test-1-9",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_poshi-test-failure"
			});
		samplesList.add(
			new String[] {
				"2003", "test-1-3",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_semantic_versioning_failure"
			});
		samplesList.add(
			new String[] {
				"2209", "test-1-2",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_source-format-failure"
			});

		return samplesList;
	}

	public BuildTest(
		String buildNumber, String hostName, String jobName, String sampleKey) {

		_buildNumber = buildNumber;
		_hostName = hostName;
		_jobName = jobName;
		_sampleKey = sampleKey;
	}

	@Before
	public void setUp() throws Exception {
		JenkinsResultsParserUtil.setBuildProperties(
			JenkinsResultsParserUtil.getBuildProperties());

		downloadSample(_sampleKey, _buildNumber, _jobName, _hostName);
	}

	@After
	public void tearDown() throws Exception {
		JenkinsResultsParserUtil.setBuildProperties((Hashtable<?, ?>)null);
	}

	@Test
	public void testGetGitHubMessage() throws Exception {
		assertSamples();
	}

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

	@Override
	protected String getMessage(File sampleDir) throws Exception {
		Build build = BuildFactory.newBuildFromArchive(
			"BuildTest/" + sampleDir.getName());

		build.setCompareToUpstream(false);

		return Dom4JUtil.format(build.getGitHubMessageElement(), true);
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

	private final String _buildNumber;
	private final String _hostName;
	private final String _jobName;
	private final String _sampleKey;

}