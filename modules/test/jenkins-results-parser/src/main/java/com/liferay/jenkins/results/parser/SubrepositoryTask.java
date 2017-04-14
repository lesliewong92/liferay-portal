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

import java.io.IOException;

import java.util.Properties;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public abstract class SubrepositoryTask {

	public SubrepositoryTask() {
	}

	public Element getTaskSummaryListIndexElement() {
		Properties buildProperties;

		try {
			buildProperties = JenkinsResultsParserUtil.getBuildProperties();
		}
		catch (IOException ioe) {
			throw new RuntimeException("Unable to get build properties");
		}

		String logFileName =
			buildProperties.getProperty("top.level.user.content.url") + "/" +
				taskName + ".log";

		Element element = Dom4JUtil.getNewElement(
			"li", null, Dom4JUtil.getNewAnchorElement(logFileName, taskName));

		if (result.equals("SUCCESS")) {
			Dom4JUtil.addToElement(element, " - :white_check_mark:");
		}

		if (result.equals("ABORTED")) {
			Dom4JUtil.addToElement(
				element, " -  :no_entry:", getFailureMessageElement());
		}

		if (result.equals("FAILURE")) {
			Dom4JUtil.addToElement(
				element, " -  :x:", getFailureMessageElement());
		}

		return element;
	}

	public String getResult() {
		return result;
	}

	protected abstract Element getFailureMessageElement();

	protected static String result;
	protected static String taskName;

}