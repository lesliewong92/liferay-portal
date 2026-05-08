/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegment;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leslie Wong
 */
@Component(
	property = "dto.class.name=com.liferay.osb.faro.engine.client.model.IndividualSegment",
	service = DTOConverter.class
)
public class IndividualSegmentDTOConverter
	implements DTOConverter
		<com.liferay.osb.faro.engine.client.model.IndividualSegment,
		 IndividualSegment> {

	@Override
	public String getContentType() {
		return IndividualSegment.class.getSimpleName();
	}

	@Override
	public IndividualSegment toDTO(
		DTOConverterContext dtoConverterContext,
		com.liferay.osb.faro.engine.client.model.IndividualSegment
			engineIndividualSegment) {

		if (engineIndividualSegment == null) {
			return null;
		}

		return new IndividualSegment() {
			{
				setActiveIndividualCount(
					engineIndividualSegment::getActiveIndividualCount);
				setAnonymousIndividualCount(
					engineIndividualSegment::getAnonymousIndividualCount);
				setChannelId(engineIndividualSegment::getChannelId);
				setDateCreated(engineIndividualSegment::getDateCreated);
				setDateModified(engineIndividualSegment::getDateModified);
				setFilter(engineIndividualSegment::getFilterString);
				setId(engineIndividualSegment::getId);
				setIncludeAnonymousUsers(
					engineIndividualSegment::isIncludeAnonymousUsers);
				setIndividualCount(engineIndividualSegment::getIndividualCount);
				setKnownIndividualCount(
					engineIndividualSegment::getKnownIndividualCount);
				setLastActivityDate(
					engineIndividualSegment::getLastActivityDate);
				setName(engineIndividualSegment::getName);
				setSegmentType(engineIndividualSegment::getSegmentType);
				setState(engineIndividualSegment::getState);
				setStatus(engineIndividualSegment::getStatus);
			}
		};
	}

}