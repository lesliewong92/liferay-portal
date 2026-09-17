/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.resource.v1_0;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.IndividualSegmentMembershipChangeAggregation;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegmentMembershipChangeMetric;
import com.liferay.osb.faro.rest.internal.dto.v1_0.util.IndividualSegmentMembershipChangeMetricUtil;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualSegmentMembershipChangeMetricResource;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Leslie Wong
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/individual-segment-membership-change-metric.properties",
	scope = ServiceScope.PROTOTYPE,
	service = IndividualSegmentMembershipChangeMetricResource.class
)
public class IndividualSegmentMembershipChangeMetricResourceImpl
	extends BaseIndividualSegmentMembershipChangeMetricResourceImpl {

	@Override
	public IndividualSegmentMembershipChangeMetric
			getWorkspaceGroupIndividualSegmentMembershipChangeMetric(
				Long groupId, String individualSegmentId, String interval,
				String rangeEnd, String rangeKey, String rangeStart)
		throws Exception {

		String resolvedRangeKey = _getRangeKey(rangeEnd, rangeKey, rangeStart);

		LocalDate endLocalDate = _toLocalDate(
			DateRangeUtil.getEndDate(resolvedRangeKey, rangeEnd));
		LocalDate startLocalDate = _toLocalDate(
			DateRangeUtil.getStartDate(resolvedRangeKey, rangeStart));

		long days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;

		// Ask for the selected range and the range of the same length
		// immediately before it, so every metric has something to compare
		// against, then split the buckets back apart on the selected start

		Results<IndividualSegmentMembershipChangeAggregation> results =
			_contactsEngineClient.
				getIndividualSegmentMembershipChangeAggregations(
					_faroProjectLocalService.getFaroProjectByGroupId(groupId),
					individualSegmentId, _getInterval(interval),
					(int)(2 * days), String.valueOf(endLocalDate), null,
					String.valueOf(startLocalDate.minusDays(days)));

		return IndividualSegmentMembershipChangeMetricUtil.
			toIndividualSegmentMembershipChangeMetric(
				results.getItems(),
				Date.from(
					startLocalDate.atStartOfDay(
						ZoneOffset.UTC
					).toInstant()));
	}

	private String _getInterval(String interval) {
		if (Validator.isNull(interval)) {
			return _INTERVAL_DAY;
		}

		return StringUtil.toLowerCase(interval);
	}

	private String _getRangeKey(
		String rangeEnd, String rangeKey, String rangeStart) {

		if (Validator.isNotNull(rangeKey)) {
			return rangeKey;
		}

		if (Validator.isNotNull(rangeEnd) && Validator.isNotNull(rangeStart)) {
			return null;
		}

		return TimeRange.LAST_30_DAYS.name();
	}

	private LocalDate _toLocalDate(Date date) {
		Instant instant = date.toInstant();

		ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

		return zonedDateTime.toLocalDate();
	}

	private static final String _INTERVAL_DAY = "day";

	@Reference
	private ContactsEngineClient _contactsEngineClient;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

}