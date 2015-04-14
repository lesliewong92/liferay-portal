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

import com.liferay.poshi.runner.PoshiRunnerContext;
import com.liferay.poshi.runner.PoshiRunnerException;
import com.liferay.poshi.runner.PoshiRunnerGetterUtil;
import com.liferay.poshi.runner.PoshiRunnerStackTraceUtil;
import com.liferay.poshi.runner.PoshiRunnerVariablesUtil;
import com.liferay.poshi.runner.util.FileUtil;
import com.liferay.poshi.runner.util.PropsValues;
import com.liferay.poshi.runner.util.StringUtil;
import com.liferay.poshi.runner.util.Validator;

import java.util.List;
import java.util.Stack;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class XMLLoggerHandler {

	public static void createXMLLogFile(String xmlLogContent) throws Exception {
		String loggerContent = FileUtil.read(
			"src/com/liferay/poshi/runner/logger/dependencies/index.html");

		loggerContent = loggerContent.replace(
			"<!-- insert content here -->", xmlLogContent);

		FileUtil.write("test-results/html/index.html", loggerContent);
	}

	public static LoggerElement generateAttributeElements(
		List<Attribute> attributes, LoggerElement containerElement) {

		//TODO : Make explicit spaces within the line container

		LoggerElement quoteElement1 = new LoggerElement();

		quoteElement1.setName("span");
		quoteElement1.setClassName("misc quote");
		quoteElement1.setText("\"");

		LoggerElement quoteElement2 = new LoggerElement();

		quoteElement2.setName("span");
		quoteElement2.setClassName("misc quote");
		quoteElement2.setText("\" ");

		for(Attribute attribute : attributes) {
			String attributeName = attribute.getName();

			if(attributeName.equals("line-number")) {
				continue;
			}

			LoggerElement miniElement1 = new LoggerElement();

			miniElement1.setName("span");
			miniElement1.setClassName("tag-type");
			miniElement1.setText(attributeName);

			containerElement.addChildLoggerElement(miniElement1);

			LoggerElement equalsElement = new LoggerElement();

			equalsElement.setName("span");
			equalsElement.setClassName("misc");
			equalsElement.setText("=");

			containerElement.addChildLoggerElement(equalsElement);
			containerElement.addChildLoggerElement(quoteElement1);

			LoggerElement miniElement2 = new LoggerElement();

			miniElement2.setName("span");
			miniElement2.setClassName("name");
			miniElement2.setText(attribute.getValue());

			containerElement.addChildLoggerElement(miniElement2);
			containerElement.addChildLoggerElement(quoteElement2);
		}

		return containerElement;
	}

	public static LoggerElement generateBtnContainerElement(Element element) {
		LoggerElement btnContainerElement = new LoggerElement();

		btnContainerElement.setName("div");
		btnContainerElement.setClassName("btn-container");

		if (element.attributeValue("line-number") != null) {
			LoggerElement lineNumberElement = new LoggerElement();

			lineNumberElement.setName("div");
			lineNumberElement.setClassName("line-number");
			lineNumberElement.setText(element.attributeValue("line-number"));

			btnContainerElement.addChildLoggerElement(lineNumberElement);
		}

		List<Element> childElements = element.elements();

		if (!childElements.isEmpty()) {
			LoggerElement btnElement = new LoggerElement();

			btnElement.setName("button");
			btnElement.setClassName("btn btn-collapse toggle");
			btnElement.setButtonLinkId(_buttonLinkId);

			_buttonIdStack.push(_buttonLinkId);

			System.out.println("Added " + _buttonLinkId);

			_buttonLinkId++;

			btnContainerElement.addChildLoggerElement(btnElement);

			if (element.attributeValue("macro") != null ||
			element.attributeValue("macro-desktop") != null ||
			element.attributeValue("macro-mobile") != null) {

				LoggerElement varBtnElement = new LoggerElement();

				varBtnElement.setName("button");
				varBtnElement.setClassName("btn btn-var");
				varBtnElement.setButtonLinkId(_buttonLinkId);

				_buttonIdStack.push(_buttonLinkId);

				System.out.println("Added " + _buttonLinkId);

				_buttonLinkId++;

				btnContainerElement.addChildLoggerElement(varBtnElement);
			}
		}

		return btnContainerElement;
	}

	public static LoggerElement generateChildContainerElement() {
		LoggerElement childContainerElement = new LoggerElement();

		childContainerElement.setName("ul");
		childContainerElement.setClassName("child-container collapsible");
		System.out.println("Dropping value 1");
		childContainerElement.setButtonLinkId(_buttonIdStack.pop());

		return childContainerElement;
	}

	public static LoggerElement generateClosingElement(Element element) {
		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		LoggerElement lessThanElement = new LoggerElement();

		lessThanElement.setName("span");
		lessThanElement.setClassName("misc");
		lessThanElement.setText("&lt;/");

		lineContainerElement.addChildLoggerElement(lessThanElement);

		LoggerElement actionTypeElement = new LoggerElement();

		actionTypeElement.setName("span");
		actionTypeElement.setClassName("action-type");
		actionTypeElement.setText(element.getName() + " ");

		lineContainerElement.addChildLoggerElement(actionTypeElement);

		LoggerElement greaterThanElement = new LoggerElement();

		greaterThanElement.setName("span");
		greaterThanElement.setClassName("misc");
		greaterThanElement.setText("&gt;");

		lineContainerElement.addChildLoggerElement(greaterThanElement);

		return lineContainerElement;
	}

	public static LoggerElement generateIONOElements(Element element) {
		LoggerElement testcaseElement = new LoggerElement();

		testcaseElement.setName("li");

		String elementName = element.getName();

		if (elementName.equals("execute")) {
			Attribute attribute = element.attribute(1);

			testcaseElement.setClassName(attribute.getName());
		}

		testcaseElement.addChildLoggerElement(
			generateBtnContainerElement(element));
		testcaseElement.addChildLoggerElement(
			generateLineContainerElement(element));

		List<Element> childElements = element.elements();

		if (element.attributeValue("macro") != null ||
			element.attributeValue("macro-desktop") != null ||
			element.attributeValue("macro-mobile") != null) {

			if (element.attributeValue("macro") != null) {
				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro"));
			}
			else if ((element.attributeValue("macro-desktop") != null) &&
				 Validator.isNotNull(PropsValues.MOBILE_DEVICE_TYPE)) {

				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro-desktop"));
			}
			else if ((element.attributeValue("macro-mobile") != null) &&
				 Validator.isNotNull(PropsValues.MOBILE_DEVICE_TYPE)) {

				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro-mobile"));
			}

			testcaseElement.addChildLoggerElement(
				generateClosingElement(element));
		}
		else if (!childElements.isEmpty()) {
			LoggerElement childContainerElement = generateChildContainerElement();

			for (Element childElement : childElements) {
				childContainerElement.addChildLoggerElement(
					generateIONOElements(childElement));
			}

			testcaseElement.addChildLoggerElement(childContainerElement);
			testcaseElement.addChildLoggerElement(
				generateClosingElement(element));
		}

		return testcaseElement;
	}

	public static LoggerElement generateLineContainerElement(
		Element element) {

		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		LoggerElement lessThanElement = new LoggerElement();

		lessThanElement.setName("span");
		lessThanElement.setClassName("misc");
		lessThanElement.setText("&lt;");

		lineContainerElement.addChildLoggerElement(lessThanElement);

		LoggerElement actionTypeElement = new LoggerElement();

		actionTypeElement.setName("span");
		actionTypeElement.setClassName("action-type");
		actionTypeElement.setText(element.getName() + " ");

		lineContainerElement.addChildLoggerElement(actionTypeElement);

		List<Attribute> attributes = element.attributes();

		lineContainerElement = generateAttributeElements(
			attributes, lineContainerElement);

		LoggerElement greaterThanElement = new LoggerElement();

		greaterThanElement.setName("span");
		greaterThanElement.setClassName("misc");
		greaterThanElement.setText("&gt;");

		lineContainerElement.addChildLoggerElement(greaterThanElement);

		List<Element> elements = element.elements();

		if (!elements.isEmpty() &&
			(element.attributeValue("macro") != null ||
			element.attributeValue("macro-desktop") != null ||
			element.attributeValue("macro-mobile") != null)) {

			lineContainerElement.addChildLoggerElement(
				generateParameterElements(elements));
		}

		return lineContainerElement;
	}

	public static LoggerElement generateMacroElement(
		Element executeElement, String macroType) {

		List<Element> elements = executeElement.elements();

		String classCommandName = executeElement.attributeValue(macroType);

		String className =
			PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
				classCommandName);

		LoggerElement macroContainerElement = generateChildContainerElement();

		Element rootElement = PoshiRunnerContext.getMacroRootElement(className);

		List<Element> rootVarElements = rootElement.elements("var");

		for (Element rootVarElement : rootVarElements) {
			macroContainerElement.addChildLoggerElement(
				generateIONOElements(rootVarElement));
		}

		Element commandElement = PoshiRunnerContext.getMacroCommandElement(
			classCommandName);

		List<Element> childElements = commandElement.elements();

		for (Element childElement : childElements) {
			macroContainerElement.addChildLoggerElement(
				generateIONOElements(childElement));
		}

		return macroContainerElement;
	}

	public static LoggerElement generateParameterElements(List<Element> elements) {
		LoggerElement parameterContainerElement = new LoggerElement();

		parameterContainerElement.setName("div");
		parameterContainerElement.setClassName("parameter-container collapsible");
		System.out.println("Dropping value 2");
		parameterContainerElement.setButtonLinkId(_buttonIdStack.pop());

		for(Element element : elements) {
			LoggerElement parameterLineNumberElement = new LoggerElement();

			parameterLineNumberElement.setName("div");
			parameterLineNumberElement.setClassName("line-number");
			parameterLineNumberElement.setText(
				element.attributeValue("line-number"));

			parameterContainerElement.addChildLoggerElement(
				parameterLineNumberElement);

			parameterContainerElement.addChildLoggerElement(
				generateLineContainerElement(element));
		}

		return parameterContainerElement;
	}

	public static void generateXMLLog(
		Element element, String classCommandName, String testClassName)
		throws Exception {

		LoggerElement xmlLogElement = new LoggerElement();

		xmlLogElement.setName("li");
		xmlLogElement.setClassName("header");

		LoggerElement btnContainerElement = new LoggerElement();

		btnContainerElement.setName("div");
		btnContainerElement.setClassName("btn-container");

		LoggerElement btnElement = new LoggerElement();

		btnElement.setName("button");
		btnElement.setClassName("btn btn-collapse");
		btnElement.setButtonLinkId(_buttonLinkId);

		_buttonIdStack.push(_buttonLinkId);

		System.out.println("Added " + _buttonLinkId);

		_buttonLinkId++;

		btnContainerElement.addChildLoggerElement(btnElement);

		xmlLogElement.addChildLoggerElement(btnContainerElement);

		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		LoggerElement lineElement = new LoggerElement();

		lineElement.setName("h3");
		lineElement.setClassName("testCaseCommand");
		lineElement.setText(classCommandName);

		lineContainerElement.addChildLoggerElement(lineElement);

		xmlLogElement.addChildLoggerElement(lineContainerElement);

		LoggerElement childContainerElement = generateChildContainerElement();

		Element setupElement = getSetupElement(testClassName);

		childContainerElement.addChildLoggerElement(generateIONOElements(setupElement));
		// childContainerElement.addChildLoggerElement(generateIONOElements(element));

		// Element teardownElement = getTeardownElement(testClassName);

		// childContainerElement.addChildLoggerElement(generateIONOElements(teardownElement));

		xmlLogElement.addChildLoggerElement(childContainerElement);

		createXMLLogFile(xmlLogElement.toString());
	}

	public static Element getSetupElement(String testClassName) {
		String setupElementName = testClassName + "#set-up";

		return PoshiRunnerContext.getTestcaseCommandElement(
			setupElementName);
	}

	public static Element getTeardownElement(String testClassName) {
		String teardownElementName = testClassName + "#tear-down";

		return PoshiRunnerContext.getTestcaseCommandElement(
			teardownElementName);
	}

	private static int _buttonLinkId = 0;
	private static final Stack<Integer> _buttonIdStack = new Stack<Integer>();
}