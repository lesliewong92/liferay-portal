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

package com.liferay.poshi.runner.logger;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class XMLLoggerHandler {

	public static LoggerElement appendAttributesToLineContainer(
		Element element, LoggerElement lineContainerLoggerElement) {

		LoggerElement quoteLoggerElement = new LoggerElement();

		quoteLoggerElement.setClassName("misc quote");
		quoteLoggerElement.setName("span");
		quoteLoggerElement.setText("\"");

		List<Attribute> attributes = element.attributes();

		for(Attribute attribute : attributes) {
			String attributeName = attribute.getName();

			if(attributeName.equals("line-number")) {
				continue;
			}

			LoggerElement tagTypeLoggerElement = new LoggerElement();

			tagTypeLoggerElement.setClassName("tag-type");
			tagTypeLoggerElement.setName("span");
			tagTypeLoggerElement.setText(attributeName);

			lineContainerLoggerElement.addChildLoggerElement(
				tagTypeLoggerElement);

			LoggerElement equalsLoggerElement = new LoggerElement();

			equalsLoggerElement.setClassName("misc");
			equalsLoggerElement.setName("span");
			equalsLoggerElement.setText("=");

			lineContainerLoggerElement.addChildLoggerElement(
				equalsLoggerElement);

			lineContainerLoggerElement.addChildLoggerElement(
				quoteLoggerElement);

			LoggerElement nameLoggerElement = new LoggerElement();

			nameLoggerElement.setClassName("name");
			nameLoggerElement.setName("span");
			nameLoggerElement.setText(attribute.getValue());

			lineContainerLoggerElement.addChildLoggerElement(nameLoggerElement);
			lineContainerLoggerElement.addChildLoggerElement(
				quoteLoggerElement);
		}

		return lineContainerLoggerElement;
	}

	public static LoggerElement generateBtnContainerLoggerElement(
		Element element) {

		LoggerElement btnContainerLoggerElement = new LoggerElement();

		btnContainerLoggerElement.setClassName("btn-container");
		btnContainerLoggerElement.setName("div");

		if (element.attributeValue("line-number") != null) {
			LoggerElement lineNumberElement = new LoggerElement();

			lineNumberElement.setClassName("line-number");
			lineNumberElement.setName("div");
			lineNumberElement.setText(element.attributeValue("line-number"));

			btnContainerLoggerElement.addChildLoggerElement(lineNumberElement);
		}

		return btnContainerLoggerElement;
	}

	public static LoggerElement generateChildContainerLoggerElement() {
		LoggerElement childContainerLoggerElement = new LoggerElement();

		childContainerLoggerElement.setClassName(
			"child-container collapsible collapse");
		childContainerLoggerElement.setName("ul");

		return childContainerLoggerElement;
	}

	public static LoggerElement generateClosingLoggerElement(Element element) {
		LoggerElement lineContainerLoggerElement = new LoggerElement();

		lineContainerLoggerElement.setName("div");
		lineContainerLoggerElement.setClassName("line-container");

		LoggerElement lessThanLoggerElement = new LoggerElement();

		lessThanLoggerElement.setName("span");
		lessThanLoggerElement.setClassName("misc");
		lessThanLoggerElement.setText("&lt;/");

		lineContainerLoggerElement.addChildLoggerElement(lessThanLoggerElement);

		LoggerElement actionTypeLoggerElement = new LoggerElement();

		actionTypeLoggerElement.setName("span");
		actionTypeLoggerElement.setClassName("action-type");
		actionTypeLoggerElement.setText(element.getName());

		lineContainerLoggerElement.addChildLoggerElement(
			actionTypeLoggerElement);

		LoggerElement greaterThanLoggerElement = new LoggerElement();

		greaterThanLoggerElement.setName("span");
		greaterThanLoggerElement.setClassName("misc");
		greaterThanLoggerElement.setText("&gt;");

		lineContainerLoggerElement.addChildLoggerElement(
			greaterThanLoggerElement);

		return lineContainerLoggerElement;
	}

	public static LoggerElement generateLineContainerLoggerElement(
		Element element) {

		LoggerElement lineContainerLoggerElement = new LoggerElement();

		lineContainerLoggerElement.setClassName("line-container");
		lineContainerLoggerElement.setName("div");

		LoggerElement lessThanLoggerElement = new LoggerElement();

		lessThanLoggerElement.setClassName("misc");
		lessThanLoggerElement.setName("span");
		lessThanLoggerElement.setText("&lt;");

		lineContainerLoggerElement.addChildLoggerElement(lessThanLoggerElement);

		LoggerElement actionTypeLoggerElement = new LoggerElement();

		actionTypeLoggerElement.setClassName("action-type");
		actionTypeLoggerElement.setName("span");
		actionTypeLoggerElement.setText(element.getName());

		lineContainerLoggerElement.addChildLoggerElement(
			actionTypeLoggerElement);

		lineContainerLoggerElement = appendAttributesToLineContainer(
			element, lineContainerLoggerElement);

		LoggerElement greaterThanLoggerElement = new LoggerElement();

		greaterThanLoggerElement.setClassName("misc");
		greaterThanLoggerElement.setName("span");
		greaterThanLoggerElement.setText("&gt;");

		lineContainerLoggerElement.addChildLoggerElement(
			greaterThanLoggerElement);

		return lineContainerLoggerElement;
	}

	public static LoggerElement generateLoggerElement(Element element) {
		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setName("li");

		String elementName = element.getName();

		if (elementName.equals("execute")) {
			Attribute attribute = element.attribute(1);

			loggerElement.setClassName(attribute.getName());
		}

		loggerElement.addChildLoggerElement(
			generateBtnContainerLoggerElement(element));
		loggerElement.addChildLoggerElement(
			generateLineContainerLoggerElement(element));

		List<Element> childElements = element.elements();

		if (!childElements.isEmpty()) {
			LoggerElement childContainerLoggerElement =
				generateChildContainerLoggerElement();

			for (Element childElement : childElements) {
				childContainerLoggerElement.addChildLoggerElement(
					generateLoggerElement(childElement));
			}

			loggerElement.addChildLoggerElement(childContainerLoggerElement);
			loggerElement.addChildLoggerElement(
				generateClosingLoggerElement(element));
		}

		return loggerElement;
	}

	public static void generateXMLLog(String classCommandName, Element element) {
		LoggerElement xmlLoggerElement = new LoggerElement();

		xmlLoggerElement.setClassName("header");
		xmlLoggerElement.setName("li");

		LoggerElement btnContainerLoggerElement = new LoggerElement();

		btnContainerLoggerElement.setClassName("btn-container");
		btnContainerLoggerElement.setName("div");

		LoggerElement btnLoggerElement = new LoggerElement();

		btnLoggerElement.setClassName("btn btn-collapse");
		btnLoggerElement.setName("button");

		btnContainerLoggerElement.addChildLoggerElement(btnLoggerElement);

		xmlLoggerElement.addChildLoggerElement(btnContainerLoggerElement);

		LoggerElement lineContainerLoggerElement = new LoggerElement();

		lineContainerLoggerElement.setClassName("line-container");
		lineContainerLoggerElement.setName("div");

		LoggerElement lineLoggerElement = new LoggerElement();

		lineLoggerElement.setClassName("test-case-command");
		lineLoggerElement.setName("h3");
		lineLoggerElement.setText(classCommandName);

		lineContainerLoggerElement.addChildLoggerElement(lineLoggerElement);

		xmlLoggerElement.addChildLoggerElement(lineContainerLoggerElement);
	}

}