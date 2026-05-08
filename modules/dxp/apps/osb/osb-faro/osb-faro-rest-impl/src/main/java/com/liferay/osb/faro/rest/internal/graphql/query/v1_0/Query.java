/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.graphql.query.v1_0;

import com.liferay.osb.faro.rest.dto.v1_0.Account;
import com.liferay.osb.faro.rest.dto.v1_0.AssetSummaryMetric;
import com.liferay.osb.faro.rest.dto.v1_0.Channel;
import com.liferay.osb.faro.rest.dto.v1_0.Event;
import com.liferay.osb.faro.rest.dto.v1_0.FieldMapping;
import com.liferay.osb.faro.rest.dto.v1_0.Individual;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegment;
import com.liferay.osb.faro.rest.dto.v1_0.IndividualSegmentMembership;
import com.liferay.osb.faro.rest.dto.v1_0.PageMetric;
import com.liferay.osb.faro.rest.resource.v1_0.AccountResource;
import com.liferay.osb.faro.rest.resource.v1_0.AssetSummaryMetricResource;
import com.liferay.osb.faro.rest.resource.v1_0.ChannelResource;
import com.liferay.osb.faro.rest.resource.v1_0.EventResource;
import com.liferay.osb.faro.rest.resource.v1_0.FieldMappingResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualSegmentMembershipResource;
import com.liferay.osb.faro.rest.resource.v1_0.IndividualSegmentResource;
import com.liferay.osb.faro.rest.resource.v1_0.PageMetricResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import jakarta.annotation.Generated;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.constraints.NotEmpty;

import jakarta.ws.rs.core.UriInfo;

import java.util.Map;
import java.util.function.BiFunction;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
public class Query {

	public static void setAccountResourceComponentServiceObjects(
		ComponentServiceObjects<AccountResource>
			accountResourceComponentServiceObjects) {

		_accountResourceComponentServiceObjects =
			accountResourceComponentServiceObjects;
	}

	public static void setAssetSummaryMetricResourceComponentServiceObjects(
		ComponentServiceObjects<AssetSummaryMetricResource>
			assetSummaryMetricResourceComponentServiceObjects) {

		_assetSummaryMetricResourceComponentServiceObjects =
			assetSummaryMetricResourceComponentServiceObjects;
	}

	public static void setChannelResourceComponentServiceObjects(
		ComponentServiceObjects<ChannelResource>
			channelResourceComponentServiceObjects) {

		_channelResourceComponentServiceObjects =
			channelResourceComponentServiceObjects;
	}

	public static void setEventResourceComponentServiceObjects(
		ComponentServiceObjects<EventResource>
			eventResourceComponentServiceObjects) {

		_eventResourceComponentServiceObjects =
			eventResourceComponentServiceObjects;
	}

	public static void setFieldMappingResourceComponentServiceObjects(
		ComponentServiceObjects<FieldMappingResource>
			fieldMappingResourceComponentServiceObjects) {

		_fieldMappingResourceComponentServiceObjects =
			fieldMappingResourceComponentServiceObjects;
	}

	public static void setIndividualResourceComponentServiceObjects(
		ComponentServiceObjects<IndividualResource>
			individualResourceComponentServiceObjects) {

		_individualResourceComponentServiceObjects =
			individualResourceComponentServiceObjects;
	}

	public static void setIndividualSegmentResourceComponentServiceObjects(
		ComponentServiceObjects<IndividualSegmentResource>
			individualSegmentResourceComponentServiceObjects) {

		_individualSegmentResourceComponentServiceObjects =
			individualSegmentResourceComponentServiceObjects;
	}

	public static void
		setIndividualSegmentMembershipResourceComponentServiceObjects(
			ComponentServiceObjects<IndividualSegmentMembershipResource>
				individualSegmentMembershipResourceComponentServiceObjects) {

		_individualSegmentMembershipResourceComponentServiceObjects =
			individualSegmentMembershipResourceComponentServiceObjects;
	}

	public static void setPageMetricResourceComponentServiceObjects(
		ComponentServiceObjects<PageMetricResource>
			pageMetricResourceComponentServiceObjects) {

		_pageMetricResourceComponentServiceObjects =
			pageMetricResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {account(accountId: ___, siteKey: ___){accountName, annualRevenue, country, dateModified, id, industry, lastActivityDate, lifecycleStage}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a single Account from a site's FaroProject. Returns a single Account by id from a site's FaroProject. Use this when you already have an Account id from a previous call (e.g. `getSiteAccountsPage`). To search Accounts by name or filter, use `getSiteAccountsPage`."
	)
	public Account account(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("accountId") String accountId)
		throws Exception {

		return _applyComponentServiceObjects(
			_accountResourceComponentServiceObjects,
			this::_populateResourceContext,
			accountResource -> accountResource.getSiteAccount(
				Long.valueOf(siteKey), accountId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {accounts(channelId: ___, page: ___, pageSize: ___, search: ___, siteKey: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List or search Accounts known to a site's FaroProject. Returns a paginated list of Accounts (B2B companies) known to the Faro Contacts engine for a given site. Use this to browse or search Accounts by name. To fetch a single Account by id, use `getAccount`."
	)
	public AccountPage accounts(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_accountResourceComponentServiceObjects,
			this::_populateResourceContext,
			accountResource -> new AccountPage(
				accountResource.getSiteAccountsPage(
					Long.valueOf(siteKey), channelId, search,
					Pagination.of(page, pageSize),
					_sortsBiFunction.apply(accountResource, sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {assetSummaries(channelId: ___, page: ___, pageSize: ___, rangeEnd: ___, rangeKey: ___, rangeStart: ___, search: ___, siteKey: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List analytics asset summaries (page, blog, document, form, journal, object-entry) ranked by the requested sort metric, returning download/impression/read/view counts plus their period-over-period trend percentages. Use this to answer 'what content is performing best' and to pick assets for deeper drill-down via `getSitePagesPage` or asset-specific endpoints."
	)
	public AssetSummaryMetricPage assetSummaries(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("rangeEnd") String rangeEnd,
			@GraphQLName("rangeKey") Integer rangeKey,
			@GraphQLName("rangeStart") String rangeStart,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_assetSummaryMetricResourceComponentServiceObjects,
			this::_populateResourceContext,
			assetSummaryMetricResource -> new AssetSummaryMetricPage(
				assetSummaryMetricResource.getSiteAssetSummariesPage(
					Long.valueOf(siteKey), channelId, rangeEnd, rangeKey,
					rangeStart, search, Pagination.of(page, pageSize),
					_sortsBiFunction.apply(
						assetSummaryMetricResource, sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channel(channelId: ___, siteKey: ___){id, name}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a single Channel from a site's FaroProject. Returns a single Channel by id from a site's FaroProject. A Channel is a logical grouping of data sources (a website, a CRM connection) under which Faro contacts and activities are scoped."
	)
	public Channel channel(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId)
		throws Exception {

		return _applyComponentServiceObjects(
			_channelResourceComponentServiceObjects,
			this::_populateResourceContext,
			channelResource -> channelResource.getSiteChannel(
				Long.valueOf(siteKey), channelId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channels(page: ___, pageSize: ___, siteKey: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List Channels configured under a site's FaroProject. Returns a paginated list of Channels configured under a site's FaroProject. To fetch a single Channel by id, use `getChannel`."
	)
	public ChannelPage channels(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_channelResourceComponentServiceObjects,
			this::_populateResourceContext,
			channelResource -> new ChannelPage(
				channelResource.getSiteChannelsPage(
					Long.valueOf(siteKey), Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelEvents(channelId: ___, includeAnonymousUsers: ___, page: ___, pageSize: ___, rangeEnd: ___, rangeKey: ___, rangeStart: ___, search: ___, siteKey: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List tracked analytics events (page views, custom events, form submissions, etc.) for a specific channel, optionally narrowed to a single Individual or to a date range. Use this to inspect raw activity for a channel; for aggregated metrics across events, prefer `getSiteAssetSummariesPage` or `getSitePagesPage`."
	)
	public EventPage channelEvents(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("includeAnonymousUsers") Boolean includeAnonymousUsers,
			@GraphQLName("rangeEnd") String rangeEnd,
			@GraphQLName("rangeKey") Integer rangeKey,
			@GraphQLName("rangeStart") String rangeStart,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_eventResourceComponentServiceObjects,
			this::_populateResourceContext,
			eventResource -> new EventPage(
				eventResource.getSiteChannelEventsPage(
					Long.valueOf(siteKey), channelId, includeAnonymousUsers,
					rangeEnd, rangeKey, rangeStart, search,
					Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {fieldMapping(fieldMappingId: ___, siteKey: ___){context, dateModified, displayName, displayType, fieldName, fieldType, ownerType, repeatable}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a single FieldMapping from a site's FaroProject. Returns a single FieldMapping by id from a site's FaroProject. To list FieldMappings, use `getSiteFieldMappingsPage`."
	)
	public FieldMapping fieldMapping(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("fieldMappingId") String fieldMappingId)
		throws Exception {

		return _applyComponentServiceObjects(
			_fieldMappingResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldMappingResource -> fieldMappingResource.getSiteFieldMapping(
				Long.valueOf(siteKey), fieldMappingId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {fieldMappings(context: ___, ownerType: ___, page: ___, pageSize: ___, search: ___, siteKey: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List FieldMappings to discover custom attributes in a FaroProject. Returns a paginated list of FieldMappings for a site's FaroProject. Use this to discover which custom Individual or Account attributes exist in a given project (e.g. before constructing a `filter` expression that references a custom field)."
	)
	public FieldMappingPage fieldMappings(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("context") String context,
			@GraphQLName("ownerType") String ownerType,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_fieldMappingResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldMappingResource -> new FieldMappingPage(
				fieldMappingResource.getSiteFieldMappingsPage(
					Long.valueOf(siteKey), context, ownerType, search,
					Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individual(channelId: ___, individualId: ___, siteKey: ___){accountName, activitiesCount, dateCreated, dateModified, demographics, firstActivityDate, id, lastActivityDate, lastSessionCountry, profileType}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a single Individual from a site's FaroProject. Returns a single Individual by id from a site's FaroProject. Use this to fetch a contact's full profile once you have an id (from `getSiteIndividualsPage` or from segment memberships). To search Individuals by name, email, or other attributes, use `getSiteIndividualsPage`."
	)
	public Individual individual(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("individualId") String individualId,
			@GraphQLName("channelId") String channelId)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualResource -> individualResource.getSiteIndividual(
				Long.valueOf(siteKey), individualId, channelId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individualProfile(individualId: ___, siteKey: ___){accountName, activitiesCount, dateCreated, dateModified, demographics, firstActivityDate, id, lastActivityDate, lastSessionCountry, profileType}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a richer Individual profile via the Cerebro GraphQL engine. Returns a richer Individual profile fetched directly from the Cerebro engine via GraphQL. Use this when you need fields not available through the engine client's REST surface (e.g. computed insights, real-time activity metrics). For routine lookup by id, prefer `getSiteIndividual`."
	)
	public Individual individualProfile(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("individualId") String individualId)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualResource -> individualResource.getSiteIndividualProfile(
				Long.valueOf(siteKey), individualId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individuals(accountId: ___, channelId: ___, dataSourceId: ___, includeAnonymousUsers: ___, individualSegmentId: ___, interestName: ___, page: ___, pageSize: ___, search: ___, siteKey: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List or search Individuals (contacts) for a site's FaroProject. Returns a paginated list of Individuals (contacts) known to a site's FaroProject. Supports free-text search, OData-style filtering, and scoping by Account, Channel, DataSource, IndividualSegment, or interest. Use this for contact discovery, audience browsing, and 'find a person' queries. To fetch a single Individual by id (when you already have one), use `getIndividual` instead."
	)
	public IndividualPage individuals(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("accountId") String accountId,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("dataSourceId") String dataSourceId,
			@GraphQLName("includeAnonymousUsers") Boolean includeAnonymousUsers,
			@GraphQLName("individualSegmentId") String individualSegmentId,
			@GraphQLName("interestName") String interestName,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualResource -> new IndividualPage(
				individualResource.getSiteIndividualsPage(
					Long.valueOf(siteKey), accountId, channelId, dataSourceId,
					includeAnonymousUsers, individualSegmentId, interestName,
					search, Pagination.of(page, pageSize),
					_sortsBiFunction.apply(individualResource, sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individualSegment(individualSegmentId: ___, siteKey: ___){activeIndividualCount, anonymousIndividualCount, channelId, dateCreated, dateModified, filter, id, includeAnonymousUsers, individualCount, knownIndividualCount, lastActivityDate, name, segmentType, state, status}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Fetch a single IndividualSegment with its filter and counts. Returns a single IndividualSegment by id from a site's FaroProject, including the filter expression that defines membership and aggregate counts. To list segments, use `getSiteIndividualSegmentsPage`. To list members of a segment, use `getSiteIndividualSegmentMembershipsPage`."
	)
	public IndividualSegment individualSegment(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("individualSegmentId") String individualSegmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualSegmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualSegmentResource ->
				individualSegmentResource.getSiteIndividualSegment(
					Long.valueOf(siteKey), individualSegmentId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individualSegments(channelId: ___, name: ___, page: ___, pageSize: ___, search: ___, siteKey: ___, status: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List or search IndividualSegments under a site's FaroProject. Returns a paginated list of IndividualSegments (audiences) configured under a site's FaroProject. To fetch a single segment by id, use `getIndividualSegment`. To list the Individuals currently in a specific segment, use `getIndividualSegmentMembershipsPage`."
	)
	public IndividualSegmentPage individualSegments(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("name") String name,
			@GraphQLName("search") String search,
			@GraphQLName("status") String status,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualSegmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualSegmentResource -> new IndividualSegmentPage(
				individualSegmentResource.getSiteIndividualSegmentsPage(
					Long.valueOf(siteKey), channelId, name, search, status,
					Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {individualSegmentMemberships(individualSegmentId: ___, page: ___, pageSize: ___, siteKey: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List Individuals currently or formerly in a given segment. Returns a paginated list of memberships for an IndividualSegment. Each entry links one Individual to the segment and records when the Individual entered (and, if applicable, exited). Use the resulting `individualId` values with `getSiteIndividual` to retrieve the full Individual records."
	)
	public IndividualSegmentMembershipPage individualSegmentMemberships(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("individualSegmentId") String individualSegmentId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_individualSegmentMembershipResourceComponentServiceObjects,
			this::_populateResourceContext,
			individualSegmentMembershipResource ->
				new IndividualSegmentMembershipPage(
					individualSegmentMembershipResource.
						getSiteIndividualSegmentMembershipsPage(
							Long.valueOf(siteKey), individualSegmentId,
							Pagination.of(page, pageSize),
							_sortsBiFunction.apply(
								individualSegmentMembershipResource,
								sortsString))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {pages(channelId: ___, dataSourceId: ___, page: ___, pageSize: ___, rangeEnd: ___, rangeKey: ___, rangeStart: ___, search: ___, siteKey: ___, sorts: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "List analytics metrics for tracked pages on the site, ranked by views or another metric. Use this for 'top pages' style queries. Returns flattened view, visitor, bounce, exit, and access-path metrics for each page; for cross-asset comparisons that include non-pages, use `getSiteAssetSummariesPage`."
	)
	public PageMetricPage pages(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("channelId") String channelId,
			@GraphQLName("dataSourceId") String dataSourceId,
			@GraphQLName("rangeEnd") String rangeEnd,
			@GraphQLName("rangeKey") Integer rangeKey,
			@GraphQLName("rangeStart") String rangeStart,
			@GraphQLName("search") String search,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sort") String sortsString)
		throws Exception {

		return _applyComponentServiceObjects(
			_pageMetricResourceComponentServiceObjects,
			this::_populateResourceContext,
			pageMetricResource -> new PageMetricPage(
				pageMetricResource.getSitePagesPage(
					Long.valueOf(siteKey), channelId, dataSourceId, rangeEnd,
					rangeKey, rangeStart, search, Pagination.of(page, pageSize),
					_sortsBiFunction.apply(pageMetricResource, sortsString))));
	}

	@GraphQLName("AccountPage")
	public class AccountPage {

		public AccountPage(Page accountPage) {
			actions = accountPage.getActions();

			items = accountPage.getItems();
			lastPage = accountPage.getLastPage();
			page = accountPage.getPage();
			pageSize = accountPage.getPageSize();
			totalCount = accountPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<Account> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("AssetSummaryMetricPage")
	public class AssetSummaryMetricPage {

		public AssetSummaryMetricPage(Page assetSummaryMetricPage) {
			actions = assetSummaryMetricPage.getActions();

			items = assetSummaryMetricPage.getItems();
			lastPage = assetSummaryMetricPage.getLastPage();
			page = assetSummaryMetricPage.getPage();
			pageSize = assetSummaryMetricPage.getPageSize();
			totalCount = assetSummaryMetricPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<AssetSummaryMetric> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("ChannelPage")
	public class ChannelPage {

		public ChannelPage(Page channelPage) {
			actions = channelPage.getActions();

			items = channelPage.getItems();
			lastPage = channelPage.getLastPage();
			page = channelPage.getPage();
			pageSize = channelPage.getPageSize();
			totalCount = channelPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<Channel> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("EventPage")
	public class EventPage {

		public EventPage(Page eventPage) {
			actions = eventPage.getActions();

			items = eventPage.getItems();
			lastPage = eventPage.getLastPage();
			page = eventPage.getPage();
			pageSize = eventPage.getPageSize();
			totalCount = eventPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<Event> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("FieldMappingPage")
	public class FieldMappingPage {

		public FieldMappingPage(Page fieldMappingPage) {
			actions = fieldMappingPage.getActions();

			items = fieldMappingPage.getItems();
			lastPage = fieldMappingPage.getLastPage();
			page = fieldMappingPage.getPage();
			pageSize = fieldMappingPage.getPageSize();
			totalCount = fieldMappingPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<FieldMapping> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("IndividualPage")
	public class IndividualPage {

		public IndividualPage(Page individualPage) {
			actions = individualPage.getActions();

			items = individualPage.getItems();
			lastPage = individualPage.getLastPage();
			page = individualPage.getPage();
			pageSize = individualPage.getPageSize();
			totalCount = individualPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<Individual> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("IndividualSegmentPage")
	public class IndividualSegmentPage {

		public IndividualSegmentPage(Page individualSegmentPage) {
			actions = individualSegmentPage.getActions();

			items = individualSegmentPage.getItems();
			lastPage = individualSegmentPage.getLastPage();
			page = individualSegmentPage.getPage();
			pageSize = individualSegmentPage.getPageSize();
			totalCount = individualSegmentPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<IndividualSegment> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("IndividualSegmentMembershipPage")
	public class IndividualSegmentMembershipPage {

		public IndividualSegmentMembershipPage(
			Page individualSegmentMembershipPage) {

			actions = individualSegmentMembershipPage.getActions();

			items = individualSegmentMembershipPage.getItems();
			lastPage = individualSegmentMembershipPage.getLastPage();
			page = individualSegmentMembershipPage.getPage();
			pageSize = individualSegmentMembershipPage.getPageSize();
			totalCount = individualSegmentMembershipPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<IndividualSegmentMembership> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PageMetricPage")
	public class PageMetricPage {

		public PageMetricPage(Page pageMetricPage) {
			actions = pageMetricPage.getActions();

			items = pageMetricPage.getItems();
			lastPage = pageMetricPage.getLastPage();
			page = pageMetricPage.getPage();
			pageSize = pageMetricPage.getPageSize();
			totalCount = pageMetricPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map<String, String>> actions;

		@GraphQLField
		protected java.util.Collection<PageMetric> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(AccountResource accountResource)
		throws Exception {

		accountResource.setContextAcceptLanguage(_acceptLanguage);
		accountResource.setContextCompany(_company);
		accountResource.setContextHttpServletRequest(_httpServletRequest);
		accountResource.setContextHttpServletResponse(_httpServletResponse);
		accountResource.setContextUriInfo(_uriInfo);
		accountResource.setContextUser(_user);
		accountResource.setGroupLocalService(_groupLocalService);
		accountResource.setResourceActionLocalService(
			_resourceActionLocalService);
		accountResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		accountResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			AssetSummaryMetricResource assetSummaryMetricResource)
		throws Exception {

		assetSummaryMetricResource.setContextAcceptLanguage(_acceptLanguage);
		assetSummaryMetricResource.setContextCompany(_company);
		assetSummaryMetricResource.setContextHttpServletRequest(
			_httpServletRequest);
		assetSummaryMetricResource.setContextHttpServletResponse(
			_httpServletResponse);
		assetSummaryMetricResource.setContextUriInfo(_uriInfo);
		assetSummaryMetricResource.setContextUser(_user);
		assetSummaryMetricResource.setGroupLocalService(_groupLocalService);
		assetSummaryMetricResource.setResourceActionLocalService(
			_resourceActionLocalService);
		assetSummaryMetricResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		assetSummaryMetricResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(ChannelResource channelResource)
		throws Exception {

		channelResource.setContextAcceptLanguage(_acceptLanguage);
		channelResource.setContextCompany(_company);
		channelResource.setContextHttpServletRequest(_httpServletRequest);
		channelResource.setContextHttpServletResponse(_httpServletResponse);
		channelResource.setContextUriInfo(_uriInfo);
		channelResource.setContextUser(_user);
		channelResource.setGroupLocalService(_groupLocalService);
		channelResource.setResourceActionLocalService(
			_resourceActionLocalService);
		channelResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		channelResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(EventResource eventResource)
		throws Exception {

		eventResource.setContextAcceptLanguage(_acceptLanguage);
		eventResource.setContextCompany(_company);
		eventResource.setContextHttpServletRequest(_httpServletRequest);
		eventResource.setContextHttpServletResponse(_httpServletResponse);
		eventResource.setContextUriInfo(_uriInfo);
		eventResource.setContextUser(_user);
		eventResource.setGroupLocalService(_groupLocalService);
		eventResource.setResourceActionLocalService(
			_resourceActionLocalService);
		eventResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		eventResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			FieldMappingResource fieldMappingResource)
		throws Exception {

		fieldMappingResource.setContextAcceptLanguage(_acceptLanguage);
		fieldMappingResource.setContextCompany(_company);
		fieldMappingResource.setContextHttpServletRequest(_httpServletRequest);
		fieldMappingResource.setContextHttpServletResponse(
			_httpServletResponse);
		fieldMappingResource.setContextUriInfo(_uriInfo);
		fieldMappingResource.setContextUser(_user);
		fieldMappingResource.setGroupLocalService(_groupLocalService);
		fieldMappingResource.setResourceActionLocalService(
			_resourceActionLocalService);
		fieldMappingResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		fieldMappingResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(IndividualResource individualResource)
		throws Exception {

		individualResource.setContextAcceptLanguage(_acceptLanguage);
		individualResource.setContextCompany(_company);
		individualResource.setContextHttpServletRequest(_httpServletRequest);
		individualResource.setContextHttpServletResponse(_httpServletResponse);
		individualResource.setContextUriInfo(_uriInfo);
		individualResource.setContextUser(_user);
		individualResource.setGroupLocalService(_groupLocalService);
		individualResource.setResourceActionLocalService(
			_resourceActionLocalService);
		individualResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		individualResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			IndividualSegmentResource individualSegmentResource)
		throws Exception {

		individualSegmentResource.setContextAcceptLanguage(_acceptLanguage);
		individualSegmentResource.setContextCompany(_company);
		individualSegmentResource.setContextHttpServletRequest(
			_httpServletRequest);
		individualSegmentResource.setContextHttpServletResponse(
			_httpServletResponse);
		individualSegmentResource.setContextUriInfo(_uriInfo);
		individualSegmentResource.setContextUser(_user);
		individualSegmentResource.setGroupLocalService(_groupLocalService);
		individualSegmentResource.setResourceActionLocalService(
			_resourceActionLocalService);
		individualSegmentResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		individualSegmentResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			IndividualSegmentMembershipResource
				individualSegmentMembershipResource)
		throws Exception {

		individualSegmentMembershipResource.setContextAcceptLanguage(
			_acceptLanguage);
		individualSegmentMembershipResource.setContextCompany(_company);
		individualSegmentMembershipResource.setContextHttpServletRequest(
			_httpServletRequest);
		individualSegmentMembershipResource.setContextHttpServletResponse(
			_httpServletResponse);
		individualSegmentMembershipResource.setContextUriInfo(_uriInfo);
		individualSegmentMembershipResource.setContextUser(_user);
		individualSegmentMembershipResource.setGroupLocalService(
			_groupLocalService);
		individualSegmentMembershipResource.setResourceActionLocalService(
			_resourceActionLocalService);
		individualSegmentMembershipResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		individualSegmentMembershipResource.setRoleLocalService(
			_roleLocalService);
	}

	private void _populateResourceContext(PageMetricResource pageMetricResource)
		throws Exception {

		pageMetricResource.setContextAcceptLanguage(_acceptLanguage);
		pageMetricResource.setContextCompany(_company);
		pageMetricResource.setContextHttpServletRequest(_httpServletRequest);
		pageMetricResource.setContextHttpServletResponse(_httpServletResponse);
		pageMetricResource.setContextUriInfo(_uriInfo);
		pageMetricResource.setContextUser(_user);
		pageMetricResource.setGroupLocalService(_groupLocalService);
		pageMetricResource.setResourceActionLocalService(
			_resourceActionLocalService);
		pageMetricResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		pageMetricResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<AccountResource>
		_accountResourceComponentServiceObjects;
	private static ComponentServiceObjects<AssetSummaryMetricResource>
		_assetSummaryMetricResourceComponentServiceObjects;
	private static ComponentServiceObjects<ChannelResource>
		_channelResourceComponentServiceObjects;
	private static ComponentServiceObjects<EventResource>
		_eventResourceComponentServiceObjects;
	private static ComponentServiceObjects<FieldMappingResource>
		_fieldMappingResourceComponentServiceObjects;
	private static ComponentServiceObjects<IndividualResource>
		_individualResourceComponentServiceObjects;
	private static ComponentServiceObjects<IndividualSegmentResource>
		_individualSegmentResourceComponentServiceObjects;
	private static ComponentServiceObjects<IndividualSegmentMembershipResource>
		_individualSegmentMembershipResourceComponentServiceObjects;
	private static ComponentServiceObjects<PageMetricResource>
		_pageMetricResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction
		<Object, String, com.liferay.portal.kernel.search.filter.Filter>
			_filterBiFunction;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private ResourceActionLocalService _resourceActionLocalService;
	private ResourcePermissionLocalService _resourcePermissionLocalService;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, com.liferay.portal.kernel.search.Sort[]>
		_sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}
// LIFERAY-REST-BUILDER-HASH:-277069846