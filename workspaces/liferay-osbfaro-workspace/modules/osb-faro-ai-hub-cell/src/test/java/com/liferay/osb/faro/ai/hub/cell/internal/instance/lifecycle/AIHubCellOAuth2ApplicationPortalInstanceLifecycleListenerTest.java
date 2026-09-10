/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.instance.lifecycle;

import com.liferay.osb.faro.ai.hub.cell.internal.oauth2.AIHubCellOAuth2ApplicationScopeAliasesUpdater;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Leslie Wong
 */
public class AIHubCellOAuth2ApplicationPortalInstanceLifecycleListenerTest {

	@Test
	public void testPortalInstanceRegistered() throws Exception {
		AIHubCellOAuth2ApplicationScopeAliasesUpdater
			aiHubCellOAuth2ApplicationScopeAliasesUpdater = Mockito.mock(
				AIHubCellOAuth2ApplicationScopeAliasesUpdater.class);

		AIHubCellOAuth2ApplicationPortalInstanceLifecycleListener
			aiHubCellOAuth2ApplicationPortalInstanceLifecycleListener =
				new AIHubCellOAuth2ApplicationPortalInstanceLifecycleListener();

		ReflectionTestUtil.setFieldValue(
			aiHubCellOAuth2ApplicationPortalInstanceLifecycleListener,
			"_aiHubCellOAuth2ApplicationScopeAliasesUpdater",
			aiHubCellOAuth2ApplicationScopeAliasesUpdater);

		long companyId = RandomTestUtil.randomLong();

		Company company = Mockito.mock(Company.class);

		Mockito.when(
			company.getCompanyId()
		).thenReturn(
			companyId
		);

		aiHubCellOAuth2ApplicationPortalInstanceLifecycleListener.
			portalInstanceRegistered(company);

		Mockito.verify(
			aiHubCellOAuth2ApplicationScopeAliasesUpdater
		).updateScopeAliases(
			companyId
		);
	}

}