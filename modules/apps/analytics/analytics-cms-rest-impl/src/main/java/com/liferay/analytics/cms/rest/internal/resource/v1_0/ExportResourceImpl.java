/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.analytics.cms.rest.internal.resource.v1_0;

import com.liferay.analytics.cms.rest.internal.client.AnalyticsCloudClient;
import com.liferay.analytics.cms.rest.internal.depot.entry.util.DepotEntryUtil;
import com.liferay.analytics.cms.rest.resource.v1_0.ExportResource;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.license.util.LicenseManagerUtil;
import com.liferay.portal.kernel.util.Http;

import jakarta.ws.rs.core.Response;

import java.util.Arrays;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Rachael Koestartyo
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/export.properties",
	scope = ServiceScope.PROTOTYPE, service = ExportResource.class
)
public class ExportResourceImpl extends BaseExportResourceImpl {

	@Override
	public Response getCategoryExport(Long[] depotEntryIds, Integer rangeKey)
		throws Exception {

		return _getExportResponse(
			"views-by-categories", "/categories/export", depotEntryIds,
			rangeKey);
	}

	@Override
	public Response getGeolocationExport(Long[] depotEntryIds, Integer rangeKey)
		throws Exception {

		return _getExportResponse(
			"views-by-geolocation", "/geolocation/export", depotEntryIds,
			rangeKey);
	}

	@Override
	public Response getSummaryExport(Long[] depotEntryIds, Integer rangeKey)
		throws Exception {

		return _getExportResponse(
			"top-assets", "/summaries/export", depotEntryIds, rangeKey);
	}

	private Response _getExportResponse(
			String fileName, String path, Long[] depotEntryIds,
			Integer rangeKey)
		throws Exception {

		LicenseManagerUtil.checkFreeTier();

		Long[] groupIds = DepotEntryUtil.getGroupIds(
			DepotEntryUtil.getDepotEntries(
				contextCompany.getCompanyId(), depotEntryIds));

		AnalyticsCloudClient analyticsCloudClient = new AnalyticsCloudClient(
			_http);

		String content = analyticsCloudClient.getExport(
			_analyticsSettingsManager.getAnalyticsConfiguration(
				contextCompany.getCompanyId()),
			Arrays.asList(groupIds), path, rangeKey);

		return Response.ok(
			content
		).header(
			"Content-Disposition",
			StringBundler.concat("attachment; filename=", fileName, ".csv")
		).build();
	}

	@Reference
	private AnalyticsSettingsManager _analyticsSettingsManager;

	@Reference
	private Http _http;

}