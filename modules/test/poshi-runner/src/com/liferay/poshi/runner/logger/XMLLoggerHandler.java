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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class XMLLoggerHandler {

	public static LoggerElement generateTextLoggerElement(
		String className, String text) {

		LoggerElement textLoggerElement = new LoggerElement();

		textLoggerElement.setClassName(className);
		textLoggerElement.setName("span");
		textLoggerElement.setText(text);

		return textLoggerElement;
	}

	public static LoggerElement generateAttributeElements(
		List<Attribute> attributes, LoggerElement containerElement) {

		LoggerElement quoteElement = generateTextLoggerElement(
			"misc quote", "\"");

		for(Attribute attribute : attributes) {
			String attributeName = attribute.getName();

			if(attributeName.equals("line-number")) {
				continue;
			}

			containerElement.addChildLoggerElement(
				generateTextLoggerElement("tag-type", attributeName));

			containerElement.addChildLoggerElement(
				generateTextLoggerElement("misc", "="));

			containerElement.addChildLoggerElement(quoteElement);

			containerElement.addChildLoggerElement(
				generateTextLoggerElement("name", attribute.getValue()));

			containerElement.addChildLoggerElement(quoteElement);
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

		//Modify in order to account for expanding if just macro

		if (!childElements.isEmpty() ||
			(element.attributeValue("macro") != null ||
			element.attributeValue("macro-desktop") != null ||
			element.attributeValue("macro-mobile") != null)) {

			LoggerElement btnElement = new LoggerElement();

			btnElement.setAttribute("data-btnlinkid", "xml-" + _buttonLinkId);
			btnElement.setClassName("btn btn-collapse");
			btnElement.setName("button");

			_buttonIdStack.push(_buttonLinkId);

			_buttonLinkId++;

			btnContainerElement.addChildLoggerElement(btnElement);

			if (!childElements.isEmpty() &&
			(element.attributeValue("macro") != null ||
			element.attributeValue("macro-desktop") != null ||
			element.attributeValue("macro-mobile") != null)) {

				LoggerElement varBtnElement = new LoggerElement();

				varBtnElement.setAttribute("data-btnlinkid", "xml-" + _buttonLinkId);
				varBtnElement.setClassName("btn btn-var");
				varBtnElement.setName("button");

				_buttonIdStack.push(_buttonLinkId);

				_buttonLinkId++;

				btnContainerElement.addChildLoggerElement(varBtnElement);
			}
		}

		return btnContainerElement;
	}

	public static LoggerElement generateChildContainerElement() {
		LoggerElement childContainerElement = new LoggerElement();

		childContainerElement.setAttribute("data-btnlinkid", "xml-" + _buttonIdStack.pop());
		childContainerElement.setClassName("child-container collapsible collapse");
		childContainerElement.setName("ul");

		return childContainerElement;
	}

	public static LoggerElement generateClosingElement(Element element) {
		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("misc", "&lt;/"));

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("action-type", element.getName()));

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("misc", "&gt;"));

		return lineContainerElement;
	}

	public static LoggerElement generateIONOElements(Element element) {
		LoggerElement testcaseElement = new LoggerElement();

		_elementToLoggerElement.put(PoshiRunnerStackTraceUtil.getUniqueID(), testcaseElement);

		testcaseElement.setName("li");

		String elementName = element.getName();

		if (elementName.equals("echo")) {
			testcaseElement.setClassName("echo line-group");
		}
		else if (elementName.equals("execute")) {
			Attribute attribute = element.attribute(1);

			testcaseElement.setClassName(
				attribute.getName() + " line-group");
		}
		else if (elementName.equals("if")) {
			testcaseElement.setClassName("conditional line-group");
		}
		else {
			testcaseElement.setClassName("line-group");
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
				PoshiRunnerStackTraceUtil.pushFilePath(element.attributeValue("macro"), "macro");

				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro"));

				PoshiRunnerStackTraceUtil.popFilePath();
			}
			else if ((element.attributeValue("macro-desktop") != null) &&
				 Validator.isNull(PropsValues.MOBILE_DEVICE_TYPE)) {

				PoshiRunnerStackTraceUtil.pushFilePath(element.attributeValue("macro-desktop"), "macro");

				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro-desktop"));

				PoshiRunnerStackTraceUtil.popFilePath();
			}
			else if ((element.attributeValue("macro-mobile") != null) &&
				 Validator.isNotNull(PropsValues.MOBILE_DEVICE_TYPE)) {

				PoshiRunnerStackTraceUtil.pushFilePath(element.attributeValue("macro-mobile"), "macro");

				testcaseElement.addChildLoggerElement(
					generateMacroElement(element, "macro-mobile"));

				PoshiRunnerStackTraceUtil.popFilePath();
			}

			testcaseElement.addChildLoggerElement(
				generateClosingElement(element));
		}
		else if (!childElements.isEmpty()) {
			LoggerElement childContainerElement = generateChildContainerElement();

			for (Element childElement : childElements) {
				PoshiRunnerStackTraceUtil.pushStackTrace(childElement.attributeValue("line-number"));

				childContainerElement.addChildLoggerElement(generateIONOElements(childElement));

				PoshiRunnerStackTraceUtil.popStackTrace();
			}

			testcaseElement.addChildLoggerElement(childContainerElement);
			testcaseElement.addChildLoggerElement(
				generateClosingElement(element));
		}

/*
		if (classType.equals("test-case")) {
			PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, classType);

			PoshiRunnerStackTraceUtil.pushStackTrace(element.attributeValue("line-number"));

		}
		else if (classType.equals("macro")) {
			PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, classType);

			PoshiRunnerStackTraceUtil.pushStackTrace(element.attributeValue("line-number"));

			System.out.println(PoshiRunnerStackTraceUtil.getUniqueID());
		}
		if (classType.equals("test-case")) {
			PoshiRunnerStackTraceUtil.popStackTrace();
			PoshiRunnerStackTraceUtil.popFilePath();
		}
		else if (classType.equals("macro")) {
			PoshiRunnerStackTraceUtil.popStackTrace();
			PoshiRunnerStackTraceUtil.popFilePath();
		}*/

		return testcaseElement;
	}

	public static LoggerElement getLoggerElementFromElement(String uniqueID) {
		return _elementToLoggerElement.get(uniqueID);
	}

	public static LoggerElement generateLineContainerElement(
		Element element) {

		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("misc", "&lt;"));

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("action-type", element.getName()));

		List<Attribute> attributes = element.attributes();

		LoggerElement quoteElement = generateTextLoggerElement(
			"misc quote", "\"");

		for(Attribute attribute : attributes) {
			String attributeName = attribute.getName();

			if(attributeName.equals("line-number")) {
				continue;
			}

			lineContainerElement.addChildLoggerElement(
				generateTextLoggerElement("tag-type", attributeName));

			lineContainerElement.addChildLoggerElement(
				generateTextLoggerElement("misc", "="));

			lineContainerElement.addChildLoggerElement(quoteElement);

			lineContainerElement.addChildLoggerElement(
				generateTextLoggerElement("name", attribute.getValue()));

			lineContainerElement.addChildLoggerElement(quoteElement);
		}

		lineContainerElement.addChildLoggerElement(
			generateTextLoggerElement("misc", "&gt;"));

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
			PoshiRunnerStackTraceUtil.pushStackTrace(rootVarElement.attributeValue("line-number"));

			macroContainerElement.addChildLoggerElement(generateIONOElements(rootVarElement));

			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		Element commandElement = PoshiRunnerContext.getMacroCommandElement(
			classCommandName);

		List<Element> childElements = commandElement.elements();

		for (Element childElement : childElements) {
			PoshiRunnerStackTraceUtil.pushStackTrace(childElement.attributeValue("line-number"));

			macroContainerElement.addChildLoggerElement(generateIONOElements(childElement));

			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		return macroContainerElement;
	}

	public static LoggerElement generateParameterElements(
		List<Element> elements) {

		LoggerElement parameterContainerElement = new LoggerElement();

		parameterContainerElement.setAttribute("data-btnlinkid", "xml-" + _buttonIdStack.pop());
		parameterContainerElement.setClassName("parameter-container collapsible collapse");
		parameterContainerElement.setName("div");

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

	public static void generateXMLLog(String classCommandName, Element element, String testClassName) throws Exception {

		LoggerElement rootElement = new LoggerElement();

		rootElement.setName("li");
		rootElement.setClassName("header pending");

		LoggerElement btnContainerElement = new LoggerElement();

		btnContainerElement.setName("div");
		btnContainerElement.setClassName("btn-container");

		LoggerElement btnElement = new LoggerElement();

		btnElement.setAttribute("data-btnlinkid", "xml-" + _buttonLinkId);
		btnElement.setClassName("btn btn-collapse toggle");
		btnElement.setName("button");

		_buttonIdStack.push(_buttonLinkId);
		_buttonLinkId++;

		btnContainerElement.addChildLoggerElement(btnElement);
		rootElement.addChildLoggerElement(btnContainerElement);

		LoggerElement lineContainerElement = new LoggerElement();

		lineContainerElement.setName("div");
		lineContainerElement.setClassName("line-container");

		LoggerElement lineElement = new LoggerElement();

		lineElement.setName("h3");
		lineElement.setClassName("testCaseCommand");
		lineElement.setText(classCommandName);

		lineContainerElement.addChildLoggerElement(lineElement);
		rootElement.addChildLoggerElement(lineContainerElement);

		LoggerElement childContainerElement = new LoggerElement();

		childContainerElement.setAttribute("data-btnlinkid", "xml-" + _buttonIdStack.pop());
		childContainerElement.setClassName("child-container collapsible");
		childContainerElement.setName("ul");

		Element setupElement = getSetupElement(testClassName);

		PoshiRunnerStackTraceUtil.pushFilePath(testClassName + "#set-up", "test-case");
		PoshiRunnerStackTraceUtil.pushStackTrace(setupElement.attributeValue("line-number"));

		childContainerElement.addChildLoggerElement(generateIONOElements(setupElement));

		PoshiRunnerStackTraceUtil.popStackTrace();
		PoshiRunnerStackTraceUtil.popFilePath();


		PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, "test-case");
		PoshiRunnerStackTraceUtil.pushStackTrace(element.attributeValue("line-number"));

		childContainerElement.addChildLoggerElement(generateIONOElements(element));

		PoshiRunnerStackTraceUtil.popStackTrace();
		PoshiRunnerStackTraceUtil.popFilePath();

/*		Element teardownElement = getTeardownElement(testClassName);

		childContainerElement.addChildLoggerElement(generateIONOElements(teardownElement));*/

		rootElement.addChildLoggerElement(childContainerElement);

		createXMLLogFile(rootElement.toString());
	}

	public static Element getSetupElement(String testClassName) {
		String setupElementName = testClassName + "#set-up";

		return PoshiRunnerContext.getTestCaseCommandElement(setupElementName);
	}

	public static Element getTeardownElement(String testClassName) {
		String teardownElementName = testClassName + "#tear-down";

		return PoshiRunnerContext.getTestCaseCommandElement(teardownElementName);
	}

	public static void createXMLLogFile(String xmlLogContent) throws Exception {
		String loggerContent = FileUtil.read(
			"src/META-INF/resources/html/index.html");

		loggerContent = loggerContent.replace(
			"<ul class=\"xml-log-container\" id=\"xmlLogContainer\" />",
			"<ul class=\"xml-log-container\" id=\"xmlLogContainer\">\n" + xmlLogContent + "\n</ul>");

		FileUtil.write("test-results/html/index.html", loggerContent);
	}

	private static boolean _isCurrentCommand(Element element) {
		return element.equals(_commandElement);
	}

	private static Map<String, LoggerElement> _elementToLoggerElement = new HashMap<String, LoggerElement>();

	private static int _buttonLinkId = 0;
	private static int _level = 0;
	private static final Stack<Integer> _buttonIdStack = new Stack<Integer>();
	private static Element _commandElement;
}