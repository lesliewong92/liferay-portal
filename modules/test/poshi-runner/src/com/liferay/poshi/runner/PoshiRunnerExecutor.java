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

package com.liferay.poshi.runner;

import com.liferay.poshi.runner.logger.CommandLoggerHandler;
import com.liferay.poshi.runner.logger.SummaryLoggerHandler;
import com.liferay.poshi.runner.selenium.LiferaySelenium;
import com.liferay.poshi.runner.selenium.SeleniumUtil;
import com.liferay.poshi.runner.util.GetterUtil;
import com.liferay.poshi.runner.util.PropsValues;
import com.liferay.poshi.runner.util.StringPool;
import com.liferay.poshi.runner.util.Validator;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * @author Karen Dang
 * @author Michael Hashimoto
 */
public class PoshiRunnerExecutor {

	public static boolean evaluateConditionalElement(Element element)
		throws Exception {

		String elementName = element.getName();

		if (elementName.equals("and")) {
			List<Element> andElements = element.elements();

			for (Element andElement : andElements) {
				PoshiRunnerStackTraceUtil.pushStackTrace(
					andElement.attributeValue("line-number"));

				CommandLoggerHandler.setLineGroupStatus("pending");

				if (!evaluateConditionalElement(andElement)) {
					CommandLoggerHandler.setLineGroupStatus("conditional-fail");
					PoshiRunnerStackTraceUtil.popStackTrace();

					return false;
				}

				CommandLoggerHandler.setLineGroupStatus("pass");
				PoshiRunnerStackTraceUtil.popStackTrace();

			}

			return true;
		}
		else if (elementName.equals("condition")) {
			if (element.attributeValue("action") != null) {
				runActionElement(element);

				return (boolean)_returnObject;
			}
			else if (element.attributeValue("function") != null) {
				runFunctionElement(element);

				return (boolean)_returnObject;
			}
			else if (element.attributeValue("selenium") != null) {
				runSeleniumElement(element);

				return (boolean)_returnObject;
			}
		}
		else if (elementName.equals("contains")) {
			String string = PoshiRunnerVariablesUtil.replaceCommandVars(
				element.attributeValue("string"));
			String substring = PoshiRunnerVariablesUtil.replaceCommandVars(
				element.attributeValue("substring"));

			if (string.contains(substring)) {
				return true;
			}

			return false;
		}
		else if (elementName.equals("equals")) {
			String arg1 = PoshiRunnerVariablesUtil.replaceCommandVars(
				element.attributeValue("arg1"));
			String arg2 = PoshiRunnerVariablesUtil.replaceCommandVars(
				element.attributeValue("arg2"));

			if (arg1.equals(arg2)) {
				return true;
			}

			return false;
		}
		else if (elementName.equals("isset")) {
			if (PoshiRunnerVariablesUtil.containsKeyInCommandMap(
					element.attributeValue("var"))) {

				return true;
			}

			return false;
		}
		else if (elementName.equals("or")) {
			List<Element> orElements = element.elements();

			for (Element orElement : orElements) {
				PoshiRunnerStackTraceUtil.pushStackTrace(
					orElement.attributeValue("line-number"));

				CommandLoggerHandler.setLineGroupStatus("pending");

				if (evaluateConditionalElement(orElement)) {
					CommandLoggerHandler.setLineGroupStatus("pass");
					PoshiRunnerStackTraceUtil.popStackTrace();

					return true;
				}

				CommandLoggerHandler.setLineGroupStatus("conditional-fail");
				PoshiRunnerStackTraceUtil.popStackTrace();
			}

			return false;
		}
		else if (elementName.equals("not")) {
			List<Element> notElements = element.elements();

			Element notElement = notElements.get(0);

			PoshiRunnerStackTraceUtil.pushStackTrace(
				notElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			boolean notElementValue = !evaluateConditionalElement(notElement);

			if(notElementValue) {
				CommandLoggerHandler.setLineGroupStatus("pass");
				PoshiRunnerStackTraceUtil.popStackTrace();
			}
			else {
				CommandLoggerHandler.setLineGroupStatus("conditional-fail");
				PoshiRunnerStackTraceUtil.popStackTrace();
			}

			return notElementValue;
		}

		return false;
	}

	public static void parseElement(Element element) throws Exception {
		String elementName = element.getName();

		List<Element> childElements = element.elements();

		for (Element childElement : childElements) {
			String childElementName = childElement.getName();

			PoshiRunnerStackTraceUtil.pushStackTrace(
				childElement.attributeValue("line-number"));

			if (childElementName.equals("echo") ||
				childElementName.equals("description")) {

				CommandLoggerHandler.setLineGroupStatus("pending");

				String message = childElement.attributeValue("message");

				if (message == null) {
					message = childElement.getText();
				}

				System.out.println(
					PoshiRunnerVariablesUtil.replaceCommandVars(message));

				CommandLoggerHandler.setLineGroupStatus("pass");
			}
			else if (childElementName.equals("execute")) {
				if (childElement.attributeValue("action") != null) {
					runActionElement(childElement);
				}
				else if (childElement.attributeValue("function") != null) {
					runFunctionElement(childElement);
				}
				else if (childElement.attributeValue("macro") != null) {
					runMacroElement(childElement, "macro");
				}
				else if ((childElement.attributeValue(
							"macro-desktop") != null) &&
						 Validator.isNotNull(PropsValues.MOBILE_DEVICE_TYPE)) {

					runMacroElement(childElement, "macro-desktop");
				}
				else if ((childElement.attributeValue(
							"macro-mobile") != null) &&
						 Validator.isNotNull(PropsValues.MOBILE_DEVICE_TYPE)) {

					runMacroElement(childElement, "macro-mobile");
				}
				else if (childElement.attributeValue("selenium") != null) {
					runSeleniumElement(childElement);
				}
			}
			else if (childElementName.equals("if")) {
				String currentContext = 
					PoshiRunnerStackTraceUtil.popStackTrace();

				boolean isFunction = currentContext.contains(".function");

				PoshiRunnerStackTraceUtil.pushStackTrace(currentContext);

				if(isFunction) {
					runIfElement(childElement, false);
				}
				else {
					runIfElement(childElement, true);
				}
			}
			else if (childElementName.equals("fail")) {
				CommandLoggerHandler.setLineGroupStatus("pending");

				String message = childElement.attributeValue("message");

				if (Validator.isNotNull(message)) {
					throw new Exception(
						PoshiRunnerVariablesUtil.replaceCommandVars(message));
				}

				throw new Exception();
			}
			else if (childElementName.equals("for")) {
				runForElement(childElement);
			}
			else if (childElementName.equals("task")) {
				CommandLoggerHandler.setLineGroupStatus("pending");

				try {
					SummaryLoggerHandler.startSummary(childElement);

					parseElement(childElement);
				}
				catch (Exception e) {
					SummaryLoggerHandler.failSummary(
						childElement, e.getMessage());

					throw e;
				}

				SummaryLoggerHandler.passSummary(childElement);

				CommandLoggerHandler.setLineGroupStatus("pass");
			}
			else if (childElementName.equals("var")) {
				CommandLoggerHandler.setLineGroupStatus("pending");

				runVarElement(childElement, true);

				CommandLoggerHandler.setLineGroupStatus("pass");
			}
			else if (childElementName.equals("while")) {
				runWhileElement(childElement);
			}

			PoshiRunnerStackTraceUtil.popStackTrace();
		}
	}

	public static void runActionElement(Element executeElement)
		throws Exception {

		String actionClassCommandName = executeElement.attributeValue("action");

		PoshiRunnerStackTraceUtil.pushFilePath(
			actionClassCommandName, "action");

		List<Element> executeVarElements = executeElement.elements("var");

		for (Element executeVarElement : executeVarElements) {
			PoshiRunnerStackTraceUtil.pushStackTrace(
				executeVarElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			runVarElement(executeVarElement, false);

			CommandLoggerHandler.setLineGroupStatus("pass");
			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		int locatorCount = PoshiRunnerContext.getActionLocatorCount(
			actionClassCommandName);

		for (int i = 0; i < locatorCount; i++) {
			String locator = executeElement.attributeValue("locator" + (i + 1));
			String locatorKey = executeElement.attributeValue(
				"locator-key" + (i + 1));
			String value = executeElement.attributeValue("value" + (i + 1));

			if (locator != null) {
				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"locator" + (i + 1), locator);
			}
			else if (locatorKey != null) {
				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"locator-key" + (i + 1), locatorKey);

				String pathClassName =
					PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
						actionClassCommandName);

				locator = PoshiRunnerContext.getPathLocator(
					pathClassName + "#" + locatorKey);

				locator = PoshiRunnerVariablesUtil.replaceExecuteVars(locator);

				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"locator" + (i + 1), locator);
			}

			if (value != null) {
				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"value" + (i + 1), value);
			}
		}

		PoshiRunnerVariablesUtil.pushCommandMap();

		List<Element> caseElements = PoshiRunnerContext.getActionCaseElements(
			actionClassCommandName);

		runCaseElements(caseElements, locatorCount);

		PoshiRunnerVariablesUtil.popCommandMap();

		PoshiRunnerStackTraceUtil.popFilePath();
	}

	public static void runCaseElements(
			List<Element> caseElements, int locatorCount)
		throws Exception {

		for (Element caseElement : caseElements) {
			String elementName = caseElement.getName();

			if (elementName.equals("case")) {
				String attributeName = null;
				String expected = null;

				String[] arguments =
					new String[] {"locator", "locator-key", "value"};

				for (int i = 0; i < locatorCount; i++) {
					for (String argument : arguments) {
						attributeName = argument + (i + 1);

						expected = caseElement.attributeValue(attributeName);

						if (expected != null) {
							break;
						}
					}
				}

				String actual = PoshiRunnerVariablesUtil.getValueFromCommandMap(
					attributeName);

				if (actual == null) {
					continue;
				}

				String comparator = caseElement.attributeValue("comparator");

				if (comparator == null) {
					comparator = "equals";
				}

				if ((comparator.equals("contains") &&
					 actual.contains(expected)) ||
					(comparator.equals("endsWith") &&
					 actual.endsWith(expected)) ||
					(comparator.equals("equals") && actual.equals(expected)) ||
					(comparator.equals("startsWith") &&
					 actual.startsWith(expected))) {

					parseElement(caseElement);

					break;
				}
			}
			else if (elementName.equals("default")) {
				parseElement(caseElement);

				break;
			}
		}
	}

	public static void runForElement(Element element) throws Exception {
		CommandLoggerHandler.setLineGroupStatus("pending");

		String list = PoshiRunnerVariablesUtil.replaceCommandVars(
			element.attributeValue("list"));

		String[] paramValues = list.split(",");

		String paramName = PoshiRunnerVariablesUtil.replaceCommandVars(
			element.attributeValue("param"));

		for (String paramValue : paramValues) {
			PoshiRunnerVariablesUtil.putIntoCommandMap(paramName, paramValue);

			parseElement(element);
		}

		CommandLoggerHandler.setLineGroupStatus("pass");
	}

	public static void runFunctionElement(Element executeElement)
		throws Exception {

		String classCommandName = executeElement.attributeValue("function");

		PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, "function");

		List<Element> executeVarElements = executeElement.elements("var");

		for (Element executeVarElement : executeVarElements) {
			PoshiRunnerStackTraceUtil.pushStackTrace(
				executeVarElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			runVarElement(executeVarElement, false);

			CommandLoggerHandler.setLineGroupStatus("pass");
			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		String className = classCommandName;

		if (classCommandName.contains("#")) {
			className = PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
				classCommandName);
		}

		int locatorCount = PoshiRunnerContext.getFunctionLocatorCount(
			className);

		for (int i = 0; i < locatorCount; i++) {
			String locator = executeElement.attributeValue("locator" + (i + 1));

			if (locator == null) {
				locator = PoshiRunnerVariablesUtil.getValueFromCommandMap(
					"locator" + (i + 1));
			}

			if (locator != null) {
				if (locator.contains("#")) {
					String pathClassName =
						PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
							locator);

					String locatorKey =
						PoshiRunnerVariablesUtil.replaceCommandVars(
							PoshiRunnerGetterUtil.
								getCommandNameFromClassCommandName(locator));

					PoshiRunnerVariablesUtil.putIntoExecuteMap(
						"locator-key" + (i + 1), locatorKey);

					locator = PoshiRunnerContext.getPathLocator(
						pathClassName + "#" + locatorKey);

					locator = PoshiRunnerVariablesUtil.replaceExecuteVars(
						locator);
				}

				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"locator" + (i + 1), locator);
			}

			String value = executeElement.attributeValue("value" + (i + 1));

			if (value == null) {
				value = PoshiRunnerVariablesUtil.getValueFromCommandMap(
					"value" + (i + 1));
			}

			if (value != null) {
				PoshiRunnerVariablesUtil.putIntoExecuteMap(
					"value" + (i + 1), value);
			}
		}

		PoshiRunnerVariablesUtil.pushCommandMap();

		// PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, "function");

		CommandLoggerHandler.startCommand(executeElement);

		SummaryLoggerHandler.startSummary(executeElement);

		Element commandElement = PoshiRunnerContext.getFunctionCommandElement(
			classCommandName);

		try {
			parseElement(commandElement);
		}
		catch (Exception e) {
			SummaryLoggerHandler.failSummary(executeElement, e.getMessage());

			CommandLoggerHandler.failCommand(executeElement);

			throw e;
		}

		PoshiRunnerVariablesUtil.popCommandMap();

		PoshiRunnerStackTraceUtil.popFilePath();

		CommandLoggerHandler.passCommand(executeElement);
		SummaryLoggerHandler.passSummary(executeElement);
	}

	public static void runIfElement(Element element, boolean isNotFunction) 
		throws Exception {

		CommandLoggerHandler.setLineGroupStatus("pending");

		List<Element> ifChildElements = element.elements();

		Element ifConditionElement = ifChildElements.get(0);

		if(isNotFunction) {
			PoshiRunnerStackTraceUtil.pushStackTrace(
				ifConditionElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");
		}

		boolean condition = evaluateConditionalElement(ifConditionElement);

		if(isNotFunction) {
			if (condition) {
				CommandLoggerHandler.setLineGroupStatus("pass");
			}
			else {
				CommandLoggerHandler.setLineGroupStatus("conditional-fail");
			}

			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		if (condition) {
			Element ifThenElement = element.element("then");

			PoshiRunnerStackTraceUtil.pushStackTrace(
				ifThenElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			parseElement(ifThenElement);

			CommandLoggerHandler.setLineGroupStatus("pass");
			PoshiRunnerStackTraceUtil.popStackTrace();

			CommandLoggerHandler.setLineGroupStatus("pass");
		}
		else if (element.element("elseif") != null) {
			List<Element> elseIfElements = element.elements("elseif");

			for (Element elseIfElement : elseIfElements) {
				PoshiRunnerStackTraceUtil.pushStackTrace(
					elseIfElement.attributeValue("line-number"));

				CommandLoggerHandler.setLineGroupStatus("pending");

				List<Element> elseIfChildElements = elseIfElement.elements();

				Element elseIfConditionElement = elseIfChildElements.get(0);

				PoshiRunnerStackTraceUtil.pushStackTrace(
					elseIfConditionElement.attributeValue("line-number"));

				CommandLoggerHandler.setLineGroupStatus("pending");

				condition = evaluateConditionalElement(elseIfConditionElement);

				if (condition) {
					CommandLoggerHandler.setLineGroupStatus("pass");
				}
				else {
					CommandLoggerHandler.setLineGroupStatus("conditional-fail");
				}

				PoshiRunnerStackTraceUtil.popStackTrace();

				if (condition) {
					Element elseIfThenElement = elseIfElement.element("then");

					PoshiRunnerStackTraceUtil.pushStackTrace(
						elseIfThenElement.attributeValue("line-number"));

					CommandLoggerHandler.setLineGroupStatus("pending");

					parseElement(elseIfThenElement);

					CommandLoggerHandler.setLineGroupStatus("pass");
					PoshiRunnerStackTraceUtil.popStackTrace();

					CommandLoggerHandler.setLineGroupStatus("pass");
					PoshiRunnerStackTraceUtil.popStackTrace();

					CommandLoggerHandler.setLineGroupStatus("pass");

					break;
				}

				CommandLoggerHandler.setLineGroupStatus("conditional-fail");

				PoshiRunnerStackTraceUtil.popStackTrace();
			}
		}

		if ((element.element("else") != null) && !condition) {
			Element elseElement = element.element("else");

			PoshiRunnerStackTraceUtil.pushStackTrace(
				elseElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			parseElement(elseElement);

			CommandLoggerHandler.setLineGroupStatus("pass");

			PoshiRunnerStackTraceUtil.popStackTrace();

			CommandLoggerHandler.setLineGroupStatus("pass");
		}

		if(!condition &&
			element.element("else") == null) {

			CommandLoggerHandler.setLineGroupStatus("conditional-fail");
		}
	}

	public static void runMacroElement(Element executeElement, String macroType)
		throws Exception {

		String classCommandName = executeElement.attributeValue(macroType);

		CommandLoggerHandler.setLineGroupStatus("pending");

		String className =
			PoshiRunnerGetterUtil.getClassNameFromClassCommandName(
				classCommandName);

		PoshiRunnerStackTraceUtil.pushFilePath(classCommandName, "macro");

		Element rootElement = PoshiRunnerContext.getMacroRootElement(className);

		List<Element> rootVarElements = rootElement.elements("var");

		for (Element rootVarElement : rootVarElements) {
			PoshiRunnerStackTraceUtil.pushStackTrace(
				rootVarElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			runVarElement(rootVarElement, false);

			CommandLoggerHandler.setLineGroupStatus("pass");
			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		List<Element> executeVarElements = executeElement.elements("var");

		for (Element executeVarElement : executeVarElements) {
			runVarElement(executeVarElement, false);
		}

		PoshiRunnerVariablesUtil.pushCommandMap();

		SummaryLoggerHandler.startSummary(executeElement);

		Element commandElement = PoshiRunnerContext.getMacroCommandElement(
			classCommandName);

		try {
			parseElement(commandElement);
		}
		catch (Exception e) {
			SummaryLoggerHandler.failSummary(executeElement, e.getMessage());

			throw e;
		}

		CommandLoggerHandler.setLineGroupStatus("pass");

		PoshiRunnerVariablesUtil.popCommandMap();

		PoshiRunnerStackTraceUtil.popFilePath();

		SummaryLoggerHandler.passSummary(executeElement);
	}

	public static void runSeleniumElement(Element executeElement)
		throws Exception {

		List<String> arguments = new ArrayList<>();
		List<Class<?>> parameterClasses = new ArrayList<>();

		String selenium = executeElement.attributeValue("selenium");

		int parameterCount = PoshiRunnerContext.getSeleniumParameterCount(
			selenium);

		for (int i = 0; i < parameterCount; i++) {
			String argument = executeElement.attributeValue(
				"argument" + (i + 1));

			if (argument == null) {
				if (i == 0) {
					if (selenium.equals("assertConfirmation") ||
						selenium.equals("assertConsoleTextNotPresent") ||
						selenium.equals("assertConsoleTextPresent") ||
						selenium.equals("assertLocation") ||
						selenium.equals("assertTextNotPresent") ||
						selenium.equals("assertTextPresent") ||
						selenium.equals("waitForConfirmation") ||
						selenium.equals("waitForTextNotPresent") ||
						selenium.equals("waitForTextPresent")) {

						argument =
							PoshiRunnerVariablesUtil.getValueFromCommandMap(
								"value1");
					}
					else {
						argument =
							PoshiRunnerVariablesUtil.getValueFromCommandMap(
								"locator1");
					}
				}
				else if (i == 1) {
					argument = PoshiRunnerVariablesUtil.getValueFromCommandMap(
						"value1");

					if (selenium.equals("clickAt")) {
						argument = "";
					}
				}
				else if (i == 2) {
					argument = PoshiRunnerVariablesUtil.getValueFromCommandMap(
						"locator2");
				}
			}

			arguments.add(argument);

			parameterClasses.add(String.class);
		}

		CommandLoggerHandler.sendRunLine(executeElement, arguments);

		LiferaySelenium liferaySelenium = SeleniumUtil.getSelenium();

		Class<?> clazz = liferaySelenium.getClass();

		try {
			Method method = clazz.getMethod(
				selenium,
				parameterClasses.toArray(new Class[parameterClasses.size()]));

			_returnObject = method.invoke(
				liferaySelenium,
				(Object[])arguments.toArray(new String[arguments.size()]));
		}
		catch (Exception e) {
			Throwable throwable = e.getCause();

			throw new Exception(throwable.getMessage(), e);
		}
	}

	public static void runVarElement(Element element, boolean commandVar)
		throws Exception {

		String varName = element.attributeValue("name");
		String varValue = element.attributeValue("value");

		if (varValue == null) {
			if (element.attributeValue("method") != null) {
				String classCommandName =
					PoshiRunnerVariablesUtil.replaceCommandVars(
						element.attributeValue("method"));

				if (classCommandName.startsWith("TestPropsUtil")) {
					classCommandName = classCommandName.replace(
						"TestPropsUtil", "PropsUtil");
				}

				varValue = PoshiRunnerGetterUtil.getVarMethodValue(
					classCommandName);
			}
			else if ((element.attributeValue("group") != null) &&
					 (element.attributeValue("input") != null) &&
					 (element.attributeValue("pattern") != null)) {

				StringBuilder sb = new StringBuilder();

				sb.append("RegexUtil#replace(");
				sb.append(element.attributeValue("input"));
				sb.append(StringPool.COMMA);
				sb.append(element.attributeValue("pattern"));
				sb.append(element.attributeValue("group"));
				sb.append(StringPool.CLOSE_PARENTHESIS);

				varValue = PoshiRunnerGetterUtil.getVarMethodValue(
					sb.toString());
			}
			else {
				varValue = element.getText();
			}
		}

		varValue = PoshiRunnerVariablesUtil.replaceCommandVars(varValue);

		if (commandVar) {
			PoshiRunnerVariablesUtil.putIntoCommandMap(varName, varValue);
		}
		else {
			PoshiRunnerVariablesUtil.putIntoExecuteMap(varName, varValue);
		}
	}

	public static void runWhileElement(Element element) throws Exception {
		int maxIterations = 15;

		if (element.attributeValue("max-iterations") != null) {
			maxIterations = GetterUtil.getInteger(
				element.attributeValue("max-iterations"));
		}

		CommandLoggerHandler.setLineGroupStatus("pending");

		List<Element> whileChildElements = element.elements();

		Element conditionElement = whileChildElements.get(0);

		Element thenElement = element.element("then");

		boolean conditionInitFail = false;

		for (int i = 0; i < maxIterations; i++) {
			PoshiRunnerStackTraceUtil.pushStackTrace(
				conditionElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			if (!evaluateConditionalElement(conditionElement)) {
				if(i == 0) {
					CommandLoggerHandler.setLineGroupStatus("conditional-fail");

					conditionInitFail = true;
				}
				else {
					CommandLoggerHandler.setLineGroupStatus("pass");
				}

				PoshiRunnerStackTraceUtil.popStackTrace();

				break;
			}

			CommandLoggerHandler.setLineGroupStatus("pass");

			PoshiRunnerStackTraceUtil.popStackTrace();

			PoshiRunnerStackTraceUtil.pushStackTrace(
				thenElement.attributeValue("line-number"));

			CommandLoggerHandler.setLineGroupStatus("pending");

			parseElement(thenElement);

			CommandLoggerHandler.setLineGroupStatus("pass");

			PoshiRunnerStackTraceUtil.popStackTrace();
		}

		if(conditionInitFail) {
			CommandLoggerHandler.setLineGroupStatus("conditional-fail");
		}
		else {
			CommandLoggerHandler.setLineGroupStatus("pass");
		}
	}

	private static Object _returnObject;

}