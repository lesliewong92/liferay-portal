/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.rest.dto.v1_0.FieldMapping;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leslie Wong
 */
@Component(
	property = "dto.class.name=com.liferay.osb.faro.engine.client.model.FieldMapping",
	service = DTOConverter.class
)
public class FieldMappingDTOConverter
	implements DTOConverter
		<com.liferay.osb.faro.engine.client.model.FieldMapping, FieldMapping> {

	@Override
	public String getContentType() {
		return FieldMapping.class.getSimpleName();
	}

	@Override
	public FieldMapping toDTO(
		DTOConverterContext dtoConverterContext,
		com.liferay.osb.faro.engine.client.model.FieldMapping
			engineFieldMapping) {

		if (engineFieldMapping == null) {
			return null;
		}

		return new FieldMapping() {
			{
				setContext(engineFieldMapping::getContext);
				setDateModified(engineFieldMapping::getDateModified);
				setDisplayName(engineFieldMapping::getDisplayName);
				setDisplayType(engineFieldMapping::getDisplayType);
				setFieldName(engineFieldMapping::getFieldName);
				setFieldType(engineFieldMapping::getFieldType);
				setOwnerType(engineFieldMapping::getOwnerType);
				setRepeatable(engineFieldMapping::getRepeatable);
			}
		};
	}

}