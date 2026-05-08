/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.util;

import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.rest.dto.v1_0.Account;
import com.liferay.osb.faro.rest.dto.v1_0.AssetSummaryMetric;
import com.liferay.osb.faro.rest.dto.v1_0.Channel;
import com.liferay.osb.faro.rest.dto.v1_0.Event;
import com.liferay.osb.faro.rest.dto.v1_0.FieldMapping;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualDemographicField;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegment;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegmentMembership;
import com.liferay.osb.faro.rest.dto.v1_0.PageMetric;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteAssetSummariesPageResponse;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSiteChannelEventsPageResponse;
import com.liferay.osb.faro.rest.internal.graphql.dto.GetSitePagesPageResponse;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leslie Wong
 */
public class FaroDTOUtil {

	public static Account toAccount(
		com.liferay.osb.faro.engine.client.model.Account engineAccount) {

		if (engineAccount == null) {
			return null;
		}

		Account account = new Account();

		account.setAccountName(engineAccount::getAccountName);
		account.setAnnualRevenue(engineAccount::getAnnualRevenue);
		account.setCountry(engineAccount::getCountry);
		account.setDateModified(engineAccount::getModifiedDate);
		account.setId(engineAccount::getId);
		account.setIndustry(engineAccount::getIndustry);
		account.setLastActivityDate(engineAccount::getLastActivityDate);
		account.setLifecycleStage(engineAccount::getLifecycleStage);

		return account;
	}

	public static AssetSummaryMetric toAssetSummaryMetric(
		GetSiteAssetSummariesPageResponse.AssetSummaryMetric engineMetric) {

		if (engineMetric == null) {
			return null;
		}

		AssetSummaryMetric assetSummaryMetric = new AssetSummaryMetric();

		assetSummaryMetric.setAssetId(engineMetric::getAssetId);
		assetSummaryMetric.setAssetTitle(engineMetric::getAssetTitle);
		assetSummaryMetric.setAssetType(engineMetric::getAssetType);
		assetSummaryMetric.setDownloads(
			() -> _value(engineMetric.getDownloadsMetric()));
		assetSummaryMetric.setDownloadsTrendPercentage(
			() -> _trendPercentage(engineMetric.getDownloadsMetric()));
		assetSummaryMetric.setImpressions(
			() -> _value(engineMetric.getImpressionsMetric()));
		assetSummaryMetric.setImpressionsTrendPercentage(
			() -> _trendPercentage(engineMetric.getImpressionsMetric()));
		assetSummaryMetric.setReads(
			() -> _value(engineMetric.getReadsMetric()));
		assetSummaryMetric.setReadsTrendPercentage(
			() -> _trendPercentage(engineMetric.getReadsMetric()));
		assetSummaryMetric.setViews(
			() -> _value(engineMetric.getViewsMetric()));
		assetSummaryMetric.setViewsTrendPercentage(
			() -> _trendPercentage(engineMetric.getViewsMetric()));

		return assetSummaryMetric;
	}

	public static Channel toChannel(
		com.liferay.osb.faro.engine.client.model.Channel engineChannel) {

		if (engineChannel == null) {
			return null;
		}

		Channel channel = new Channel();

		channel.setId(engineChannel::getId);
		channel.setName(engineChannel::getName);

		return channel;
	}

	public static Event toEvent(
		GetSiteChannelEventsPageResponse.Event engineEvent) {

		if (engineEvent == null) {
			return null;
		}

		Event event = new Event();

		event.setApplicationId(engineEvent::getApplicationId);
		event.setAssetTitle(engineEvent::getAssetTitle);
		event.setAttributes(
			() -> _propertiesToMap(engineEvent.getProperties()));
		event.setCanonicalUrl(engineEvent::getCanonicalUrl);
		event.setCreateDate(engineEvent::getCreateDate);
		event.setEmailAddressHashed(engineEvent::getEmailAddressHashed);
		event.setName(engineEvent::getName);
		event.setPageDescription(engineEvent::getPageDescription);
		event.setPageKeywords(engineEvent::getPageKeywords);
		event.setPageTitle(engineEvent::getPageTitle);
		event.setReferrer(engineEvent::getReferrer);
		event.setUrl(engineEvent::getUrl);

		return event;
	}

	public static FieldMapping toFieldMapping(
		com.liferay.osb.faro.engine.client.model.FieldMapping
			engineFieldMapping) {

		if (engineFieldMapping == null) {
			return null;
		}

		FieldMapping fieldMapping = new FieldMapping();

		fieldMapping.setContext(engineFieldMapping::getContext);
		fieldMapping.setDateModified(engineFieldMapping::getDateModified);
		fieldMapping.setDisplayName(engineFieldMapping::getDisplayName);
		fieldMapping.setDisplayType(engineFieldMapping::getDisplayType);
		fieldMapping.setFieldName(engineFieldMapping::getFieldName);
		fieldMapping.setFieldType(engineFieldMapping::getFieldType);
		fieldMapping.setOwnerType(engineFieldMapping::getOwnerType);
		fieldMapping.setRepeatable(engineFieldMapping::getRepeatable);

		return fieldMapping;
	}

	public static Individual toIndividual(
		com.liferay.osb.faro.engine.client.model.Individual engineIndividual) {

		if (engineIndividual == null) {
			return null;
		}

		Individual individual = new Individual();

		individual.setAccountName(engineIndividual::getAccountName);
		individual.setActivitiesCount(engineIndividual::getActivitiesCount);
		individual.setDateCreated(engineIndividual::getDateCreated);
		individual.setDateModified(engineIndividual::getDateModified);
		individual.setDemographics(
			() -> _toDemographics(engineIndividual.getDemographics()));
		individual.setFirstActivityDate(engineIndividual::getFirstActivityDate);
		individual.setId(engineIndividual::getId);
		individual.setLastActivityDate(engineIndividual::getLastActivityDate);
		individual.setLastSessionCountry(
			engineIndividual::getLastSessionCountry);
		individual.setProfileType(engineIndividual::getProfileType);

		return individual;
	}

	public static IndividualSegment toIndividualSegment(
		com.liferay.osb.faro.engine.client.model.IndividualSegment
			engineIndividualSegment) {

		if (engineIndividualSegment == null) {
			return null;
		}

		IndividualSegment individualSegment = new IndividualSegment();

		individualSegment.setActiveIndividualCount(
			engineIndividualSegment::getActiveIndividualCount);
		individualSegment.setAnonymousIndividualCount(
			engineIndividualSegment::getAnonymousIndividualCount);
		individualSegment.setChannelId(engineIndividualSegment::getChannelId);
		individualSegment.setDateCreated(
			engineIndividualSegment::getDateCreated);
		individualSegment.setDateModified(
			engineIndividualSegment::getDateModified);
		individualSegment.setFilter(engineIndividualSegment::getFilterString);
		individualSegment.setId(engineIndividualSegment::getId);
		individualSegment.setIncludeAnonymousUsers(
			engineIndividualSegment::isIncludeAnonymousUsers);
		individualSegment.setIndividualCount(
			engineIndividualSegment::getIndividualCount);
		individualSegment.setKnownIndividualCount(
			engineIndividualSegment::getKnownIndividualCount);
		individualSegment.setLastActivityDate(
			engineIndividualSegment::getLastActivityDate);
		individualSegment.setName(engineIndividualSegment::getName);
		individualSegment.setSegmentType(
			engineIndividualSegment::getSegmentType);
		individualSegment.setState(engineIndividualSegment::getState);
		individualSegment.setStatus(engineIndividualSegment::getStatus);

		return individualSegment;
	}

	public static IndividualSegmentMembership toIndividualSegmentMembership(
		com.liferay.osb.faro.engine.client.model.IndividualSegmentMembership
			engineIndividualSegmentMembership) {

		if (engineIndividualSegmentMembership == null) {
			return null;
		}

		IndividualSegmentMembership individualSegmentMembership =
			new IndividualSegmentMembership();

		individualSegmentMembership.setDateCreated(
			engineIndividualSegmentMembership::getDateCreated);
		individualSegmentMembership.setDateRemoved(
			engineIndividualSegmentMembership::getDateRemoved);
		individualSegmentMembership.setIndividualId(
			engineIndividualSegmentMembership::getIndividualId);
		individualSegmentMembership.setIndividualSegmentId(
			engineIndividualSegmentMembership::getIndividualSegmentId);
		individualSegmentMembership.setStatus(
			engineIndividualSegmentMembership::getStatus);

		return individualSegmentMembership;
	}

	public static PageMetric toPageMetric(
		GetSitePagesPageResponse.PageMetric enginePageMetric) {

		if (enginePageMetric == null) {
			return null;
		}

		PageMetric pageMetric = new PageMetric();

		pageMetric.setAssetId(enginePageMetric::getAssetId);
		pageMetric.setAssetTitle(enginePageMetric::getAssetTitle);
		pageMetric.setAssetType(enginePageMetric::getAssetType);
		pageMetric.setAvgTimeOnPage(
			() -> _value(enginePageMetric.getAvgTimeOnPageMetric()));
		pageMetric.setBounceRate(
			() -> _value(enginePageMetric.getBounceRateMetric()));
		pageMetric.setDataSourceId(enginePageMetric::getDataSourceId);
		pageMetric.setDirectAccess(
			() -> _value(enginePageMetric.getDirectAccessMetric()));
		pageMetric.setEntrances(
			() -> _value(enginePageMetric.getEntrancesMetric()));
		pageMetric.setExitRate(
			() -> _value(enginePageMetric.getExitRateMetric()));
		pageMetric.setIndirectAccess(
			() -> _value(enginePageMetric.getIndirectAccessMetric()));
		pageMetric.setUrls(() -> _toUrlsArray(enginePageMetric.getUrls()));
		pageMetric.setViews(() -> _value(enginePageMetric.getViewsMetric()));
		pageMetric.setViewsTrendPercentage(
			() -> _trendPercentage(enginePageMetric.getViewsMetric()));
		pageMetric.setVisitors(
			() -> _value(enginePageMetric.getVisitorsMetric()));
		pageMetric.setVisitorsTrendPercentage(
			() -> _trendPercentage(enginePageMetric.getVisitorsMetric()));

		return pageMetric;
	}

	private static Map<String, String> _propertiesToMap(
		List<GetSiteChannelEventsPageResponse.Property> properties) {

		if (ListUtil.isEmpty(properties)) {
			return null;
		}

		Map<String, String> attributes = new LinkedHashMap<>(properties.size());

		for (GetSiteChannelEventsPageResponse.Property property : properties) {
			String name = property.getName();

			if (name != null) {
				attributes.put(name, property.getValue());
			}
		}

		return attributes;
	}

	private static Map<String, List<IndividualDemographicField>>
		_toDemographics(Map<String, List<Field>> engineDemographics) {

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
					_toIndividualDemographicField(engineField));
			}

			demographics.put(entry.getKey(), individualDemographicFields);
		}

		return demographics;
	}

	private static IndividualDemographicField _toIndividualDemographicField(
		Field engineField) {

		if (engineField == null) {
			return null;
		}

		IndividualDemographicField individualDemographicField =
			new IndividualDemographicField();

		individualDemographicField.setFieldType(engineField::getFieldType);
		individualDemographicField.setLabel(engineField::getLabel);
		individualDemographicField.setName(engineField::getName);
		individualDemographicField.setValue(engineField::getValue);

		return individualDemographicField;
	}

	private static String[] _toUrlsArray(List<String> urls) {
		if (ListUtil.isEmpty(urls)) {
			return null;
		}

		return urls.toArray(new String[0]);
	}

	private static Double _trendPercentage(
		GetSiteAssetSummariesPageResponse.Metric metric) {

		if ((metric == null) || (metric.getTrend() == null)) {
			return null;
		}

		return metric.getTrend(
		).getPercentage();
	}

	private static Double _trendPercentage(
		GetSitePagesPageResponse.Metric metric) {

		if ((metric == null) || (metric.getTrend() == null)) {
			return null;
		}

		return metric.getTrend(
		).getPercentage();
	}

	private static Double _value(
		GetSiteAssetSummariesPageResponse.Metric metric) {

		if (metric == null) {
			return null;
		}

		return metric.getValue();
	}

	private static Double _value(GetSitePagesPageResponse.Metric metric) {
		if (metric == null) {
			return null;
		}

		return metric.getValue();
	}

}