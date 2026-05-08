/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import jakarta.annotation.Generated;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
@GraphQLName(
	description = "A FieldMapping defines how a logical Faro field (e.g. an Individual attribute like 'jobTitle') is sourced from one or more underlying data sources. Inspect FieldMappings to discover what custom attributes exist on Individuals or Accounts in a given Faro project.",
	value = "FieldMapping"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "FieldMapping")
public class FieldMapping implements Serializable {

	public static FieldMapping toDTO(String json) {
		return ObjectMapperUtil.readValue(FieldMapping.class, json);
	}

	public static FieldMapping unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(FieldMapping.class, json);
	}

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "The entity type the field is attached to. Common values: 'individual', 'account'."
	)
	public String getContext() {
		if (_contextSupplier != null) {
			context = _contextSupplier.get();

			_contextSupplier = null;
		}

		return context;
	}

	public void setContext(String context) {
		this.context = context;

		_contextSupplier = null;
	}

	@JsonIgnore
	public void setContext(
		UnsafeSupplier<String, Exception> contextUnsafeSupplier) {

		_contextSupplier = () -> {
			try {
				return contextUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "The entity type the field is attached to. Common values: 'individual', 'account'."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String context;

	@JsonIgnore
	private Supplier<String> _contextSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Last time this field mapping was modified."
	)
	public Date getDateModified() {
		if (_dateModifiedSupplier != null) {
			dateModified = _dateModifiedSupplier.get();

			_dateModifiedSupplier = null;
		}

		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;

		_dateModifiedSupplier = null;
	}

	@JsonIgnore
	public void setDateModified(
		UnsafeSupplier<Date, Exception> dateModifiedUnsafeSupplier) {

		_dateModifiedSupplier = () -> {
			try {
				return dateModifiedUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(description = "Last time this field mapping was modified.")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected Date dateModified;

	@JsonIgnore
	private Supplier<Date> _dateModifiedSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Human-readable name shown in Faro admin UI for this field."
	)
	public String getDisplayName() {
		if (_displayNameSupplier != null) {
			displayName = _displayNameSupplier.get();

			_displayNameSupplier = null;
		}

		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;

		_displayNameSupplier = null;
	}

	@JsonIgnore
	public void setDisplayName(
		UnsafeSupplier<String, Exception> displayNameUnsafeSupplier) {

		_displayNameSupplier = () -> {
			try {
				return displayNameUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Human-readable name shown in Faro admin UI for this field."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String displayName;

	@JsonIgnore
	private Supplier<String> _displayNameSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "UI display hint, e.g. 'text', 'select'. Source for UI rendering decisions; not the data type."
	)
	public String getDisplayType() {
		if (_displayTypeSupplier != null) {
			displayType = _displayTypeSupplier.get();

			_displayTypeSupplier = null;
		}

		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;

		_displayTypeSupplier = null;
	}

	@JsonIgnore
	public void setDisplayType(
		UnsafeSupplier<String, Exception> displayTypeUnsafeSupplier) {

		_displayTypeSupplier = () -> {
			try {
				return displayTypeUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "UI display hint, e.g. 'text', 'select'. Source for UI rendering decisions; not the data type."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String displayType;

	@JsonIgnore
	private Supplier<String> _displayTypeSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Canonical (logical) name of the field. This is the name used in filter expressions and OData queries."
	)
	public String getFieldName() {
		if (_fieldNameSupplier != null) {
			fieldName = _fieldNameSupplier.get();

			_fieldNameSupplier = null;
		}

		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;

		_fieldNameSupplier = null;
	}

	@JsonIgnore
	public void setFieldName(
		UnsafeSupplier<String, Exception> fieldNameUnsafeSupplier) {

		_fieldNameSupplier = () -> {
			try {
				return fieldNameUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Canonical (logical) name of the field. This is the name used in filter expressions and OData queries."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String fieldName;

	@JsonIgnore
	private Supplier<String> _fieldNameSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Logical data type of the field: 'string', 'number', 'boolean', 'date', etc."
	)
	public String getFieldType() {
		if (_fieldTypeSupplier != null) {
			fieldType = _fieldTypeSupplier.get();

			_fieldTypeSupplier = null;
		}

		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;

		_fieldTypeSupplier = null;
	}

	@JsonIgnore
	public void setFieldType(
		UnsafeSupplier<String, Exception> fieldTypeUnsafeSupplier) {

		_fieldTypeSupplier = () -> {
			try {
				return fieldTypeUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Logical data type of the field: 'string', 'number', 'boolean', 'date', etc."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String fieldType;

	@JsonIgnore
	private Supplier<String> _fieldTypeSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Sub-type of the owning entity, e.g. 'KNOWN' or 'ANONYMOUS' for Individuals."
	)
	public String getOwnerType() {
		if (_ownerTypeSupplier != null) {
			ownerType = _ownerTypeSupplier.get();

			_ownerTypeSupplier = null;
		}

		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;

		_ownerTypeSupplier = null;
	}

	@JsonIgnore
	public void setOwnerType(
		UnsafeSupplier<String, Exception> ownerTypeUnsafeSupplier) {

		_ownerTypeSupplier = () -> {
			try {
				return ownerTypeUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Sub-type of the owning entity, e.g. 'KNOWN' or 'ANONYMOUS' for Individuals."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String ownerType;

	@JsonIgnore
	private Supplier<String> _ownerTypeSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "True if this field can have multiple values per entity (e.g. tags). False for scalar fields."
	)
	public Boolean getRepeatable() {
		if (_repeatableSupplier != null) {
			repeatable = _repeatableSupplier.get();

			_repeatableSupplier = null;
		}

		return repeatable;
	}

	public void setRepeatable(Boolean repeatable) {
		this.repeatable = repeatable;

		_repeatableSupplier = null;
	}

	@JsonIgnore
	public void setRepeatable(
		UnsafeSupplier<Boolean, Exception> repeatableUnsafeSupplier) {

		_repeatableSupplier = () -> {
			try {
				return repeatableUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "True if this field can have multiple values per entity (e.g. tags). False for scalar fields."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected Boolean repeatable;

	@JsonIgnore
	private Supplier<Boolean> _repeatableSupplier;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FieldMapping)) {
			return false;
		}

		FieldMapping fieldMapping = (FieldMapping)object;

		return Objects.equals(toString(), fieldMapping.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		String context = getContext();

		if (context != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"context\": ");

			sb.append("\"");

			sb.append(_escape(context));

			sb.append("\"");
		}

		Date dateModified = getDateModified();

		if (dateModified != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateModified\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(dateModified));

			sb.append("\"");
		}

		String displayName = getDisplayName();

		if (displayName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayName\": ");

			sb.append("\"");

			sb.append(_escape(displayName));

			sb.append("\"");
		}

		String displayType = getDisplayType();

		if (displayType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayType\": ");

			sb.append("\"");

			sb.append(_escape(displayType));

			sb.append("\"");
		}

		String fieldName = getFieldName();

		if (fieldName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"fieldName\": ");

			sb.append("\"");

			sb.append(_escape(fieldName));

			sb.append("\"");
		}

		String fieldType = getFieldType();

		if (fieldType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"fieldType\": ");

			sb.append("\"");

			sb.append(_escape(fieldType));

			sb.append("\"");
		}

		String ownerType = getOwnerType();

		if (ownerType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ownerType\": ");

			sb.append("\"");

			sb.append(_escape(ownerType));

			sb.append("\"");
		}

		Boolean repeatable = getRepeatable();

		if (repeatable != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"repeatable\": ");

			sb.append(repeatable);
		}

		sb.append("}");

		return sb.toString();
	}

	@io.swagger.v3.oas.annotations.media.Schema(
		accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.osb.faro.rest.dto.v1_0.FieldMapping",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
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
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof Map) {
						sb.append(_toJSON((Map<String, ?>)valueArray[i]));
					}
					else if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

	private Map<String, Serializable> _extendedProperties;

}
// LIFERAY-REST-BUILDER-HASH:1318309555