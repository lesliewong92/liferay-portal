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
import java.io.IOException;

import java.net.URL;

import java.util.List;

/**
 * @author Leslie Wong
 */
public class TestSample {

	public TestSample(String sampleKey, URL url, List<Report> reports) {
		_sampleKey = sampleKey;
		_url = url;
		_reports = reports;

		archiveBuild();
	}

	@Override
	protected void archiveBuild() throws Exception {
		Build build = BuildFactory.newBuild(
			JenkinsResultsParserUtil.getLocalURL(_url.toExternalForm()), null);

		build.archive(getSimpleClassName() + "/" + _sampleDir.getName());
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

			public void downloadSample() {

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

	private List<Report> _reports;
	private String _sampleKey;
	private URL _url;

}