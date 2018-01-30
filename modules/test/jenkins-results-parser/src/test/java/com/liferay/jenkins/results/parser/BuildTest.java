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
import java.io.IOException;
import java.io.StringReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Peter Yoo
 */

public class BuildTest extends BaseJenkinsResultsParserTestCase {

	@BeforeClass
	public void setUp() throws Exception {
		JenkinsResultsParserUtil.setBuildProperties(
			JenkinsResultsParserUtil.getBuildProperties());

		TestSample testSample = new TestSample(
			"test-jenkins-acceptance-pullrequest_passed",
			getSampleURL(
				null, "117", "test-jenkins-acceptance-pullrequest",
				"test-1-17"),
			Arrays.asList(
				new TestSample.Report[] {TestSample.Report.GITHUB_MESSAGE}));

		downloadSample(
			"test-jenkins-acceptance-pullrequest_passed",
			getSampleURL(
				null, "117", "test-jenkins-acceptance-pullrequest",
				"test-1-17"));

		downloadSample(
			"test-plugins-acceptance-pullrequest(ee-6.2.x)_passed",
			getSampleURL(
				null, "66", "test-plugins-acceptance-pullrequest(ee-6.2.x)",
				"test-1-8"));

		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(7.0.x)_unresolved-req-failure",
		// 	getSampleURL(
		// 		null, "103", "test-portal-acceptance-pullrequest(7.0.x)",
		// 		"test-1-14"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)_generic-failure",
		// 	getSampleURL(
		// 		null, "1375", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-1"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)" +
		// 		"_modules-compile-failure",
		// 	getSampleURL(
		// 		null, "999", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-21"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)_passed",
		// 	getSampleURL(
		// 		null, "446", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-8"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)_poshi-test-failure",
		// 	getSampleURL(
		// 		null, "1268", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-9"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)" +
		// 		"_semantic_versioning_failure",
		// 	getSampleURL(
		// 		null, "2003", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-3"));
		// downloadSample(
		// 	"test-portal-acceptance-pullrequest(master)_source-format-failure",
		// 	getSampleURL(
		// 		null, "2209", "test-portal-acceptance-pullrequest(master)",
		// 		"test-1-2"));
	}

	@After
	public void tearDown() throws Exception {
		JenkinsResultsParserUtil.setBuildProperties((Hashtable<?, ?>)null);
	}

	@Test
	public void testGetGitHubMessage() throws Exception {
		// assertSamples("expected_message.html");

		File file = new File(
			dependenciesDir, "test-jenkins-acceptance-pullrequest_passed");

		assertSample(file, "expected_message.html");
	}

	@Test
	public void testGetJenkinsReport() throws Exception {
		assertSamples("expected_jenkins_report.html");
	}

	@Test
	public void testGetValidationGitHubMessage() throws Exception {
		assertSamples("expected_validation_message.html");
	}

	protected void downloadSample(
			String sampleKey, String buildNumber, String jobName,
			String hostName)
		throws Exception {

		downloadSample(
			sampleKey, getSampleURL(null, buildNumber, jobName, hostName));
	}

	@Override
	protected String getMessage(File sampleDir) throws Exception {
		return _report.getMessage(sampleDir);
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
		_report.writeExpectedMessage(sampleDir);
	}

	private final TestSample.Report _report = TestSample.Report.GITHUB_MESSAGE;

}