/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.model.listener;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.osb.faro.ai.hub.cell.internal.oauth2.AIHubCellOAuth2ApplicationScopeAliasesUpdater;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Leslie Wong
 */
public class AIHubCellOAuth2ApplicationModelListenerTest {

	@Before
	public void setUp() {
		Mockito.when(
			_oAuth2Application.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);

		ReflectionTestUtil.setFieldValue(
			_aiHubCellOAuth2ApplicationModelListener,
			"_aiHubCellOAuth2ApplicationScopeAliasesUpdater",
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater);
	}

	@Test
	public void testOnAfterUpdate() throws Exception {
		_mockExternalReferenceCode("AI-HUB-CELL");

		_aiHubCellOAuth2ApplicationModelListener.onAfterUpdate(
			_oAuth2Application, _oAuth2Application);

		Mockito.verify(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater
		).updateScopeAliases(
			_COMPANY_ID
		);
	}

	@Test
	public void testOnAfterUpdateWithException() throws Exception {
		_mockExternalReferenceCode("AI-HUB-CELL");

		Mockito.doThrow(
			new RuntimeException()
		).when(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater
		).updateScopeAliases(
			_COMPANY_ID
		);

		_aiHubCellOAuth2ApplicationModelListener.onAfterUpdate(
			_oAuth2Application, _oAuth2Application);
	}

	@Test
	public void testOnAfterUpdateWithOtherExternalReferenceCode()
		throws Exception {

		_mockExternalReferenceCode(RandomTestUtil.randomString());

		_aiHubCellOAuth2ApplicationModelListener.onAfterUpdate(
			_oAuth2Application, _oAuth2Application);

		Mockito.verifyNoInteractions(
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater);
	}

	private void _mockExternalReferenceCode(String externalReferenceCode) {
		Mockito.when(
			_oAuth2Application.getExternalReferenceCode()
		).thenReturn(
			externalReferenceCode
		);
	}

	private static final long _COMPANY_ID = RandomTestUtil.randomLong();

	private final AIHubCellOAuth2ApplicationModelListener
		_aiHubCellOAuth2ApplicationModelListener =
			new AIHubCellOAuth2ApplicationModelListener();
	private final AIHubCellOAuth2ApplicationScopeAliasesUpdater
		_aiHubCellOAuth2ApplicationScopeAliasesUpdater = Mockito.mock(
			AIHubCellOAuth2ApplicationScopeAliasesUpdater.class);
	private final OAuth2Application _oAuth2Application = Mockito.mock(
		OAuth2Application.class);

}