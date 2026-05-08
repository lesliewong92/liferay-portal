/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.graphql.servlet.v1_0;

import com.liferay.osb.faro.rest.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.osb.faro.rest.internal.graphql.query.v1_0.Query;
import com.liferay.osb.faro.rest.internal.resource.v1_0.AccountResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.AssetSummaryMetricResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.ChannelResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.EventResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.FieldMappingResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.IndividualResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.IndividualSegmentMembershipResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.IndividualSegmentResourceImpl;
import com.liferay.osb.faro.rest.internal.resource.v1_0.PageMetricResourceImpl;
import com.liferay.osb.faro.rest.resource.v1_0.AccountResource;
import com.liferay.osb.faro.rest.resource.v1_0.AssetSummaryMetricResource;
import com.liferay.osb.faro.rest.resource.v1_0.ChannelResource;
import com.liferay.osb.faro.rest.resource.v1_0.EventResource;
import com.liferay.osb.faro.rest.resource.v1_0.FieldMappingResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualSegmentMembershipResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualSegmentResource;
import com.liferay.osb.faro.rest.resource.v1_0.PageMetricResource;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import jakarta.annotation.Generated;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Leslie Wong
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Query.setAccountResourceComponentServiceObjects(
			_accountResourceComponentServiceObjects);
		Query.setAssetSummaryMetricResourceComponentServiceObjects(
			_assetSummaryMetricResourceComponentServiceObjects);
		Query.setChannelResourceComponentServiceObjects(
			_channelResourceComponentServiceObjects);
		Query.setEventResourceComponentServiceObjects(
			_eventResourceComponentServiceObjects);
		Query.setFieldMappingResourceComponentServiceObjects(
			_fieldMappingResourceComponentServiceObjects);
		Query.setIndividualResourceComponentServiceObjects(
			_individualResourceComponentServiceObjects);
		Query.setIndividualSegmentResourceComponentServiceObjects(
			_individualSegmentResourceComponentServiceObjects);
		Query.setIndividualSegmentMembershipResourceComponentServiceObjects(
			_individualSegmentMembershipResourceComponentServiceObjects);
		Query.setPageMetricResourceComponentServiceObjects(
			_pageMetricResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Faro.Rest";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/faro-rest-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"query#account",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "getSiteAccount"));
					put(
						"query#accounts",
						new ObjectValuePair<>(
							AccountResourceImpl.class, "getSiteAccountsPage"));
					put(
						"query#assetSummaries",
						new ObjectValuePair<>(
							AssetSummaryMetricResourceImpl.class,
							"getSiteAssetSummariesPage"));
					put(
						"query#channel",
						new ObjectValuePair<>(
							ChannelResourceImpl.class, "getSiteChannel"));
					put(
						"query#channels",
						new ObjectValuePair<>(
							ChannelResourceImpl.class, "getSiteChannelsPage"));
					put(
						"query#channelEvents",
						new ObjectValuePair<>(
							EventResourceImpl.class,
							"getSiteChannelEventsPage"));
					put(
						"query#fieldMapping",
						new ObjectValuePair<>(
							FieldMappingResourceImpl.class,
							"getSiteFieldMapping"));
					put(
						"query#fieldMappings",
						new ObjectValuePair<>(
							FieldMappingResourceImpl.class,
							"getSiteFieldMappingsPage"));
					put(
						"query#individual",
						new ObjectValuePair<>(
							IndividualResourceImpl.class, "getSiteIndividual"));
					put(
						"query#individualProfile",
						new ObjectValuePair<>(
							IndividualResourceImpl.class,
							"getSiteIndividualProfile"));
					put(
						"query#individuals",
						new ObjectValuePair<>(
							IndividualResourceImpl.class,
							"getSiteIndividualsPage"));
					put(
						"query#individualSegment",
						new ObjectValuePair<>(
							IndividualSegmentResourceImpl.class,
							"getSiteIndividualSegment"));
					put(
						"query#individualSegments",
						new ObjectValuePair<>(
							IndividualSegmentResourceImpl.class,
							"getSiteIndividualSegmentsPage"));
					put(
						"query#individualSegmentMemberships",
						new ObjectValuePair<>(
							IndividualSegmentMembershipResourceImpl.class,
							"getSiteIndividualSegmentMembershipsPage"));
					put(
						"query#pages",
						new ObjectValuePair<>(
							PageMetricResourceImpl.class, "getSitePagesPage"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AccountResource>
		_accountResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<AssetSummaryMetricResource>
		_assetSummaryMetricResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ChannelResource>
		_channelResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<EventResource>
		_eventResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<FieldMappingResource>
		_fieldMappingResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<IndividualResource>
		_individualResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<IndividualSegmentResource>
		_individualSegmentResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<IndividualSegmentMembershipResource>
		_individualSegmentMembershipResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<PageMetricResource>
		_pageMetricResourceComponentServiceObjects;

}
// LIFERAY-REST-BUILDER-HASH:-1611052012