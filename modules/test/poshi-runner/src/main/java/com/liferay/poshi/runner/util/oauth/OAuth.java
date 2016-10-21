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
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;

/**
 * @author Leslie Wong
 */
public class OAuth {

	public OAuth(
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

	public OAuth(
		String accessTokenEndpoint, String accessTokenString,
		String accessTokenSecret, String apiKey, String apiSecret,
		String authorizationURL, String requestTokenEndpoint) {

		ServiceBuilder serviceBuilder = new ServiceBuilder();

		serviceBuilder.apiKey(apiKey);
		serviceBuilder.apiSecret(apiSecret);

		_oAuthService = serviceBuilder.build(
			new OAuth10aAPIImpl(
				accessTokenEndpoint, authorizationURL, requestTokenEndpoint));

		_oAuthAccessToken =
			new OAuth1AccessToken(accessTokenString, accessTokenSecret);
	}

	public static OAuthService getOAuthService() {
		return _getOAuthService;
	}

	public OAuthAccessToken getOAuthAccessToken() {
		return _oAuthRequest;
	}

	private static OAuthService _oAuthService;
	private static Token _oAuthAccessToken;

}