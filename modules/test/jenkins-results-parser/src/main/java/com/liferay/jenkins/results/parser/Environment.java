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

public class Environment {
    public Environment(String batchName) {
		Properties buildProperties = null;

		try {
			buildProperties = JenkinsResultsParserUtil.getBuildProperties();
		}
		catch (IOException ioe) {
			throw new RuntimeException("Unable to get build properties", ioe);
		}

        _appServer = _getEnvironment(batchName, buildProperties, "app.server");
        _browser = _getEnvironment(batchName, buildProperties, "browser");
        _database = _getEnvironment(batchName, buildProperties, "database");
        _jdk = _getEnvironment(batchName, buildProperties, "java.jdk");
        _operatingSystem = _getEnvironment(
            batchName, buildProperties, "operating.system");

        environmentHash = getEnvironmentHash();
    }

    protected String getEnvironment(
        String batchName, Properties buildProperties, String environmentType) {

		List<String> environmentOptions = new ArrayList<>(
			Arrays.asList(
				StringUtils.split(
					buildProperties.getProperty(environmentType + ".types"),
					",")));

		for (String environmentOption : environmentOptions) {
			if (batchName.contains(environmentOption)) {
				String batchComponent = getBatchComponent(
					batchName, environmentOption);

				return buildProperties.getProperty(
					"env.option." + environmentType + "." + batchComponent);
			}
		}

		String name = buildProperties.getProperty(environmentType + ".type");

		String environmentVersion = (String)buildProperties.get(
			environmentType + "." + name + ".version");

		Matcher matcher = majorVersionPattern.matcher(
			buildProperties.getProperty(
				environmentType + "." + name + ".version"));

		String environmentMajorVersion;

		if (matcher.matches()) {
			environmentMajorVersion = matcher.group(1);
		}
		else {
			environmentMajorVersion = environmentVersion;
		}

		if (environmentType.equals("java.jdk")) {
			return buildProperties.getProperty(
				"env.option." + environmentType + "." + name + "." +
					environmentMajorVersion.replace(".", ""));
		}
		else {
			return buildProperties.getProperty(
				"env.option." + environmentType + "." + name +
					environmentMajorVersion.replace(".", ""));
		}
	}

    protected String getEnvironmentHash(Properties properties)
		throws Exception {

		List<Map<String, Object>> environmentOptions = DBUtil.executeQuery(
			new ArrayList<>(),
			properties.getProperty("testray.integration.database.type"),
			properties.getProperty("testray.integration.database.password"),
			properties.getProperty("testray.integration.environment.query"),
			properties.getProperty("testray.integration.database.url"),
			properties.getProperty("testray.integration.database.user.name"));

		StringBuilder sb = new StringBuilder();

		for (String environment : environments) {
			for (Map<String, Object> environmentOption : environmentOptions) {
				String name = (String)environmentOption.get("name");

				if (environment.equals(name)) {
					sb.append(environmentOption.get("testrayFactorCategoryId"));
					sb.append(environmentOption.get("testrayFactorOptionId"));

					break;
				}
			}
		}

		String testrayFactors = sb.toString();

		return String.valueOf(testrayFactors.hashCode());
	}

    public String environmentHash;
    private String _appServer;
    private String _browser;
    private String _database;
    private String _jdk;
    private String _operatingSystem;
}