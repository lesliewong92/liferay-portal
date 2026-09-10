/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.oauth2;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * @author Leslie Wong
 */
public class AIHubCellOAuth2ApplicationScopeAliasesUpdaterTest {

	@Before
	public void setUp() {
		Mockito.when(
			_oAuth2Application.getOAuth2ApplicationId()
		).thenReturn(
			_OAUTH2_APPLICATION_ID
		);

		Mockito.when(
			_oAuth2Application.getOAuth2ApplicationScopeAliasesId()
		).thenReturn(
			_OAUTH2_APPLICATION_SCOPE_ALIASES_ID
		);

		Mockito.when(
			_oAuth2Application.getUserId()
		).thenReturn(
			_USER_ID
		);

		Mockito.when(
			_oAuth2Application.getUserName()
		).thenReturn(
			_USER_NAME
		);

		Mockito.when(
			_oAuth2ApplicationLocalService.
				fetchOAuth2ApplicationByExternalReferenceCode(
					"AI-HUB-CELL", _COMPANY_ID)
		).thenReturn(
			_oAuth2Application
		);

		ReflectionTestUtil.setFieldValue(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater,
			"_oAuth2ApplicationLocalService", _oAuth2ApplicationLocalService);
		ReflectionTestUtil.setFieldValue(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater,
			"_oAuth2ApplicationScopeAliasesLocalService",
			_oAuth2ApplicationScopeAliasesLocalService);
		ReflectionTestUtil.setFieldValue(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater, "_scopeLocator",
			_scopeLocator);
	}

	@Test
	public void testUpdateScopeAliases() throws Exception {
		String scopeAlias1 = RandomTestUtil.randomString();
		String scopeAlias2 = RandomTestUtil.randomString();

		_mockLiferayOAuth2Scopes(scopeAlias1, scopeAlias2, _SCOPE_ALIAS);
		_mockScopeAliasesList(scopeAlias1, scopeAlias2);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		Mockito.verify(
			_scopeLocator
		).getLiferayOAuth2Scopes(
			_COMPANY_ID
		);

		List<String> scopeAliasesList = _getScopeAliasesList();

		Assert.assertEquals(
			scopeAliasesList.toString(), 3, scopeAliasesList.size());
		Assert.assertTrue(
			scopeAliasesList.containsAll(
				Arrays.asList(scopeAlias1, scopeAlias2, _SCOPE_ALIAS)));
	}

	@Test
	public void testUpdateScopeAliasesWithExistingScopeAlias()
		throws Exception {

		_mockScopeAliasesList(RandomTestUtil.randomString(), _SCOPE_ALIAS);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		_verifyNoUpdate();

		Mockito.verifyNoInteractions(_scopeLocator);
	}

	@Test
	public void testUpdateScopeAliasesWithoutOAuth2Application()
		throws Exception {

		Mockito.when(
			_oAuth2ApplicationLocalService.
				fetchOAuth2ApplicationByExternalReferenceCode(
					"AI-HUB-CELL", _COMPANY_ID)
		).thenReturn(
			null
		);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		_verifyNoUpdate();

		Mockito.verifyNoInteractions(
			_oAuth2ApplicationScopeAliasesLocalService, _scopeLocator);
	}

	@Test
	public void testUpdateScopeAliasesWithoutScopeAliases() throws Exception {
		_mockLiferayOAuth2Scopes(_SCOPE_ALIAS);
		_mockScopeAliasesList();

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		Assert.assertEquals(
			Collections.singletonList(_SCOPE_ALIAS), _getScopeAliasesList());
	}

	@Test
	public void testUpdateScopeAliasesWithPortalException() throws Exception {
		_mockLiferayOAuth2Scopes(_SCOPE_ALIAS);
		_mockScopeAliasesList();

		Mockito.when(
			_oAuth2ApplicationLocalService.updateScopeAliases(
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(),
				Mockito.anyList())
		).thenThrow(
			new PortalException()
		);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);
	}

	@Test
	public void testUpdateScopeAliasesWithUnresolvableScopeAlias()
		throws Exception {

		String scopeAlias = RandomTestUtil.randomString();

		_mockLiferayOAuth2Scopes(scopeAlias);
		_mockScopeAliasesList(scopeAlias);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		_verifyNoUpdate();

		_mockLiferayOAuth2Scopes(_SCOPE_ALIAS);

		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			_COMPANY_ID);

		_verifyNoUpdate();
	}

	private List<String> _getScopeAliasesList() throws Exception {
		ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(
			List.class);

		Mockito.verify(
			_oAuth2ApplicationLocalService
		).updateScopeAliases(
			Mockito.eq(_USER_ID), Mockito.eq(_USER_NAME),
			Mockito.eq(_OAUTH2_APPLICATION_ID), argumentCaptor.capture()
		);

		return argumentCaptor.getValue();
	}

	private void _mockLiferayOAuth2Scopes(String... scopeAliases) {
		Mockito.reset(_scopeLocator);

		for (String scopeAlias : scopeAliases) {
			Mockito.when(
				_scopeLocator.getLiferayOAuth2Scopes(_COMPANY_ID, scopeAlias)
			).thenReturn(
				Collections.singletonList(
					Mockito.mock(LiferayOAuth2Scope.class))
			);
		}
	}

	private void _mockScopeAliasesList(String... scopeAliases) {
		Mockito.when(
			_oAuth2ApplicationScopeAliasesLocalService.getScopeAliasesList(
				_OAUTH2_APPLICATION_SCOPE_ALIASES_ID)
		).thenReturn(
			Arrays.asList(scopeAliases)
		);
	}

	private void _verifyNoUpdate() throws Exception {
		Mockito.verify(
			_oAuth2ApplicationLocalService, Mockito.never()
		).updateScopeAliases(
			Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(),
			Mockito.anyList()
		);
	}

	private static final long _COMPANY_ID = RandomTestUtil.randomLong();

	private static final long _OAUTH2_APPLICATION_ID =
		RandomTestUtil.randomLong();

	private static final long _OAUTH2_APPLICATION_SCOPE_ALIASES_ID =
		RandomTestUtil.randomLong();

	private static final String _SCOPE_ALIAS =
		"Liferay.Faro.Rest.everything.read";

	private static final long _USER_ID = RandomTestUtil.randomLong();

	private static final String _USER_NAME = RandomTestUtil.randomString();

	private final AIHubCellOAuth2ApplicationScopeAliasesUpdater
		_aiHubCellOAuth2ApplicationScopeAliasesUpdater =
			new AIHubCellOAuth2ApplicationScopeAliasesUpdater();
	private final OAuth2Application _oAuth2Application = Mockito.mock(
		OAuth2Application.class);
	private final OAuth2ApplicationLocalService _oAuth2ApplicationLocalService =
		Mockito.mock(OAuth2ApplicationLocalService.class);
	private final OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService = Mockito.mock(
			OAuth2ApplicationScopeAliasesLocalService.class);
	private final ScopeLocator _scopeLocator = Mockito.mock(ScopeLocator.class);

}