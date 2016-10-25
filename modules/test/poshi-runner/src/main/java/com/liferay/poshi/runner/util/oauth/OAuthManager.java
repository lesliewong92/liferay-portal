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

import org.scribe.builder.api.Api;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * @author Leslie Wong
 */
public class OAuthManager {

	public OAuthManager(
		String accessTokenEndpoint, String accessTokenSecret,
		String accessTokenString, String apiKey, String apiSecret,
		String authorizationURL, String requestTokenEndpoint) {

		Api api = new OAuth10aAPIImpl(
			accessTokenEndpoint, authorizationURL, requestTokenEndpoint);

		OAuthConfig oAuthConfig = new OAuthConfig(
			apiKey, apiSecret, null, SignatureType.Header, null, null);

		_oAuthService = api.createService(oAuthConfig);

		_token = new Token(accessTokenString, accessTokenSecret);
	}

	public OAuthService getOAuthService() {
		return _oAuthService;
	}

	public void signRequest(OAuthRequest oAuthRequest) {
		_oAuthService.signRequest(_token, oAuthRequest);
	}

	private OAuthService _oAuthService;
	private Token _token;

}