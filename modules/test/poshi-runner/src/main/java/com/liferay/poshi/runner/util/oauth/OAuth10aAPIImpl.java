/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner.util.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * @author Leslie Wong
 */
public class OAuth10aAPIImpl extends DefaultApi10a {

	public OAuth10aAPIImpl(
		String accessTokenEndpoint, String authorizationURL,
		String requestTokenEndpoint) {

		_accessTokenEndpoint = accessTokenEndpoint;
		_authorizationURL = authorizationURL;
		_requestTokenEndpoint = requestTokenEndpoint;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return _accessTokenEndpoint;
	}

	@Override
	public String getAuthorizationUrl(Token token) {
		return _authorizationURL.replace("{0}", token.getToken());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return _requestTokenEndpoint;
	}

	private final String _accessTokenEndpoint;
	private final String _authorizationURL;
	private final String _requestTokenEndpoint;

}