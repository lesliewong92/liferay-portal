/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.resource.v1_0;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.lang.reflect.Field;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leslie Wong
 */
public class IndividualResourceImplTest {

	@Before
	public void setUp() throws Exception {
		_individualResourceImpl = new IndividualResourceImpl();

		_setDeclaredField(
			"_contactsEngineClient",
			ProxyUtil.newProxyInstance(
				ContactsEngineClient.class.getClassLoader(),
				new Class<?>[] {ContactsEngineClient.class},
				(proxy, method, arguments) -> {
					String name = method.getName();

					if (!name.equals("getIndividuals") ||
						(arguments.length != 20)) {

						return null;
					}

					_arguments = arguments;

					return _results;
				}));
		_setDeclaredField(
			"_faroProjectLocalService",
			ProxyUtil.newProxyInstance(
				FaroProjectLocalService.class.getClassLoader(),
				new Class<?>[] {FaroProjectLocalService.class},
				(proxy, method, arguments) -> null));
	}

	@Test
	public void testGetWorkspaceGroupChannelIndividualsPagePassesFiltersToEngine()
		throws Exception {

		_individualResourceImpl.getWorkspaceGroupChannelIndividualsPage(
			1L, "channel-1", "account-1", "ACTIVE", true, "segment-1",
			"analytics", "2026-01-31", "LAST_30_DAYS", "2026-01-01", "jane",
			Pagination.of(2, 5), _sorts);

		_assertFilters();
	}

	@Test
	public void testGetWorkspaceGroupIndividualSegmentIndividualsPagePassesFiltersToEngine()
		throws Exception {

		_individualResourceImpl.
			getWorkspaceGroupIndividualSegmentIndividualsPage(
				1L, "segment-1", "account-1", "ACTIVE", "channel-1", true,
				"analytics", "2026-01-31", "LAST_30_DAYS", "2026-01-01", "jane",
				Pagination.of(2, 5), _sorts);

		_assertFilters();
	}

	@Test
	public void testGetWorkspaceGroupIndividualSegmentIndividualsPageReportsEngineTotal()
		throws Exception {

		_results.setTotal(42);

		Page<Individual> individualsPage =
			_individualResourceImpl.
				getWorkspaceGroupIndividualSegmentIndividualsPage(
					1L, "segment-1", null, null, null, null, null, null, null,
					null, null, Pagination.of(1, 20), null);

		Assert.assertEquals(42, individualsPage.getTotalCount());
	}

	private void _assertFilters() {
		Assert.assertEquals("account-1", _arguments[1]);
		Assert.assertNull(_arguments[2]);
		Assert.assertEquals("ACTIVE", _arguments[3]);
		Assert.assertEquals("channel-1", _arguments[4]);
		Assert.assertNull(_arguments[5]);
		Assert.assertNull(_arguments[6]);
		Assert.assertNull(_arguments[7]);
		Assert.assertEquals(Boolean.TRUE, _arguments[8]);
		Assert.assertEquals("segment-1", _arguments[9]);
		Assert.assertNull(_arguments[10]);
		Assert.assertEquals("analytics", _arguments[11]);
		Assert.assertNull(_arguments[12]);
		Assert.assertEquals("jane", _arguments[13]);
		Assert.assertEquals("2026-01-31", _arguments[14]);
		Assert.assertEquals(Integer.valueOf(30), _arguments[15]);
		Assert.assertEquals("2026-01-01", _arguments[16]);
		Assert.assertEquals(Integer.valueOf(2), _arguments[17]);
		Assert.assertEquals(Integer.valueOf(5), _arguments[18]);

		List<OrderByField> orderByFields = (List<OrderByField>)_arguments[19];

		Assert.assertEquals(orderByFields.toString(), 1, orderByFields.size());

		OrderByField orderByField = orderByFields.get(0);

		Assert.assertEquals("lastActivityDate", orderByField.getFieldName());
		Assert.assertEquals(
			OrderByField.OrderBy.desc, orderByField.getOrderBy());
	}

	private void _setDeclaredField(String name, Object value) throws Exception {
		Field field = IndividualResourceImpl.class.getDeclaredField(name);

		field.setAccessible(true);

		field.set(_individualResourceImpl, value);
	}

	private Object[] _arguments;
	private IndividualResourceImpl _individualResourceImpl;
	private final Results<com.liferay.osb.faro.engine.client.model.Individual>
		_results = new Results<>();
	private final Sort[] _sorts = {
		new Sort("lastActivityDate", Sort.STRING_TYPE, true)
	};

}