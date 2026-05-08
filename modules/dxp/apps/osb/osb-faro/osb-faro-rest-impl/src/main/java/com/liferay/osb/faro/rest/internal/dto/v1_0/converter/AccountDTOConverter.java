/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.rest.dto.v1_0.Account;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leslie Wong
 */
@Component(
	property = "dto.class.name=com.liferay.osb.faro.engine.client.model.Account",
	service = DTOConverter.class
)
public class AccountDTOConverter
	implements DTOConverter
		<com.liferay.osb.faro.engine.client.model.Account, Account> {

	@Override
	public String getContentType() {
		return Account.class.getSimpleName();
	}

	@Override
	public Account toDTO(
		DTOConverterContext dtoConverterContext,
		com.liferay.osb.faro.engine.client.model.Account engineAccount) {

		if (engineAccount == null) {
			return null;
		}

		return new Account() {
			{
				setAccountName(engineAccount::getAccountName);
				setAnnualRevenue(engineAccount::getAnnualRevenue);
				setCountry(engineAccount::getCountry);
				setDateModified(engineAccount::getModifiedDate);
				setId(engineAccount::getId);
				setIndustry(engineAccount::getIndustry);
				setLastActivityDate(engineAccount::getLastActivityDate);
				setLifecycleStage(engineAccount::getLifecycleStage);
			}
		};
	}

}