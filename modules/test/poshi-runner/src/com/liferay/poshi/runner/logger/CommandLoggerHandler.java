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
import com.liferay.poshi.runner.PoshiRunnerGetterUtil;
import com.liferay.poshi.runner.PoshiRunnerStackTraceUtil;
import com.liferay.poshi.runner.PoshiRunnerVariablesUtil;
import com.liferay.poshi.runner.util.Validator;

import java.util.List;

import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class CommandLoggerHandler {

	public static void failCommand(Element element) {
		if (!_isCurrentCommand(element)) {
			return;
		}

		_commandElement = null;

		_commandLoggerElement.addChildLoggerElement(
			_getErrorContainerLoggerElement());

		_commandLoggerElement.addClassName("failed");
		_runLineLoggerElement.addClassName("error-line");

		LoggerElement xmlLoggerElement =
			XMLLoggerHandler.getLoggerElementFromElement(
				PoshiRunnerStackTraceUtil.getUniqueID());

		xmlLoggerElement.setAttribute("data-status01", "fail");

		LoggerUtil.executeJavascript(
			"loggerInterface.fire('command-complete')");
	}

	public static void logClassCommandName(String classCommandName) {
		LoggerElement dividerLineLoggerElement = new LoggerElement();

		dividerLineLoggerElement.setClassName("divider-line");
		dividerLineLoggerElement.setText(classCommandName);

		_commandLogLoggerElement.addChildLoggerElement(
			dividerLineLoggerElement);
	}

	public static void passCommand(Element element) {
		if (!_isCurrentCommand(element)) {
			return;
		}

		_commandElement = null;

		LoggerElement xmlLoggerElement =
			XMLLoggerHandler.getLoggerElementFromElement(
				PoshiRunnerStackTraceUtil.getUniqueID());

		xmlLoggerElement.setAttribute("data-status01", "pass");

		LoggerUtil.executeJavascript(
			"loggerInterface.fire('command-complete')");
	}

	public static void sendRunLine(Element element, List<String> arguments) {
		LoggerElement childContainerLoggerElement =
			_commandLoggerElement.loggerElement("ul");

		LoggerElement runLineLoggerElement = new LoggerElement();

		runLineLoggerElement.setClassName("run-line");
		runLineLoggerElement.setName("li");

		StringBuilder sb = new StringBuilder();

		sb.append(_getLineItemText("misc", "Running "));
		sb.append(
			_getLineItemText(
				"command-name", element.attributeValue("selenium")));

		if (!arguments.isEmpty()) {
			sb.append(_getLineItemText("misc", " with parameters"));

			for (String argument : arguments) {
				sb.append(_getLineItemText("misc", "&nbsp;"));
				sb.append(_getLineItemText("param-value", argument));
			}
		}

		runLineLoggerElement.setText(sb.toString());

		_runLineLoggerElement = runLineLoggerElement;

		childContainerLoggerElement.addChildLoggerElement(runLineLoggerElement);
	}

	public static void startCommand(Element element) throws Exception {
		if (!_isCommand(element)) {
			return;
		}

		_commandElement = element;

		_commandLoggerElement = _getCommandLoggerElement(element);

		_commandLogLoggerElement.addChildLoggerElement(_commandLoggerElement);

		_sidebarLoggerElement.setClassName("sidebar running");

		LoggerElement xmlLoggerElement =
			XMLLoggerHandler.getLoggerElementFromElement(
				PoshiRunnerStackTraceUtil.getUniqueID());

		String functionLinkID = xmlLoggerElement.getAttributeValue(
			"data-functionlinkid");

		if (functionLinkID != null) {
			_functionLinkId = Integer.parseInt(functionLinkID.substring(15));
		}

		xmlLoggerElement.setAttribute("data-status01", "pending");

		xmlLoggerElement.setAttribute(
			"data-functionlinkid", "functionLinkId-" + _functionLinkId);

		_commandLoggerElement.setAttribute(
			"data-functionlinkid", "functionLinkId-" + _functionLinkId);

		LoggerUtil.executeJavascript(
			"loggerInterface.fire('command-complete')");

		_functionLinkId++;
	}

	public static void startTest() {
		_sidebarLoggerElement.setClassName("sidebar running");
	}

	public static void stopTest() {
		_sidebarLoggerElement.setClassName("sidebar finished");
	}

	private static LoggerElement _getButtonLoggerElement(int btnLinkId) {
		LoggerElement buttonLoggerElement = new LoggerElement();

		buttonLoggerElement.setAttribute(
			"data-btnlinkid", "command-" + btnLinkId);
		buttonLoggerElement.setClassName("btn expand-toggle");

		return buttonLoggerElement;
	}

	private static LoggerElement _getChildContainerLoggerElement(
		int btnLinkId) {

		LoggerElement childContainerLoggerElement = new LoggerElement();

		childContainerLoggerElement.setAttribute(
			"data-btnlinkid", "command-" + btnLinkId);
		childContainerLoggerElement.setClassName("child-container collapse");
		childContainerLoggerElement.setName("ul");

		return childContainerLoggerElement;
	}

	private static LoggerElement _getCommandLoggerElement(Element element)
		throws Exception {

		LoggerElement commandLoggerElement = new LoggerElement();

		commandLoggerElement.setClassName("line-group linkable");
		commandLoggerElement.setName("li");

		commandLoggerElement.addChildLoggerElement(
			_getButtonLoggerElement(_btnLinkId));

		commandLoggerElement.addChildLoggerElement(
			_getLineContainerLoggerElement(element));

		commandLoggerElement.addChildLoggerElement(
			_getChildContainerLoggerElement(_btnLinkId));

		_btnLinkId++;

		return commandLoggerElement;
	}

	private static LoggerElement _getErrorConsoleLoggerElement() {
		LoggerElement consoleLoggerElement = new LoggerElement();

		consoleLoggerElement.setClassName("console");

		LoggerElement stepsLoggerElement = new LoggerElement();

		stepsLoggerElement.setClassName("steps");

		LoggerElement stepsHeaderLoggerElement = new LoggerElement();

		stepsHeaderLoggerElement.setClassName("steps-header");
		stepsHeaderLoggerElement.setName("h4");
		stepsHeaderLoggerElement.setText("Steps:");

		stepsLoggerElement.addChildLoggerElement(stepsHeaderLoggerElement);
		stepsLoggerElement.addChildLoggerElement(
			SummaryLoggerHandler.getMajorStepsLoggerElement());

		LoggerElement causeLoggerElement = new LoggerElement();

		causeLoggerElement.setClassName("cause");

		LoggerElement causeHeaderLoggerElement = new LoggerElement();

		causeHeaderLoggerElement.setClassName("cause-header");
		causeHeaderLoggerElement.setName("h4");
		causeHeaderLoggerElement.setText("Cause:");

		causeLoggerElement.addChildLoggerElement(causeHeaderLoggerElement);
		causeLoggerElement.addChildLoggerElement(
			SummaryLoggerHandler.getCauseBodyLoggerElement());

		consoleLoggerElement.addChildLoggerElement(stepsLoggerElement);
		consoleLoggerElement.addChildLoggerElement(causeLoggerElement);

		return consoleLoggerElement;
	}

	private static LoggerElement _getErrorContainerLoggerElement() {
		LoggerElement errorContainerLoggerElement = new LoggerElement();

		errorContainerLoggerElement.setClassName("error-container hidden");

		errorContainerLoggerElement.addChildLoggerElement(
			_getErrorConsoleLoggerElement());
		errorContainerLoggerElement.addChildLoggerElement(
			_getScreenshotLoggerElement());

		return errorContainerLoggerElement;
	}

	private static LoggerElement _getLineContainerLoggerElement(Element element)
		throws Exception {

		LoggerElement lineContainerLoggerElement = new LoggerElement();

		lineContainerLoggerElement.setClassName("line-container");
		lineContainerLoggerElement.setText(_getLineContainerText(element));

		return lineContainerLoggerElement;
	}

	private static String _getLineContainerText(Element element)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(_getLineItemText("misc", "Running "));

		String classCommandName = element.attributeValue("function");

		sb.append(_getLineItemText("command-name", classCommandName));

		String className =
			PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
				classCommandName);

		int functionLocatorCount = PoshiRunnerContext.getFunctionLocatorCount(
			className);

		for (int i = 0; i < functionLocatorCount; i++) {
			String locatorKey = "locator" + (i + 1);

			if (PoshiRunnerVariablesUtil.containsKeyInCommandMap(locatorKey)) {
				sb.append(_getLineItemText("misc", " with "));
				sb.append(_getLineItemText("param-type", locatorKey));
				sb.append(_getLineItemText("misc", "&nbsp;"));

				String paramValue =
					PoshiRunnerVariablesUtil.getValueFromCommandMap(locatorKey);

				sb.append(_getLineItemText("param-value", paramValue));
			}

			String valueKey = "value" + (i + 1);

			if (PoshiRunnerVariablesUtil.containsKeyInCommandMap(valueKey)) {
				sb.append(_getLineItemText("misc", " with "));
				sb.append(_getLineItemText("param-type", valueKey));
				sb.append(_getLineItemText("misc", "&nbsp;"));

				String paramValue =
					PoshiRunnerVariablesUtil.getValueFromCommandMap(valueKey);

				sb.append(_getLineItemText("param-value", paramValue));
			}
		}

		return sb.toString();
	}

	private static String _getLineItemText(String className, String text) {
		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setClassName(className);
		loggerElement.setID(null);
		loggerElement.setName("span");
		loggerElement.setText(text);

		return loggerElement.toString();
	}

	private static LoggerElement _getScreenshotLoggerElement() {
		LoggerElement errorScreenshotLoggerElement = new LoggerElement();

		errorScreenshotLoggerElement.setClassName("screenshot");

		LoggerElement beforeLoggerElement = new LoggerElement();

		beforeLoggerElement.setAttribute("alt", "someimagebefore.jpg");
		beforeLoggerElement.setAttribute("src", "someimagebefore.jpg");
		beforeLoggerElement.setName("img");

		LoggerElement afterLoggerElement = new LoggerElement();

		afterLoggerElement.setAttribute("alt", "someimagebefore.jpg");
		afterLoggerElement.setAttribute("src", "someimagebefore.jpg");
		afterLoggerElement.setName("img");

		errorScreenshotLoggerElement.addChildLoggerElement(beforeLoggerElement);
		errorScreenshotLoggerElement.addChildLoggerElement(afterLoggerElement);

		return errorScreenshotLoggerElement;
	}

	private static boolean _isCommand(Element element) {
		if (!Validator.equals(element.getName(), "execute")) {
			return false;
		}

		if (Validator.isNull(element.attributeValue("function"))) {
			return false;
		}

		if (_commandElement != null) {
			return false;
		}

		return true;
	}

	private static boolean _isCurrentCommand(Element element) {
		return element.equals(_commandElement);
	}

	private static int _btnLinkId;
	private static Element _commandElement;
	private static LoggerElement _commandLoggerElement;
	private static final LoggerElement _commandLogLoggerElement =
		new LoggerElement("commandLog");
	private static int _functionLinkId;
	private static LoggerElement _runLineLoggerElement;
	private static final LoggerElement _sidebarLoggerElement =
		new LoggerElement("sidebar");

}