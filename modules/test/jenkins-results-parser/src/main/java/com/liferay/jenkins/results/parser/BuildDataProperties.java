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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Peter Yoo
 */
public class BuildDataProperties {

	public BuildDataProperties() {
	}

	public BuildDataProperties(String jsonString) {
		JSONObject jsonObject = new JSONObject(jsonString);

		JSONObject downstreamsJSONObject = jsonObject.getJSONObject(
			"downstreams");

		Iterator<String> jobNames = downstreamsJSONObject.keys();

		while(jobNames.hasNext()) {
			String jobName = jobNames.next();
			JSONObject downstreamJobJSONObject =
				downstreamsJSONObject.getJSONObject(jobName);

			Map<String, Properties> downstreamJobProperties = new HashMap<>();

			Iterator<String> jobVariants = downstreamJobJSONObject.keys();

			while(jobVariants.hasNext()) {
				String jobVariant = jobVariants.next();

				downstreamJobProperties.put(
					jobVariant,
					_getProperties(
						downstreamJobJSONObject.getJSONObject(jobVariant)));
			}

			_downstreamsPropertiesMap.put(jobName, downstreamJobProperties);
		}

		JSONObject repositoriesJSONObject = jsonObject.getJSONObject(
			"repositories");

		Iterator<String> repositoryTypes = repositoriesJSONObject.keys();

		while(repositoryTypes.hasNext()) {
			String repositoryType = repositoryTypes.next();

			_repositoryPropertiesMap.put(
				repositoryType,
				_getProperties(
					repositoriesJSONObject.getJSONObject(repositoryType)));
		}

		_startProperties = _getProperties(jsonObject.getJSONObject("start"));
	}

	public void addDownstreamProperties(
		String jobName, String jobVariant, Properties properties) {

		Map<String, Properties> downstreamJobProperties = new HashMap<>();

		if (_downstreamsPropertiesMap.containsKey(jobName)) {
			downstreamJobProperties = _downstreamsPropertiesMap.get(jobName);
		}

		downstreamJobProperties.put(jobVariant, properties);

		_downstreamsPropertiesMap.put(jobName, downstreamJobProperties);
	}

	public void addRepositoryProperties(
		String repositoryType, Properties properties) {

		_repositoryPropertiesMap.put(repositoryType, properties);
	}

	public void addStartProperties(Properties properties) {
		_startProperties = properties;
	}

	public Properties getDownstreamProperties(
		String jobName, String jobVariant) {

		if (!_downstreamsPropertiesMap.containsKey(jobName)) {
			throw new RuntimeException(
				"No properties are available for job '" + jobName + "'");
		}

		Map<String, Properties> downstreamProperties =
			_downstreamsPropertiesMap.get(jobName);

		return downstreamProperties.get(jobVariant);
	}

	public Properties getStartProperties() {
		return _startProperties;
	}

	// public Object getDownstreamProperty(
	// 	String jobName, String jobVariant, String propertyName) {
	// }

	// public Properties getRepositoryProperties(String repositoryType) {
	// 	return _repositoryPropertiesMap.get(repositoryType);
	// }

	// public Object getRepositoryProperty(
	// 	String repositoryType, String propertyName) {

	// }

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		JSONObject downstreamsJSONObject = new JSONObject();

		for (Map.Entry<String, Map<String, Properties>> downstreamJobEntry :
				_downstreamsPropertiesMap.entrySet()) {

			JSONObject downstreamJobJSONObject = new JSONObject();

			Map<String, Properties> downstreamProperties =
				downstreamJobEntry.getValue();

			for (Map.Entry<String, Properties> jobVariantEntry :
					downstreamProperties.entrySet()) {

				downstreamJobJSONObject.put(
					jobVariantEntry.getKey(),
					toJSONObject(jobVariantEntry.getValue()));
			}

			String jobName = downstreamJobEntry.getKey();

			downstreamsJSONObject.put(jobName, downstreamJobJSONObject);
		}

		jsonObject.put("downstreams", downstreamsJSONObject);

		JSONObject repositoriesJSONObject = new JSONObject();

		for (Map.Entry<String, Properties> repositoryPropertiesEntry :
				_repositoryPropertiesMap.entrySet()) {

			repositoriesJSONObject.put(
				repositoryPropertiesEntry.getKey(),
				toJSONObject(repositoryPropertiesEntry.getValue()));
		}

		jsonObject.put("repositories", repositoriesJSONObject);

		jsonObject.put("start", toJSONObject(_startProperties));

		return jsonObject;
	}

	public JSONObject toJSONObject(Properties properties) {
		JSONArray jsonArray = new JSONArray();

		int i = 0;

		for (String propertyName : properties.stringPropertyNames()) {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("name", propertyName);
			jsonObject.put("value", properties.getProperty(propertyName));

			jsonArray.put(i, jsonObject);

			i++;
		}

		JSONObject jsonObject = new JSONObject();

		return jsonObject.put("properties", jsonArray);
	}

	public String toJSONString() {
		JSONObject jsonObject = toJSONObject();

		return jsonObject.toString();
	}

	private Properties _getProperties(JSONObject jsonObject) {
		Properties properties = new Properties();

		JSONArray jsonArray = jsonObject.getJSONArray("properties");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject propertyJSONObject = jsonArray.getJSONObject(i);

			properties.put(
				propertyJSONObject.getString("name"),
				propertyJSONObject.getString("value"));
		}

		return properties;
	}

	private Map<String, Map<String, Properties>> _downstreamsPropertiesMap =
		new HashMap<>();
	private Map<String, Properties> _repositoryPropertiesMap = new HashMap<>();
	private Properties _startProperties = new Properties();

}