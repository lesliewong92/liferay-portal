/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.ai.hub.cell.internal.instance.lifecycle;

import com.liferay.osb.faro.ai.hub.cell.internal.oauth2.AIHubCellOAuth2ApplicationScopeAliasesUpdater;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.EveryNodeEveryStartup;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;

import jakarta.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leslie Wong
 */
@Component(service = PortalInstanceLifecycleListener.class)
public class AIHubCellOAuth2ApplicationPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener
	implements EveryNodeEveryStartup {

	@Override
	public void portalInstanceRegistered(Company company) {
		_aiHubCellOAuth2ApplicationScopeAliasesUpdater.updateScopeAliases(
			company.getCompanyId());
	}

	@Reference
	private AIHubCellOAuth2ApplicationScopeAliasesUpdater
		_aiHubCellOAuth2ApplicationScopeAliasesUpdater;

	@Reference(target = "(osgi.jaxrs.name=Liferay.Faro.Rest)")
	private Application _application;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

}