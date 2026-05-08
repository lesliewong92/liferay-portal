/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualDemographicField;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leslie Wong
 */
@Component(
	property = "dto.class.name=com.liferay.osb.faro.engine.client.model.Individual",
	service = DTOConverter.class
)
public class IndividualDTOConverter
	implements DTOConverter
		<com.liferay.osb.faro.engine.client.model.Individual, Individual> {

	@Override
	public String getContentType() {
		return Individual.class.getSimpleName();
	}

	@Override
	public Individual toDTO(
		DTOConverterContext dtoConverterContext,
		com.liferay.osb.faro.engine.client.model.Individual engineIndividual) {

		if (engineIndividual == null) {
			return null;
		}

		return new Individual() {
			{
				setAccountName(engineIndividual::getAccountName);
				setActivitiesCount(engineIndividual::getActivitiesCount);
				setDateCreated(engineIndividual::getDateCreated);
				setDateModified(engineIndividual::getDateModified);
				setDemographics(
					() -> _toDemographics(
						dtoConverterContext,
						engineIndividual.getDemographics()));
				setFirstActivityDate(engineIndividual::getFirstActivityDate);
				setId(engineIndividual::getId);
				setLastActivityDate(engineIndividual::getLastActivityDate);
				setLastSessionCountry(engineIndividual::getLastSessionCountry);
				setProfileType(engineIndividual::getProfileType);
			}
		};
	}

	private Map<String, List<IndividualDemographicField>> _toDemographics(
			DTOConverterContext dtoConverterContext,
			Map<String, List<Field>> engineDemographics)
		throws Exception {

		if ((engineDemographics == null) || engineDemographics.isEmpty()) {
			return null;
		}

		Map<String, List<IndividualDemographicField>> demographics =
			new HashMap<>();

		for (Map.Entry<String, List<Field>> entry :
				engineDemographics.entrySet()) {

			List<Field> engineFields = entry.getValue();

			if (engineFields == null) {
				continue;
			}

			List<IndividualDemographicField> individualDemographicFields =
				new ArrayList<>(engineFields.size());

			for (Field engineField : engineFields) {
				individualDemographicFields.add(
					_individualDemographicFieldDTOConverter.toDTO(
						dtoConverterContext, engineField));
			}

			demographics.put(entry.getKey(), individualDemographicFields);
		}

		return demographics;
	}

	@Reference(
		target = "(component.name=com.liferay.osb.faro.rest.internal.dto.v1_0.converter.IndividualDemographicFieldDTOConverter)"
	)
	private DTOConverter<Field, IndividualDemographicField>
		_individualDemographicFieldDTOConverter;

}