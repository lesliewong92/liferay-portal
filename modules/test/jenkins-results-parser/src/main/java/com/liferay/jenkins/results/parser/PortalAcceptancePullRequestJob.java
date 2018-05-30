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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Michael Hashimoto
 */
public class PortalAcceptancePullRequestJob
	extends PortalRepositoryJob implements PortalTestClassJob, TestSuiteJob {

	public PortalAcceptancePullRequestJob(String url) {
		this(url, "default");
	}

	public PortalAcceptancePullRequestJob(String url, String testSuiteName) {
		super(url);

		_testSuiteName = testSuiteName;
	}

	@Override
	public Set<String> getBatchNames() {
		String testBatchNames = JenkinsResultsParserUtil.getProperty(
			jobProperties, "test.batch.names[" + _testSuiteName + "]");

		if (testBatchNames == null) {
			testBatchNames = JenkinsResultsParserUtil.getProperty(
				jobProperties, "test.batch.names");
		}

		Set<String> testBatchNamesSet = getSetFromString(testBatchNames);

		if (_isPortalWebOnly()) {
			String[] portalWebOnlyBatchNameMarkers =
				{"compile-jsp", "functional", "portal-web", "source-format"};

			Set<String> portalWebOnlyBatchNamesSet = new TreeSet<>();

			for (String testBatchName : testBatchNamesSet) {
				for (String portalWebOnlyBatchNameMarker :
						portalWebOnlyBatchNameMarkers) {

					if (testBatchName.contains(portalWebOnlyBatchNameMarker)) {
						portalWebOnlyBatchNamesSet.add(testBatchName);

						break;
					}
				}
			}

			return portalWebOnlyBatchNamesSet;
		}

		try {
			PortalGitWorkingDirectory portalGitWorkingDirectory =
				(PortalGitWorkingDirectory)getGitWorkingDirectory();

			List<File> modifiedModuleDirsList =
				portalGitWorkingDirectory.getModifiedModuleDirsList();

			if (modifiedModuleDirsList.isEmpty()) {
				Set<String> noModulesChangeBatchNamesSet = new TreeSet<>();

				for (String testBatchName : testBatchNamesSet) {
					if (!testBatchName.contains("modules-compile")) {
						noModulesChangeBatchNamesSet.add(testBatchName);
					}
				}

				return noModulesChangeBatchNamesSet;
			}
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Failure occured when searching for modified modules", ioe);
		}

		return testBatchNamesSet;
	}

	@Override
	public Set<String> getDistTypes() {
		String testBatchDistAppServers = JenkinsResultsParserUtil.getProperty(
			jobProperties,
			"test.batch.dist.app.servers[" + _testSuiteName + "]");

		if (testBatchDistAppServers == null) {
			testBatchDistAppServers = JenkinsResultsParserUtil.getProperty(
				jobProperties, "test.batch.dist.app.servers");
		}

		return getSetFromString(testBatchDistAppServers);
	}

	@Override
	public String getPoshiQuery(String testBatchName) {
		String[] propertyNames = {
			JenkinsResultsParserUtil.combine(
				"test.batch.run.property.query[", testBatchName, "][",
				_testSuiteName, "]"),

			JenkinsResultsParserUtil.combine(
				"test.batch.run.property.query[", testBatchName, "]")
		};

		for (String propertyName : propertyNames) {
			if (jobProperties.containsKey(propertyName)) {
				String propertyValue = JenkinsResultsParserUtil.getProperty(
					jobProperties, propertyName);

				if ((propertyValue != null) && !propertyValue.isEmpty()) {
					return propertyValue;
				}
			}
		}

		return null;
	}

	@Override
	public String getTestSuiteName() {
		return _testSuiteName;
	}

	private boolean _isPortalWebOnly() {
		GitWorkingDirectory gitWorkingDirectory = getGitWorkingDirectory();

		File portalWebDirectory = new File(
			gitWorkingDirectory.getWorkingDirectory(), "portal-web");

		for (File modifiedFile : gitWorkingDirectory.getModifiedFilesList()) {
			if (!JenkinsResultsParserUtil.isFileInDirectory(
					portalWebDirectory, modifiedFile)) {

				return false;
			}
		}

		return true;
	}

	private final String _testSuiteName;

}