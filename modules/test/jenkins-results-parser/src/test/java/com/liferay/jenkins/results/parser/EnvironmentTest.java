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
import java.io.FileReader;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kevin Yen
 */
public class EnvironmentTest {

	private List<String> environmentTypes = Arrays.asList(
		"app.server", "browser", "database", "java.jdk", "operating.system");

	@Test
	public void testEnvironment() throws Exception {
		File testPropertiesFile = new File("../../../test.properties");

		File appServerPropertiesFile = new File(
			"../../../app.server.properties");

		Properties properties = new Properties();

		properties.load(new FileReader(testPropertiesFile));
		properties.load(new FileReader(appServerPropertiesFile));

		Environment environment;

		environment = new Environment(properties, "mysql56", "database");

		Assert.assertEquals("MySQL 5.6", environment.getFactor());
		Assert.assertEquals("mysql", environment.getName());
		Assert.assertEquals("database", environment.getType());
		Assert.assertEquals("56", environment.getVersion());

		environment = new Environment(properties, "tomcat8", "app.server");

		Assert.assertEquals("Tomcat 8.0", environment.getFactor());
		Assert.assertEquals("tomcat", environment.getName());
		Assert.assertEquals("app.server", environment.getType());
		Assert.assertEquals("80", environment.getVersion());

		environment = new Environment(properties, "weblogic121", "app.server");

		Assert.assertEquals("Weblogic 12c", environment.getFactor());
		Assert.assertEquals("weblogic", environment.getName());
		Assert.assertEquals("app.server", environment.getType());
		Assert.assertEquals("121", environment.getVersion());

		environment = new Environment(properties, "jdk7", "java.jdk");

		Assert.assertEquals("Java JDK 7 64-Bit", environment.getFactor());
		Assert.assertEquals("jdk", environment.getName());
		Assert.assertEquals("java.jdk", environment.getType());
		Assert.assertEquals("7", environment.getVersion());
	}

}