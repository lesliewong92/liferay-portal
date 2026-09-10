/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.oauth2.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.oauth2.provider.constants.ClientProfile;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leslie Wong
 */
@RunWith(Arquillian.class)
public class AIHubCellOAuth2ApplicationScopeAliasesUpdaterImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_originalOAuth2Application = _fetchOAuth2Application();
	}

	@After
	public void tearDown() throws Exception {
		if (_originalOAuth2Application != null) {
			return;
		}

		OAuth2Application oAuth2Application = _fetchOAuth2Application();

		if (oAuth2Application != null) {
			_oAuth2ApplicationLocalService.deleteOAuth2Application(
				oAuth2Application);
		}
	}

	@Test
	public void testGetLiferayOAuth2Scopes() throws Exception {
		Collection<LiferayOAuth2Scope> liferayOAuth2Scopes =
			_scopeLocator.getLiferayOAuth2Scopes(
				TestPropsValues.getCompanyId(), _SCOPE_ALIAS);

		Assert.assertFalse(liferayOAuth2Scopes.isEmpty());
	}

	@Test
	public void testUpdateScopeAliases() throws Exception {
		_addOAuth2Application();

		OAuth2Application oAuth2Application = _fetchOAuth2Application();

		List<String> scopeAliasesList =
			_oAuth2ApplicationScopeAliasesLocalService.getScopeAliasesList(
				oAuth2Application.getOAuth2ApplicationScopeAliasesId());

		Assert.assertTrue(scopeAliasesList.contains(_SCOPE_ALIAS));
	}

	private void _addOAuth2Application() throws Exception {
		User user = TestPropsValues.getUser();

		_oAuth2ApplicationLocalService.addOrUpdateOAuth2Application(
			"AI-HUB-CELL", user.getUserId(), user.getScreenName(),
			Arrays.asList(GrantType.CLIENT_CREDENTIALS), "client_secret_post",
			user.getUserId(), RandomTestUtil.randomString(),
			ClientProfile.HEADLESS_SERVER.id(), RandomTestUtil.randomString(),
			null, null, "http://localhost", 0, null,
			RandomTestUtil.randomString(), null, Arrays.asList(), false,
			Arrays.asList("Liferay.Headless.Admin.User.everything.read"), false,
			new ServiceContext());
	}

	private OAuth2Application _fetchOAuth2Application() throws Exception {
		return _oAuth2ApplicationLocalService.
			fetchOAuth2ApplicationByExternalReferenceCode(
				"AI-HUB-CELL", TestPropsValues.getCompanyId());
	}

	private static final String _SCOPE_ALIAS =
		"Liferay.Faro.Rest.everything.read";

	@Inject
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Inject
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	private OAuth2Application _originalOAuth2Application;

	@Inject
	private ScopeLocator _scopeLocator;

}