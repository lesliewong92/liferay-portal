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

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

/**
 * @author Leslie Wong
 */
public class OAuthUtil {

	public static String createRequest10a(
			String accessTokenEndpoint, String accessTokenString,
			String accessTokenSecret, String apiKey, String apiSecret,
			String authorizationURL, String requestTokenEndpoint,
			String requestURL)
		throws Exception {

		OAuthManager oAuthManager = new OAuthManager(
			accessTokenEndpoint, accessTokenSecret, accessTokenString, apiKey,
			apiSecret, authorizationURL, requestTokenEndpoint);

		System.out.println(requestURL);

		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, requestURL);

		oAuthManager.signRequest(oAuthRequest);

		Response response = oAuthRequest.send();

		if (!response.isSuccessful()) {
			throw new Exception("Request failed");
		}

		return response.getBody();
	}

	// public static Response createRequest20(
	// 		String protocolVersion, String requestURL,
	// 		String accessTokenEndpoint, String accessTokenString,
	// 		String apiKey, String apiSecret, String authorizationBaseURL,
	// 		String callbackURL)
	// 	throws Exception {

	// 	OAuth oAuth = new OAuth20(
	// 		accessTokenEndpoint, accessTokenString, apiKey, apiSecret,
	// 		authorizationBaseURL, callbackURL);

	// 	OAuthRequest oAuthRequest = oAuth.getOAuthRequest(requestURL);

	// 	Response response = oAuthRequest.send();

	// 	if (!response.isSuccessful()) {
	// 		throw new Exception("Request failed");
	// 	}

	// 	return response;
	// }

}