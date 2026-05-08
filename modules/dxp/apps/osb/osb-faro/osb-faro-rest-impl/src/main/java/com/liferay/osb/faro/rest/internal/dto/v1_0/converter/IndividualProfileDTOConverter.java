/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteIndividualProfileResponse;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leslie Wong
 */
@Component(
	property = "dto.class.name=com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteIndividualProfileResponse$Individual",
	service = DTOConverter.class
)
public class IndividualProfileDTOConverter
	implements DTOConverter
		<GetSiteIndividualProfileResponse.Individual, Individual> {

	@Override
	public String getContentType() {
		return Individual.class.getSimpleName();
	}

	@Override
	public Individual toDTO(
		DTOConverterContext dtoConverterContext,
		GetSiteIndividualProfileResponse.Individual graphQLIndividual) {

		if (graphQLIndividual == null) {
			return null;
		}

		return new Individual() {
			{
				setAccountName(graphQLIndividual::getAccountName);
				setActivitiesCount(graphQLIndividual::getActivitiesCount);
				setDateCreated(graphQLIndividual::getDateCreated);
				setDateModified(graphQLIndividual::getDateModified);
				setFirstActivityDate(graphQLIndividual::getFirstActivityDate);
				setId(graphQLIndividual::getId);
				setLastActivityDate(graphQLIndividual::getLastActivityDate);
				setLastSessionCountry(graphQLIndividual::getLastSessionCountry);
				setProfileType(graphQLIndividual::getProfileType);
			}
		};
	}

}