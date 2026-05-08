/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.resource.v1_0;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.internal.dto.v1_0.util.FaroDTOUtil;
import com.liferay.osb.faro.rest.internal.dto.v1_0.util.FaroPaginationUtil;
import com.liferay.osb.faro.rest.internal.graphql.client.FaroGraphQLClient;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteIndividualProfileResponse;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualResource;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.search.Sort;
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

		return FaroDTOUtil.toIndividual(
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

		return _toIndividual(response.getIndividual());
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

		return FaroPaginationUtil.toPage(
			_contactsEngineClient.getIndividuals(
				faroProject, accountId, channelId, dataSourceId,
				individualSegmentId, null, interestName, null, null, search,
				null, (includeAnonymousUsers != null) && includeAnonymousUsers,
				FaroPaginationUtil.getCur(pagination),
				FaroPaginationUtil.getDelta(pagination),
				FaroPaginationUtil.toOrderByFields(sorts)),
			pagination, FaroDTOUtil::toIndividual);
	}

	private Individual _toIndividual(
		GetSiteIndividualProfileResponse.Individual graphQLIndividual) {

		if (graphQLIndividual == null) {
			return null;
		}

		Individual individual = new Individual();

		individual.setAccountName(graphQLIndividual::getAccountName);
		individual.setActivitiesCount(graphQLIndividual::getActivitiesCount);
		individual.setDateCreated(graphQLIndividual::getDateCreated);
		individual.setDateModified(graphQLIndividual::getDateModified);
		individual.setFirstActivityDate(
			graphQLIndividual::getFirstActivityDate);
		individual.setId(graphQLIndividual::getId);
		individual.setLastActivityDate(graphQLIndividual::getLastActivityDate);
		individual.setLastSessionCountry(
			graphQLIndividual::getLastSessionCountry);
		individual.setProfileType(graphQLIndividual::getProfileType);

		return individual;
	}

	@Reference
	private ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroGraphQLClient _faroGraphQLClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

}