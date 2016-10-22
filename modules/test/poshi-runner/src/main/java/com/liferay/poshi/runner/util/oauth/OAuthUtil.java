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

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;

/**
 * @author Leslie Wong
 */
public class OAuthUtil {

	public static Response createRequest10a(
			String protocolVersion, String requestURL,
			String accessTokenEndpoint, String accessTokenString,
			String accessTokenSecret, String apiKey, String apiSecret,
			String authorizationURL, String requestTokenEndpoint)
		throws Exception {

		OAuth oAuth = new OAuth10a(
			accessTokenEndpoint, accessTokenString, accessTokenSecret,
			apiKey, apiSecret, authorizationURL, requestTokenEndpoint);

		OAuthRequest oAuthRequest = oAuth.getOAuthRequest(requestURL);

		Response response = oAuthRequest.send();

		if (!response.isSuccessful()) {
			throw new Exception("Request failed");
		}

		return response;
	}

	public static Response createRequest20(
			String protocolVersion, String requestURL,
			String accessTokenEndpoint, String accessTokenString,
			String apiKey, String apiSecret, String authorizationBaseURL,
			String callbackURL)
		throws Exception {

		OAuth oAuth = new OAuth20(
			accessTokenEndpoint, accessTokenString, apiKey, apiSecret,
			authorizationBaseURL, callbackURL);

		OAuthRequest oAuthRequest = oAuth.getOAuthRequest(requestURL);

		Response response = oAuthRequest.send();

		if (!response.isSuccessful()) {
			throw new Exception("Request failed");
		}

		return response;
	}

}