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
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * @author Leslie Wong
 */
public class OAuthUtil {

	public static Response createRequest(
			String protocolVersion, String requestURL. String... arguments)
		throws Exception {

		OAuthUtil oAuthUtil = getOAuthUtil(protocolVersion, arguments);

		OAuthService oAuthService = oAuthUtil.getOAuthService();

		OAuthRequest oAuthRequest = new OAuthRequest(
			Verb.GET, requestURL, oAuthService);

		oAuthService.signRequest(oAuthUtil.getOAuthAccessToken(), oAuthRequest);

		Response response = oAuthRequest.send();

		if (!response.isSuccessful()) {
			throw new Exception("Request failed");
		}

		return response;
	}

	// Rename
	public static OAuthUtil getOAuthSet(
			String protocolVersion, String... arguments)
		throws Exception {

		if (protocolVersion.equals("1.0a")) {
			String accessTokenEndpoint = arguments[0];
			String accessTokenString = arguments[1];
			String accessTokenSecret = arguments[2];
			String apiKey = arguments[3];
			String apiSecret = arguments[4];
			String authorizationURL = arguments[5];
			String requestTokenEndpoint = arguments[6];

			return new OAuthSet(
				accessTokenEndpoint, accessTokenString, accessTokenSecret,
				apiKey, apiSecret, authorizationURL, requestTokenEndpoint);
		}
		else if (protocolVersion.equals("2.0")) {
			String accessTokenEndpoint = arguments[0];
			String accessTokenString = arguments[1];
			String apiKey = arguments[2];
			String apiSecret = arguments[3];
			String authorizationBaseURL = arguments[4];
			String callbackURL = arguments[5];

			return new OAuthSet(
				accessTokenEndpoint, accessTokenString, apiKey, apiSecret,
				authorizationBaseURL, callbackURL);
		}

		throw new Exception("Invalid OAuth protocol version");
	}

}