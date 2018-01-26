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
import org.junit.Assume;
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
	public static List<Object[]> getList() throws Exception {
		List<Object[]> samplesList = new ArrayList<>();

		samplesList.add(
			new Object[] {
				"117", "test-1-17", "test-jenkins-acceptance-pullrequest",
				"test-jenkins-acceptance-pullrequest_passed",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"66", "test-1-8",
				"test-plugins-acceptance-pullrequest(ee-6.2.x)",
				"test-plugins-acceptance-pullrequest(ee-6.2.x)_passed",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"103", "test-1-14", "test-portal-acceptance-pullrequest(7.0.x)",
				"test-portal-acceptance-pullrequest(7.0.x)" +
					"_unresolved-req-failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"1375", "test-1-1",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_generic-failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"999", "test-1-21",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_modules-compile-failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"446", "test-1-8", "test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_passed",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"1268", "test-1-9",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)_poshi-test-failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"2003", "test-1-3",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_semantic_versioning_failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});
		samplesList.add(
			new Object[] {
				"2209", "test-1-2",
				"test-portal-acceptance-pullrequest(master)",
				"test-portal-acceptance-pullrequest(master)" +
					"_source-format-failure",
				Arrays.asList(new Report[] {Report.GITHUB_MESSAGE})
			});

		return samplesList;
	}

	public BuildTest(
		String buildNumber, String hostName, String jobName, String sampleKey,
		List<Report> reports) {

		_buildNumber = buildNumber;
		_hostName = hostName;
		_jobName = jobName;
		_sampleKey = sampleKey;
		_reports = reports;
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
		Assume.assumeTrue(_reports.contains(Report.GITHUB_MESSAGE));

		_report = Report.GITHUB_MESSAGE;

		File sampleDir = new File(dependenciesDir, _sampleKey);

		assertSample(sampleDir);
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

		downloadSample(
			sampleKey,
			getSampleURL(sampleKey, null, buildNumber, jobName, hostName));
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
		for (Report report : _reports) {
			report.writeExpectedMessage(sampleDir);
		}
	}

	protected enum Report {

		GITHUB_MESSAGE {

			@Override
			public String getMessage(File sampleDir) throws IOException {
				Build build = BuildFactory.newBuildFromArchive(
					"BuildTest/" + sampleDir.getName());

				build.setCompareToUpstream(false);

				return Dom4JUtil.format(build.getGitHubMessageElement(), true);
			}

			@Override
			public void writeExpectedMessage(File sampleDir)
				throws IOException {

				writeExpectedMessage(sampleDir, "expected_message.html");
			}

		},

		JENKINS_REPORT {

			@Override
			public String getMessage(File sampleDir) throws IOException {
				TopLevelBuild topLevelBuild =
					(TopLevelBuild)BuildFactory.newBuildFromArchive(
						"BuildTest/" + sampleDir.getName());

				return Dom4JUtil.format(
					topLevelBuild.getJenkinsReportElement(), true);
			}

			@Override
			public void writeExpectedMessage(File sampleDir)
				throws IOException {

				writeExpectedMessage(sampleDir, "expected_jenkins_report.html");
			}

		},

		VALIDATION_GITHUB_MESSAGE {

			@Override
			public String getMessage(File sampleDir) throws IOException {
				TopLevelBuild topLevelBuild =
					(TopLevelBuild)BuildFactory.newBuildFromArchive(
						"BuildTest/" + sampleDir.getName());

				return Dom4JUtil.format(
					topLevelBuild.getValidationGitHubMessage(), true);
			}

			@Override
			public void writeExpectedMessage(File sampleDir)
				throws IOException {

				writeExpectedMessage(
					sampleDir, "expected_validation_message.html");
			}

		};

		public abstract String getMessage(File sampleDir) throws IOException;

		public abstract void writeExpectedMessage(File sampleDir)
			throws IOException;

		public void writeExpectedMessage(File sampleDir, String fileName)
			throws IOException {

			File expectedMessageFile = new File(sampleDir, fileName);

			String expectedMessage = getMessage(sampleDir);

			JenkinsResultsParserUtil.write(
				expectedMessageFile, expectedMessage);
		}

	};

	private final String _buildNumber;
	private final String _hostName;
	private final String _jobName;
	private Report _report;
	private final List<Report> _reports;
	private final String _sampleKey;

}