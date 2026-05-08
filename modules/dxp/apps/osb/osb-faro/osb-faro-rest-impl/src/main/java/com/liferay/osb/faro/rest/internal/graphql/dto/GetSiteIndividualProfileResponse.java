/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.graphql.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Jackson-bound shape of the <code>data</code> envelope returned by the
 * Cerebro engine for the <code>getSiteIndividualProfile</code> GraphQL
 * query (see <code>resources/graphql/getSiteIndividualProfile.graphql</code>).
 *
 * @author Leslie Wong
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSiteIndividualProfileResponse {

	public Individual getIndividual() {
		return _individual;
	}

	public void setIndividual(Individual individual) {
		_individual = individual;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Individual {

		public String getAccountName() {
			return _accountName;
		}

		public Long getActivitiesCount() {
			return _activitiesCount;
		}

		public Date getDateCreated() {
			return _dateCreated;
		}

		public Date getDateModified() {
			return _dateModified;
		}

		public Date getFirstActivityDate() {
			return _firstActivityDate;
		}

		public String getId() {
			return _id;
		}

		public Date getLastActivityDate() {
			return _lastActivityDate;
		}

		public String getLastSessionCountry() {
			return _lastSessionCountry;
		}

		public String getProfileType() {
			return _profileType;
		}

		public void setAccountName(String accountName) {
			_accountName = accountName;
		}

		public void setActivitiesCount(Long activitiesCount) {
			_activitiesCount = activitiesCount;
		}

		public void setDateCreated(Date dateCreated) {
			_dateCreated = dateCreated;
		}

		public void setDateModified(Date dateModified) {
			_dateModified = dateModified;
		}

		public void setFirstActivityDate(Date firstActivityDate) {
			_firstActivityDate = firstActivityDate;
		}

		public void setId(String id) {
			_id = id;
		}

		public void setLastActivityDate(Date lastActivityDate) {
			_lastActivityDate = lastActivityDate;
		}

		public void setLastSessionCountry(String lastSessionCountry) {
			_lastSessionCountry = lastSessionCountry;
		}

		public void setProfileType(String profileType) {
			_profileType = profileType;
		}

		private String _accountName;
		private Long _activitiesCount;
		private Date _dateCreated;
		private Date _dateModified;
		private Date _firstActivityDate;
		private String _id;
		private Date _lastActivityDate;
		private String _lastSessionCountry;
		private String _profileType;

	}

	private Individual _individual;

}