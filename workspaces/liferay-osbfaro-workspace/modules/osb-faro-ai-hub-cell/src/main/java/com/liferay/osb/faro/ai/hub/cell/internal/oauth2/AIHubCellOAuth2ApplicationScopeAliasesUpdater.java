/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.oauth2;

/**
 * @author Leslie Wong
 */
public interface AIHubCellOAuth2ApplicationScopeAliasesUpdater {

	public void updateScopeAliases(long companyId);

}