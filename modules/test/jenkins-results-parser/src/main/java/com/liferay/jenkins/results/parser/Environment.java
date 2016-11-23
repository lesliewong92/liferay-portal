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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.tools.ant.Project;

/**
 * @author Kevin Yen
 */
public class Environment {

	public Environment(
		Project project, String batchParameter, String environmentType) {

		Map<String, String> propertiesMap = new HashMap<>();

		Hashtable<String, Object> properties = project.getProperties();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			propertiesMap.put(entry.getKey(), (String)entry.getValue());
		}

		init(propertiesMap, batchParameter, environmentType);
	}

	public Environment(
		Properties properties, String batchParameter, String environmentType) {

		Map<String, String> propertiesMap = new HashMap<>();

		for(Map.Entry<Object, Object> entry : properties.entrySet()) {
		    propertiesMap.put((String)entry.getKey(), (String)entry.getValue());
		}

		init(propertiesMap, batchParameter, environmentType);
	}

	public Environment(
			Map<String, String> properties, String batchParameter,
		String environmentType) {

		init(properties, batchParameter, environmentType);
	}

	protected void init(
			Map<String, String> properties, String batchParameter,
		String environmentType) {

		Matcher javaEnvironmentMatcher = javaEnvironmentPattern.matcher(
			batchParameter);

		_type = environmentType;

		if (javaEnvironmentMatcher.find()) {
			_name = "jdk";
			_version = javaEnvironmentMatcher.group("version");

			_factor = properties.get(
				"env.option.java.jdk.x64." + _version);

			return;
		}

		Pattern environmentOptionPattern = Pattern.compile(
			"env\\.option\\." + environmentType + "\\.(?<tag>" +
				batchParameter + ".*)");

		for (String propertyName : properties.keySet()) {
			Matcher environmentOptionMatcher = environmentOptionPattern.matcher(
				propertyName);

			if (environmentOptionMatcher.find()) {
				_factor = properties.get(propertyName);

				String tag = environmentOptionMatcher.group("tag");

				Matcher environmentMatcher = environmentPattern.matcher(tag);

				if (environmentMatcher.find()) {
					_name = environmentMatcher.group("shortName");
					_version = environmentMatcher.group("version");
				}

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

	protected static final Pattern environmentPattern = Pattern.compile(
		"(?<shortName>[A-Za-z]+)(?<version>[0-9]*.*)");
	protected static final Pattern javaEnvironmentPattern = Pattern.compile(
		"jdk(?<version>[0-9]+)");
	protected static final Pattern majorVersionPattern = Pattern.compile(
		"((\\d+)\\.?(\\d+?)).*");

	private String _factor;
	private String _name;
	private String _type;
	private String _version;

}