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
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leslie Wong
 */
@Component(service = AIHubCellOAuth2ApplicationScopeAliasesUpdater.class)
public class AIHubCellOAuth2ApplicationScopeAliasesUpdater {

	public void updateScopeAliases(long companyId) {
		try {
			_updateScopeAliases(companyId);
		}
		catch (PortalException portalException) {
			_log.error(
				StringBundler.concat(
					"Unable to update the scope aliases of the AI Hub Cell ",
					"OAuth2 application for company ", companyId),
				portalException);
		}
	}

	private void _updateScopeAliases(long companyId) throws PortalException {
		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.
				fetchOAuth2ApplicationByExternalReferenceCode(
					"AI-HUB-CELL", companyId);

		if (oAuth2Application == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"The AI Hub Cell OAuth2 application does not exist for " +
						"company " + companyId);
			}

			return;
		}

		List<String> scopeAliasesList = new ArrayList<>(
			_oAuth2ApplicationScopeAliasesLocalService.getScopeAliasesList(
				oAuth2Application.getOAuth2ApplicationScopeAliasesId()));

		if (scopeAliasesList.contains(_SCOPE_ALIAS)) {
			return;
		}

		_scopeLocator.getLiferayOAuth2Scopes(companyId);

		scopeAliasesList.add(_SCOPE_ALIAS);

		for (String scopeAlias : scopeAliasesList) {
			Collection<LiferayOAuth2Scope> liferayOAuth2Scopes =
				_scopeLocator.getLiferayOAuth2Scopes(companyId, scopeAlias);

			if (liferayOAuth2Scopes.isEmpty()) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"Skipping the AI Hub Cell OAuth2 application of ",
							"company ", companyId,
							" because the scope alias \"", scopeAlias,
							"\" does not resolve to any scope"));
				}

				return;
			}
		}

		_oAuth2ApplicationLocalService.updateScopeAliases(
			oAuth2Application.getUserId(), oAuth2Application.getUserName(),
			oAuth2Application.getOAuth2ApplicationId(), scopeAliasesList);

		if (_log.isInfoEnabled()) {
			_log.info(
				StringBundler.concat(
					"Granted the scope alias \"", _SCOPE_ALIAS,
					"\" to the AI Hub Cell OAuth2 application of company ",
					companyId));
		}
	}

	private static final String _SCOPE_ALIAS =
		"Liferay.Faro.Rest.everything.read";

	private static final Log _log = LogFactoryUtil.getLog(
		AIHubCellOAuth2ApplicationScopeAliasesUpdater.class);

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private ScopeLocator _scopeLocator;

}