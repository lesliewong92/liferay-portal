/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.client.serdes.v1_0;

import com.liferay.osb.faro.rest.client.dto.v1_0.FieldMapping;
import com.liferay.osb.faro.rest.client.json.BaseJSONParser;

import jakarta.annotation.Generated;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
public class FieldMappingSerDes {

	public static FieldMapping toDTO(String json) {
		FieldMappingJSONParser fieldMappingJSONParser =
			new FieldMappingJSONParser();

		return fieldMappingJSONParser.parseToDTO(json);
	}

	public static FieldMapping[] toDTOs(String json) {
		FieldMappingJSONParser fieldMappingJSONParser =
			new FieldMappingJSONParser();

		return fieldMappingJSONParser.parseToDTOs(json);
	}

	public static String toJSON(FieldMapping fieldMapping) {
		if (fieldMapping == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (fieldMapping.getContext() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"context\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getContext()));

			sb.append("\"");
		}

		if (fieldMapping.getDateModified() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateModified\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(fieldMapping.getDateModified()));

			sb.append("\"");
		}

		if (fieldMapping.getDisplayName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayName\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getDisplayName()));

			sb.append("\"");
		}

		if (fieldMapping.getDisplayType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayType\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getDisplayType()));

			sb.append("\"");
		}

		if (fieldMapping.getFieldName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"fieldName\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getFieldName()));

			sb.append("\"");
		}

		if (fieldMapping.getFieldType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"fieldType\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getFieldType()));

			sb.append("\"");
		}

		if (fieldMapping.getOwnerType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ownerType\": ");

			sb.append("\"");

			sb.append(_escape(fieldMapping.getOwnerType()));

			sb.append("\"");
		}

		if (fieldMapping.getRepeatable() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"repeatable\": ");

			sb.append(fieldMapping.getRepeatable());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		FieldMappingJSONParser fieldMappingJSONParser =
			new FieldMappingJSONParser();

		return fieldMappingJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(FieldMapping fieldMapping) {
		if (fieldMapping == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (fieldMapping.getContext() == null) {
			map.put("context", null);
		}
		else {
			map.put("context", String.valueOf(fieldMapping.getContext()));
		}

		if (fieldMapping.getDateModified() == null) {
			map.put("dateModified", null);
		}
		else {
			map.put(
				"dateModified",
				liferayToJSONDateFormat.format(fieldMapping.getDateModified()));
		}

		if (fieldMapping.getDisplayName() == null) {
			map.put("displayName", null);
		}
		else {
			map.put(
				"displayName", String.valueOf(fieldMapping.getDisplayName()));
		}

		if (fieldMapping.getDisplayType() == null) {
			map.put("displayType", null);
		}
		else {
			map.put(
				"displayType", String.valueOf(fieldMapping.getDisplayType()));
		}

		if (fieldMapping.getFieldName() == null) {
			map.put("fieldName", null);
		}
		else {
			map.put("fieldName", String.valueOf(fieldMapping.getFieldName()));
		}

		if (fieldMapping.getFieldType() == null) {
			map.put("fieldType", null);
		}
		else {
			map.put("fieldType", String.valueOf(fieldMapping.getFieldType()));
		}

		if (fieldMapping.getOwnerType() == null) {
			map.put("ownerType", null);
		}
		else {
			map.put("ownerType", String.valueOf(fieldMapping.getOwnerType()));
		}

		if (fieldMapping.getRepeatable() == null) {
			map.put("repeatable", null);
		}
		else {
			map.put("repeatable", String.valueOf(fieldMapping.getRepeatable()));
		}

		return map;
	}

	public static class FieldMappingJSONParser
		extends BaseJSONParser<FieldMapping> {

		@Override
		protected FieldMapping createDTO() {
			return new FieldMapping();
		}

		@Override
		protected FieldMapping[] createDTOArray(int size) {
			return new FieldMapping[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "context")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "displayName")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "displayType")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "fieldName")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "fieldType")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "ownerType")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "repeatable")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			FieldMapping fieldMapping, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "context")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setContext((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setDateModified(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "displayName")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setDisplayName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "displayType")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setDisplayType((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "fieldName")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setFieldName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "fieldType")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setFieldType((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "ownerType")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setOwnerType((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "repeatable")) {
				if (jsonParserFieldValue != null) {
					fieldMapping.setRepeatable((Boolean)jsonParserFieldValue);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}
// LIFERAY-REST-BUILDER-HASH:-1328579435