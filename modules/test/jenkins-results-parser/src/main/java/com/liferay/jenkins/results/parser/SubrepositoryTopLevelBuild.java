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

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class SubrepositoryTopLevelBuild extends TopLevelBuild {

	public SubrepositoryTopLevelBuild(String url) {
		this(url, null);
	}

	public SubrepositoryTopLevelBuild(String url, TopLevelBuild topLevelBuild) {
		super(url, topLevelBuild);
	}

	public Element getValidationGitHubMessage() {
		if (validationBuild == null) {
			throw new RuntimeException("Unable to find a validation build");
		}

		return validationBuild.getGitHubMessageElement();
	}

	@Override
	protected void findDownstreamBuilds() {
		super.findDownstreamBuilds();

		boolean validationBuildDetected = false;

		for (Build downstreamBuild : downstreamBuilds) {
			if (downstreamBuild instanceof ValidationBuild) {
				validationBuild = (ValidationBuild)downstreamBuild;
			}
		}

		if (validationBuildDetected) {
			downstreamBuilds.remove(validationBuild);
		}
	}

	protected ValidationBuild getValidationBuild() {
		return validationBuild;
	}

	protected ValidationBuild validationBuild;

}