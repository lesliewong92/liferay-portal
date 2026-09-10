/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.model.listener;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.osb.faro.ai.hub.cell.internal.oauth2.AIHubCellOAuth2ApplicationScopeAliasesUpdater;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.StringUtil;

import jakarta.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leslie Wong
 */
@Component(service = ModelListener.class)
public class AIHubCellOAuth2ApplicationModelListener
	extends BaseModelListener<OAuth2Application> {

	@Override
	public void onAfterUpdate(
			OAuth2Application originalOAuth2Application,
			OAuth2Application oAuth2Application)
		throws ModelListenerException {

		if (!StringUtil.equals(
				oAuth2Application.getExternalReferenceCode(), "AI-HUB-CELL")) {

			return;
		}

		try {
			_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
				oAuth2Application.getCompanyId());
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AIHubCellOAuth2ApplicationModelListener.class);

	@Reference
	private AIHubCellOAuth2ApplicationScopeAliasesUpdater
		_aiHubCellOAuth2ApplicationScopeAliasesUpdater;

	@Reference(target = "(osgi.jaxrs.name=Liferay.Faro.Rest)")
	private Application _application;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

}