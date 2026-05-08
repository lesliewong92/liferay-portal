/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.resource.v1_0;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.internal.dto.v1_0.converter.FaroDTOConverterContext;
import com.liferay.osb.faro.rest.internal.dto.v1_0.util.FaroPaginationUtil;
import com.liferay.osb.faro.rest.internal.graphql.client.FaroGraphQLClient;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteIndividualProfileResponse;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualResource;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Leslie Wong
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/individual.properties",
	scope = ServiceScope.PROTOTYPE, service = IndividualResource.class
)
public class IndividualResourceImpl extends BaseIndividualResourceImpl {

	@Override
	public Individual getSiteIndividual(
			Long siteId, String individualId, String channelId)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(siteId);

		return _individualDTOConverter.toDTO(
			new FaroDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(), individualId,
				contextAcceptLanguage.getPreferredLocale()),
			_contactsEngineClient.getIndividual(
				faroProject, individualId, channelId));
	}

	@Override
	public Individual getSiteIndividualProfile(Long siteId, String individualId)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(siteId);

		GetSiteIndividualProfileResponse response = _faroGraphQLClient.execute(
			faroProject, "getSiteIndividualProfile",
			Collections.singletonMap("individualId", individualId),
			GetSiteIndividualProfileResponse.class);

		return _individualProfileDTOConverter.toDTO(
			new FaroDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(), individualId,
				contextAcceptLanguage.getPreferredLocale()),
			response.getIndividual());
	}

	@Override
	public Page<Individual> getSiteIndividualsPage(
			Long siteId, String accountId, String channelId,
			String dataSourceId, Boolean includeAnonymousUsers,
			String individualSegmentId, String interestName, String search,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(siteId);

		Results<com.liferay.osb.faro.engine.client.model.Individual> results =
			_contactsEngineClient.getIndividuals(
				faroProject, accountId, channelId, dataSourceId,
				individualSegmentId, null, interestName, null, null, search,
				null, (includeAnonymousUsers != null) && includeAnonymousUsers,
				FaroPaginationUtil.getCur(pagination),
				FaroPaginationUtil.getDelta(pagination),
				FaroPaginationUtil.toOrderByFields(sorts));

		return Page.of(
			transform(
				results.getItems(),
				engineIndividual -> _individualDTOConverter.toDTO(
					new FaroDTOConverterContext(
						contextAcceptLanguage.isAcceptAllLanguages(),
						engineIndividual.getId(),
						contextAcceptLanguage.getPreferredLocale()),
					engineIndividual)),
			pagination, results.getTotal());
	}

	@Reference
	private ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroGraphQLClient _faroGraphQLClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference(
		target = "(component.name=com.liferay.osb.faro.rest.internal.dto.v1_0.converter.IndividualDTOConverter)"
	)
	private DTOConverter
		<com.liferay.osb.faro.engine.client.model.Individual, Individual>
			_individualDTOConverter;

	@Reference(
		target = "(component.name=com.liferay.osb.faro.rest.internal.dto.v1_0.converter.IndividualProfileDTOConverter)"
	)
	private DTOConverter
		<GetSiteIndividualProfileResponse.Individual, Individual>
			_individualProfileDTOConverter;

}