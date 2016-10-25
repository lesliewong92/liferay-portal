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

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * @author Leslie Wong
 */
public class OAuth20Manager implements OAuthManager {

	public OAuth20Manager(
		String accessTokenEndpoint, String accessTokenString, String apiKey,
		String apiSecret, String authorizationBaseURL, String callbackURL) {

		ServiceBuilder serviceBuilder = new ServiceBuilder();

		serviceBuilder.apiKey(apiKey);
		serviceBuilder.apiSecret(apiSecret);
		serviceBuilder.callback(callbackURL);

		_oAuthService = serviceBuilder.build(
			new OAuth20APIImpl(accessTokenEndpoint, authorizationBaseURL));

		_oAuthAccessToken = new OAuth2AccessToken(accessTokenString);
	}

	@Override
	public OAuthRequest getOAuthRequest(String requestURL) {
		OAuthRequest oAuthRequest = new OAuthRequest(
			Verb.GET, requestURL, _oAuthService);

		_oAuthService.signRequest(_oAuthAccessToken, oAuthRequest);

		return oAuthRequest;
	}

	private OAuth20Service _oAuthService;
	private OAuth2AccessToken _oAuthAccessToken;

}