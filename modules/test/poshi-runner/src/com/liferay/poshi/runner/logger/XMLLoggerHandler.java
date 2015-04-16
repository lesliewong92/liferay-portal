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

import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class XMLLoggerHandler {

	// public static LoggerElement generateLineContainerElement(
	// 	Element element) {

	// 	LoggerElement lineContainerElement = new LoggerElement();

	// 	lineContainerElement.setName("div");
	// 	lineContainerElement.setClassName("line-container");

	// 	LoggerElement lessThanElement = new LoggerElement();

	// 	lessThanElement.setName("span");
	// 	lessThanElement.setClassName("misc");
	// 	lessThanElement.setText("&lt;");

	// 	lineContainerElement.addChildLoggerElement(lessThanElement);

	// 	LoggerElement actionTypeElement = new LoggerElement();

	// 	actionTypeElement.setName("span");
	// 	actionTypeElement.setClassName("action-type");
	// 	actionTypeElement.setText(element.getName() + " ");

	// 	lineContainerElement.addChildLoggerElement(actionTypeElement);

	// 	List<Attribute> attributes = element.attributes();

	// 	lineContainerElement = generateAttributeElements(
	// 		attributes, lineContainerElement);

	// 	LoggerElement greaterThanElement = new LoggerElement();

	// 	greaterThanElement.setName("span");
	// 	greaterThanElement.setClassName("misc");
	// 	greaterThanElement.setText("&gt;");

	// 	lineContainerElement.addChildLoggerElement(greaterThanElement);

	// 	List<Element> elements = element.elements();

	// 	if (!elements.isEmpty() &&
	// 		(element.attributeValue("macro") != null ||
	// 		element.attributeValue("macro-desktop") != null ||
	// 		element.attributeValue("macro-mobile") != null)) {

	// 		lineContainerElement.addChildLoggerElement(
	// 			generateParameterElements(elements));
	// 	}

	// 	return lineContainerElement;
	// }

	public static void generateXMLLog(String classCommandName) {
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

	private static LoggerElement _getBtnContainerLoggerElement(
		Element element) {

		LoggerElement btnContainerLoggerElement = new LoggerElement();

		btnContainerLoggerElement.setClassName("btn-container");
		btnContainerLoggerElement.setName("div");

		btnContainerLoggerElement.addChildLoggerElement(
			_getLineNumberLoggerElement(
				element.attributeValue("line-number")));

		return btnContainerLoggerElement;
	}

	private static LoggerElement _getChildContainerLoggerElement() {
		LoggerElement childContainerLoggerElement = new LoggerElement();

		childContainerLoggerElement.setClassName(
			"child-container collapse collapsible");
		childContainerLoggerElement.setName("ul");

		return childContainerLoggerElement;
	}

	private static LoggerElement _getLineNumberLoggerElement(
		String lineNumber) {

		LoggerElement lineNumberLoggerElement = new LoggerElement();

		lineNumberLoggerElement.setClassName("line-number");
		lineNumberLoggerElement.setName("div");
		lineNumberLoggerElement.setText(lineNumber);

		return lineNumberLoggerElement;
	}

	private static LoggerElement _getParameterContainerLoggerElement(
		List<Element> elements) {

		LoggerElement parameterContainerElement = new LoggerElement();

		parameterContainerElement.setClassName(
			"parameter-container collapse collapsible");

		parameterContainerElement.setName("div");

		for(Element element : elements) {
			parameterContainerElement.addChildLoggerElement(
				_getLineNumberLoggerElement(
					element.attributeValue("line-number")));

			parameterContainerElement.addChildLoggerElement(
				generateLineContainerElement(element));
		}

		return parameterContainerElement;
	}

}