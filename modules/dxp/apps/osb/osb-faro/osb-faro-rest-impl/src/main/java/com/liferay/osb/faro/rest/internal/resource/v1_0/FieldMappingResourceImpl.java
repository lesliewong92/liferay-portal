/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.resource.v1_0;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.rest.dto.v1_0.FieldMapping;
import com.liferay.osb.faro.rest.internal.dto.v1_0.converter.FaroDTOConverterContext;
import com.liferay.osb.faro.rest.internal.dto.v1_0.util.FaroPaginationUtil;
import com.liferay.osb.faro.rest.resource.v1_0.FieldMappingResource;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Leslie Wong
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/field-mapping.properties",
	scope = ServiceScope.PROTOTYPE, service = FieldMappingResource.class
)
public class FieldMappingResourceImpl extends BaseFieldMappingResourceImpl {

	@Override
	public FieldMapping getSiteFieldMapping(Long siteId, String fieldMappingId)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(siteId);

		return _fieldMappingDTOConverter.toDTO(
			new FaroDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(), fieldMappingId,
				contextAcceptLanguage.getPreferredLocale()),
			_contactsEngineClient.getFieldMapping(faroProject, fieldMappingId));
	}

	@Override
	public Page<FieldMapping> getSiteFieldMappingsPage(
			Long siteId, String context, String ownerType, String search,
			Pagination pagination)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(siteId);

		Results<com.liferay.osb.faro.engine.client.model.FieldMapping> results =
			_contactsEngineClient.getFieldMappings(
				faroProject, context, null, ownerType, search,
				FaroPaginationUtil.getCur(pagination),
				FaroPaginationUtil.getDelta(pagination), null);

		return Page.of(
			transform(
				results.getItems(),
				engineFieldMapping -> _fieldMappingDTOConverter.toDTO(
					new FaroDTOConverterContext(
						contextAcceptLanguage.isAcceptAllLanguages(),
						engineFieldMapping.getFieldName(),
						contextAcceptLanguage.getPreferredLocale()),
					engineFieldMapping)),
			pagination, results.getTotal());
	}

	@Reference
	private ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference(
		target = "(component.name=com.liferay.osb.faro.rest.internal.dto.v1_0.converter.FieldMappingDTOConverter)"
	)
	private DTOConverter
		<com.liferay.osb.faro.engine.client.model.FieldMapping, FieldMapping>
			_fieldMappingDTOConverter;

}