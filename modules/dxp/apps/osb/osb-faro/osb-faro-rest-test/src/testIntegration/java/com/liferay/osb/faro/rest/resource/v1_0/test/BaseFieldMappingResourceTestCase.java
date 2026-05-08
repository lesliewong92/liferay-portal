/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.osb.faro.rest.client.dto.v1_0.FieldMapping;
import com.liferay.osb.faro.rest.client.http.HttpInvoker;
import com.liferay.osb.faro.rest.client.pagination.Page;
import com.liferay.osb.faro.rest.client.pagination.Pagination;
import com.liferay.osb.faro.rest.client.resource.v1_0.FieldMappingResource;
import com.liferay.osb.faro.rest.client.serdes.v1_0.FieldMappingSerDes;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsValues;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import jakarta.annotation.Generated;

import jakarta.ws.rs.core.MultivaluedHashMap;

import java.lang.reflect.Method;

import java.text.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
public abstract class BaseFieldMappingResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_fieldMappingResource.setContextCompany(testCompany);

		_testCompanyAdminUser = UserTestUtil.getAdminUser(
			testCompany.getCompanyId());

		fieldMappingResource = FieldMappingResource.builder(
		).authentication(
			_testCompanyAdminUser.getEmailAddress(),
			PropsValues.DEFAULT_ADMIN_PASSWORD
		).endpoint(
			testCompany.getVirtualHostname(), 8080, "http"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = getClientSerDesObjectMapper();

		FieldMapping fieldMapping1 = randomFieldMapping();

		String json = objectMapper.writeValueAsString(fieldMapping1);

		FieldMapping fieldMapping2 = FieldMappingSerDes.toDTO(json);

		Assert.assertTrue(equals(fieldMapping1, fieldMapping2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = getClientSerDesObjectMapper();

		FieldMapping fieldMapping = randomFieldMapping();

		String json1 = objectMapper.writeValueAsString(fieldMapping);
		String json2 = FieldMappingSerDes.toJSON(fieldMapping);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	protected ObjectMapper getClientSerDesObjectMapper() {
		return new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		FieldMapping fieldMapping = randomFieldMapping();

		fieldMapping.setContext(regex);
		fieldMapping.setDisplayName(regex);
		fieldMapping.setDisplayType(regex);
		fieldMapping.setFieldName(regex);
		fieldMapping.setFieldType(regex);
		fieldMapping.setOwnerType(regex);

		String json = FieldMappingSerDes.toJSON(fieldMapping);

		Assert.assertFalse(json.contains(regex));

		fieldMapping = FieldMappingSerDes.toDTO(json);

		Assert.assertEquals(regex, fieldMapping.getContext());
		Assert.assertEquals(regex, fieldMapping.getDisplayName());
		Assert.assertEquals(regex, fieldMapping.getDisplayType());
		Assert.assertEquals(regex, fieldMapping.getFieldName());
		Assert.assertEquals(regex, fieldMapping.getFieldType());
		Assert.assertEquals(regex, fieldMapping.getOwnerType());
	}

	@Test
	public void testGetSiteFieldMapping() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetSiteFieldMapping() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetSiteFieldMappingNotFound() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testGetSiteFieldMappingsPage() throws Exception {
		Long siteId = testGetSiteFieldMappingsPage_getSiteId();
		Long irrelevantSiteId =
			testGetSiteFieldMappingsPage_getIrrelevantSiteId();

		Page<FieldMapping> page = fieldMappingResource.getSiteFieldMappingsPage(
			siteId, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, Pagination.of(1, 10));

		long totalCount = page.getTotalCount();

		if (irrelevantSiteId != null) {
			FieldMapping irrelevantFieldMapping =
				testGetSiteFieldMappingsPage_addFieldMapping(
					irrelevantSiteId, randomIrrelevantFieldMapping());

			page = fieldMappingResource.getSiteFieldMappingsPage(
				irrelevantSiteId, null, null, null,
				Pagination.of(1, (int)totalCount + 1));

			Assert.assertEquals(totalCount + 1, page.getTotalCount());

			assertContains(
				irrelevantFieldMapping, (List<FieldMapping>)page.getItems());
			assertValid(
				page,
				testGetSiteFieldMappingsPage_getExpectedActions(
					irrelevantSiteId));
		}

		FieldMapping fieldMapping1 =
			testGetSiteFieldMappingsPage_addFieldMapping(
				siteId, randomFieldMapping());

		FieldMapping fieldMapping2 =
			testGetSiteFieldMappingsPage_addFieldMapping(
				siteId, randomFieldMapping());

		page = fieldMappingResource.getSiteFieldMappingsPage(
			siteId, null, null, null, Pagination.of(1, 10));

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(fieldMapping1, (List<FieldMapping>)page.getItems());
		assertContains(fieldMapping2, (List<FieldMapping>)page.getItems());
		assertValid(
			page, testGetSiteFieldMappingsPage_getExpectedActions(siteId));
	}

	protected Map<String, Map<String, String>>
			testGetSiteFieldMappingsPage_getExpectedActions(Long siteId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetSiteFieldMappingsPageWithPagination() throws Exception {
		Long siteId = testGetSiteFieldMappingsPage_getSiteId();

		Page<FieldMapping> fieldMappingsPage =
			fieldMappingResource.getSiteFieldMappingsPage(
				siteId, null, null, null, null);

		int totalCount = GetterUtil.getInteger(
			fieldMappingsPage.getTotalCount());

		FieldMapping fieldMapping1 =
			testGetSiteFieldMappingsPage_addFieldMapping(
				siteId, randomFieldMapping());

		FieldMapping fieldMapping2 =
			testGetSiteFieldMappingsPage_addFieldMapping(
				siteId, randomFieldMapping());

		FieldMapping fieldMapping3 =
			testGetSiteFieldMappingsPage_addFieldMapping(
				siteId, randomFieldMapping());

		// See com.liferay.portal.vulcan.internal.configuration.HeadlessAPICompanyConfiguration#pageSizeLimit

		int pageSizeLimit = 500;

		if (totalCount >= (pageSizeLimit - 2)) {
			Page<FieldMapping> page1 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null,
					Pagination.of(
						(int)Math.ceil((totalCount + 1.0) / pageSizeLimit),
						pageSizeLimit));

			Assert.assertEquals(totalCount + 3, page1.getTotalCount());

			assertContains(fieldMapping1, (List<FieldMapping>)page1.getItems());

			Page<FieldMapping> page2 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null,
					Pagination.of(
						(int)Math.ceil((totalCount + 2.0) / pageSizeLimit),
						pageSizeLimit));

			assertContains(fieldMapping2, (List<FieldMapping>)page2.getItems());

			Page<FieldMapping> page3 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null,
					Pagination.of(
						(int)Math.ceil((totalCount + 3.0) / pageSizeLimit),
						pageSizeLimit));

			assertContains(fieldMapping3, (List<FieldMapping>)page3.getItems());
		}
		else {
			Page<FieldMapping> page1 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null, Pagination.of(1, totalCount + 2));

			List<FieldMapping> fieldMappings1 =
				(List<FieldMapping>)page1.getItems();

			Assert.assertEquals(
				fieldMappings1.toString(), totalCount + 2,
				fieldMappings1.size());

			Page<FieldMapping> page2 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null, Pagination.of(2, totalCount + 2));

			Assert.assertEquals(totalCount + 3, page2.getTotalCount());

			List<FieldMapping> fieldMappings2 =
				(List<FieldMapping>)page2.getItems();

			Assert.assertEquals(
				fieldMappings2.toString(), 1, fieldMappings2.size());

			Page<FieldMapping> page3 =
				fieldMappingResource.getSiteFieldMappingsPage(
					siteId, null, null, null,
					Pagination.of(1, (int)totalCount + 3));

			assertContains(fieldMapping1, (List<FieldMapping>)page3.getItems());
			assertContains(fieldMapping2, (List<FieldMapping>)page3.getItems());
			assertContains(fieldMapping3, (List<FieldMapping>)page3.getItems());
		}
	}

	protected FieldMapping testGetSiteFieldMappingsPage_addFieldMapping(
			Long siteId, FieldMapping fieldMapping)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetSiteFieldMappingsPage_getSiteId() throws Exception {
		return testGroup.getGroupId();
	}

	protected Long testGetSiteFieldMappingsPage_getIrrelevantSiteId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	protected void assertContains(
		FieldMapping fieldMapping, List<FieldMapping> fieldMappings) {

		boolean contains = false;

		for (FieldMapping item : fieldMappings) {
			if (equals(fieldMapping, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			fieldMappings + " does not contain " + fieldMapping, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		FieldMapping fieldMapping1, FieldMapping fieldMapping2) {

		Assert.assertTrue(
			fieldMapping1 + " does not equal " + fieldMapping2,
			equals(fieldMapping1, fieldMapping2));
	}

	protected void assertEquals(
		List<FieldMapping> fieldMappings1, List<FieldMapping> fieldMappings2) {

		Assert.assertEquals(fieldMappings1.size(), fieldMappings2.size());

		for (int i = 0; i < fieldMappings1.size(); i++) {
			FieldMapping fieldMapping1 = fieldMappings1.get(i);
			FieldMapping fieldMapping2 = fieldMappings2.get(i);

			assertEquals(fieldMapping1, fieldMapping2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<FieldMapping> fieldMappings1, List<FieldMapping> fieldMappings2) {

		Assert.assertEquals(fieldMappings1.size(), fieldMappings2.size());

		for (FieldMapping fieldMapping1 : fieldMappings1) {
			boolean contains = false;

			for (FieldMapping fieldMapping2 : fieldMappings2) {
				if (equals(fieldMapping1, fieldMapping2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				fieldMappings2 + " does not contain " + fieldMapping1,
				contains);
		}
	}

	protected void assertValid(FieldMapping fieldMapping) throws Exception {
		boolean valid = true;

		if (fieldMapping.getDateModified() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("context", additionalAssertFieldName)) {
				if (fieldMapping.getContext() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("displayName", additionalAssertFieldName)) {
				if (fieldMapping.getDisplayName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("displayType", additionalAssertFieldName)) {
				if (fieldMapping.getDisplayType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("fieldName", additionalAssertFieldName)) {
				if (fieldMapping.getFieldName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("fieldType", additionalAssertFieldName)) {
				if (fieldMapping.getFieldType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("ownerType", additionalAssertFieldName)) {
				if (fieldMapping.getOwnerType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("repeatable", additionalAssertFieldName)) {
				if (fieldMapping.getRepeatable() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<FieldMapping> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<FieldMapping> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<FieldMapping> fieldMappings = page.getItems();

		int size = fieldMappings.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);

		assertValid(page.getActions(), expectedActions);
	}

	protected void assertValid(
		Map<String, Map<String, String>> actions1,
		Map<String, Map<String, String>> actions2) {

		for (String key : actions2.keySet()) {
			Map action = actions1.get(key);

			Assert.assertNotNull(key + " does not contain an action", action);

			Map<String, String> expectedAction = actions2.get(key);

			Assert.assertEquals(
				expectedAction.get("method"), action.get("method"));
			Assert.assertEquals(expectedAction.get("href"), action.get("href"));
		}
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.osb.faro.rest.dto.v1_0.FieldMapping.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			graphQLFields.addAll(getGraphQLFields(field));
		}

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(
			java.lang.reflect.Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		FieldMapping fieldMapping1, FieldMapping fieldMapping2) {

		if (fieldMapping1 == fieldMapping2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("context", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getContext(),
						fieldMapping2.getContext())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dateModified", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getDateModified(),
						fieldMapping2.getDateModified())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("displayName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getDisplayName(),
						fieldMapping2.getDisplayName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("displayType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getDisplayType(),
						fieldMapping2.getDisplayType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("fieldName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getFieldName(),
						fieldMapping2.getFieldName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("fieldType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getFieldType(),
						fieldMapping2.getFieldType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("ownerType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getOwnerType(),
						fieldMapping2.getOwnerType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("repeatable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						fieldMapping1.getRepeatable(),
						fieldMapping2.getRepeatable())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz)
		throws Exception {

		if (clazz.getClassLoader() == null) {
			return new java.lang.reflect.Field[0];
		}

		return TransformUtil.transform(
			ReflectionUtil.getDeclaredFields(clazz),
			field -> {
				if (field.isSynthetic()) {
					return null;
				}

				return field;
			},
			java.lang.reflect.Field.class);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_fieldMappingResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_fieldMappingResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		if (entityModel == null) {
			return Collections.emptyList();
		}

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		return TransformUtil.transform(
			getEntityFields(),
			entityField -> {
				if (!Objects.equals(entityField.getType(), type) ||
					ArrayUtil.contains(
						getIgnoredEntityFieldNames(), entityField.getName())) {

					return null;
				}

				return entityField;
			});
	}

	protected String getFilterString(
		EntityField entityField, String operator, FieldMapping fieldMapping) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("context")) {
			Object object = fieldMapping.getContext();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			if (operator.equals("between")) {
				Date date = fieldMapping.getDateModified();

				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(_format.format(date.getTime() - (2 * Time.SECOND)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(_format.format(date.getTime() + (2 * Time.SECOND)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_format.format(fieldMapping.getDateModified()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("displayName")) {
			Object object = fieldMapping.getDisplayName();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("displayType")) {
			Object object = fieldMapping.getDisplayType();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("fieldName")) {
			Object object = fieldMapping.getFieldName();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("fieldType")) {
			Object object = fieldMapping.getFieldType();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("ownerType")) {
			Object object = fieldMapping.getOwnerType();

			String value = String.valueOf(object);

			if (operator.equals("contains")) {
				sb = new StringBundler();

				sb.append("contains(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 2)) {
					sb.append(value.substring(1, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else if (operator.equals("startswith")) {
				sb = new StringBundler();

				sb.append("startswith(");
				sb.append(entityFieldName);
				sb.append(",'");

				if ((object != null) && (value.length() > 1)) {
					sb.append(value.substring(0, value.length() - 1));
				}
				else {
					sb.append(value);
				}

				sb.append("')");
			}
			else {
				sb.append("'");
				sb.append(value);
				sb.append("'");
			}

			return sb.toString();
		}

		if (entityFieldName.equals("repeatable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword(
			"test@liferay.com:" + PropsValues.DEFAULT_ADMIN_PASSWORD);

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected FieldMapping randomFieldMapping() throws Exception {
		return new FieldMapping() {
			{
				context = StringUtil.toLowerCase(RandomTestUtil.randomString());
				dateModified = RandomTestUtil.nextDate();
				displayName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				displayType = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				fieldName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				fieldType = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				ownerType = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				repeatable = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected FieldMapping randomIrrelevantFieldMapping() throws Exception {
		FieldMapping randomIrrelevantFieldMapping = randomFieldMapping();

		return randomIrrelevantFieldMapping;
	}

	protected FieldMapping randomPatchFieldMapping() throws Exception {
		return randomFieldMapping();
	}

	protected FieldMappingResource fieldMappingResource;
	protected com.liferay.portal.kernel.model.Group irrelevantGroup;
	protected com.liferay.portal.kernel.model.Company testCompany;
	protected com.liferay.portal.kernel.model.Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target)
			throws Exception {

			Class<?> sourceClass = source.getClass();

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field :
					_getAllDeclaredFields(sourceClass)) {

				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(
					sourceClass, field.getName(), "get");

				try {
					Method setMethod = _getMethod(
						targetClass, field.getName(), "set",
						getMethod.getReturnType());

					setMethod.invoke(target, getMethod.invoke(source));
				}
				catch (Exception e) {
					continue;
				}
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(
				bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value)
			throws Exception {

			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(
				clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static List<java.lang.reflect.Field> _getAllDeclaredFields(
			Class<?> clazz) {

			List<java.lang.reflect.Field> fields = new ArrayList<>();

			while ((clazz != null) && (clazz != Object.class)) {
				for (java.lang.reflect.Field field :
						clazz.getDeclaredFields()) {

					fields.add(field);
				}

				clazz = clazz.getSuperclass();
			}

			return fields;
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(
				Class<?> clazz, String fieldName, String prefix,
				Class<?>... parameterTypes)
			throws Exception {

			return clazz.getMethod(
				prefix + StringUtil.upperCaseFirstLetter(fieldName),
				parameterTypes);
		}

		private static Object _translateValue(
			Class<?> parameterType, Object value) {

			if ((value instanceof Integer) &&
				parameterType.equals(Long.class)) {

				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(
			Arrays.asList(
				Boolean.class, Date.class, Double.class, Integer.class,
				Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseFieldMappingResourceTestCase.class);

	private static Format _format;

	private com.liferay.portal.kernel.model.User _testCompanyAdminUser;

	@Inject
	private com.liferay.osb.faro.rest.resource.v1_0.FieldMappingResource
		_fieldMappingResource;

}
// LIFERAY-REST-BUILDER-HASH:641381954