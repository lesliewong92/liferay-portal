/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.engine.client.model;

import java.util.Date;

/**
 * @author Leslie Wong
 */
public class AccountLifecycleStageTransition {

	public String getAccountId() {
		return _accountId;
	}

	public String getAccountName() {
		return _accountName;
	}

	public AccountLifecycleStage getFromStage() {
		return _fromStage;
	}

	public AccountLifecycleStage getToStage() {
		return _toStage;
	}

	public Date getTransitionDate() {
		if (_transitionDate == null) {
			return null;
		}

		return new Date(_transitionDate.getTime());
	}

	public void setAccountId(String accountId) {
		_accountId = accountId;
	}

	public void setAccountName(String accountName) {
		_accountName = accountName;
	}

	public void setFromStage(AccountLifecycleStage fromStage) {
		_fromStage = fromStage;
	}

	public void setToStage(AccountLifecycleStage toStage) {
		_toStage = toStage;
	}

	public void setTransitionDate(Date transitionDate) {
		if (transitionDate != null) {
			_transitionDate = new Date(transitionDate.getTime());
		}
	}

	private String _accountId;
	private String _accountName;
	private AccountLifecycleStage _fromStage;
	private AccountLifecycleStage _toStage;
	private Date _transitionDate;

}