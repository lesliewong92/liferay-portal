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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import org.apache.tools.ant.Project;

/**
 * @author Kevin Yen
 */
public class Environment {

	public Environment(
		Project project, String batchName, String environmentType) {

		Map<String, String> propertiesMap = new HashMap<>();

		Hashtable<String, Object> properties = project.getProperties();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			propertiesMap.put(entry.getKey(), (String)entry.getValue());
		}

		init(propertiesMap, batchName, environmentType);
	}

	public Environment(
		Properties properties, String batchName, String environmentType) {

		Map<String, String> propertiesMap = new HashMap<>();

		for(Map.Entry<Object, Object> entry : properties.entrySet()) {
		    propertiesMap.put((String)entry.getKey(), (String)entry.getValue());
		}

		init(propertiesMap, batchName, environmentType);
	}

	public Environment(
			Map<String, String> properties, String batchName,
		String environmentType) {

		init(properties, batchName, environmentType);
	}

	protected void init(
			Map<String, String> properties, String batchName,
		String environmentType) {

		_type = environmentType;

		if (environmentType.equals("java.jdk")) {
			_name = "x32";

			String batchComponent = getBatchComponent(batchName, "jdk");

			_version = batchName.substring(3);

			_factor = properties.get("env.option.java.jdk.x32." + _version);

			return;
		}

		List<String> environmentOptions = new ArrayList(
			StringUtils.split(properties.get(environmentType + ".types"), ","));

		for (String environmentOption : environmentOptions) {
			if (batchName.contains(environmentOption)) {
				_name = environmentOption;

				String batchComponent = getBatchComponent(
					batchName, environmentOption);

				_factor = _factor = properties.get(
					"env.option." + environmentType + "." + batchComponent);

				_version = batchComponent.substring(environmentOption.length());

				return;
			}
		}

		String _name = properties.get(environmentType + ".type");

		String environmentVersion = properties.get(
			environmentType + ".version");

		Matcher matcher = majorVersionPattern.matcher(environmentVersion);

		String environmentMajorVersion;

		if (matcher.matches()) {
			environmentMajorVersion = matcher.group(1);
		}
		else {
			environmentMajorVersion = environmentVersion;
		}

		_version = environmentMajorVersion.replace(".", "");

		_factor = properties.get("env.option." + environmentType + "." + _name
			+ _version);
	}

	protected String getBatchComponent(
		String batchName, String environmentOption) {

		int x = batchName.indexOf(environmentOption);
		int y = batchName.indexOf("-", x);

		if (y == -1) {
			y = batchName.length();
		}

		return batchName.substring(x, y);
	}

	public String getFactor() {
		return _factor;
	}

	public String getName() {
		return _name;
	}

	public String getType() {
		return _type;
	}

	public String getVersion() {
		return _version;
	}

	private String _factor;
	private String _name;
	private String _type;
	private String _version;

}