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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leslie Wong
 */
public class ValidationBuildTest extends BaseBuildTestCase {

	@Before
	public void setUp() throws Exception {
		JenkinsResultsParserUtil.setBuildProperties(
			JenkinsResultsParserUtil.getBuildProperties());

		downloadSample(
			"test-jenkins-acceptance-pullrequest_passed", "117",
			"test-jenkins-acceptance-pullrequest", "test-1-17");
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
	protected String getMessage(File sampleDir) throws Exception {
		TopLevelBuild build = (TopLevelBuild)BuildFactory.newBuildFromArchive(
			"BuildTest/" + sampleDir.getName());

		build.setCompareToUpstream(false);

		return Dom4JUtil.format(build.getValidationGitHubMessage(), true);
	}

}