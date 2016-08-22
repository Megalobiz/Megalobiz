package com.megalobiz.megalobiz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthBaseRequest;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Created by KeitelRobespierre on 8/16/2016.
 */
public class MegalobizOAuthServiceImpl implements OAuthService {
    private static final String VERSION = "2.0";
    private final DefaultApi20 api;
    private final OAuthConfig config;

    public MegalobizOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        this.api = api;
        this.config = config;
    }

    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(this.api.getAccessTokenVerb(), this.api.getAccessTokenEndpoint());

        //Megalobiz customization of Oauth Service
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.addBodyParameter("client_id", this.config.getApiKey());
        request.addBodyParameter("client_secret", this.config.getApiSecret());

        if(MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.AUTHORIZATION) {
            // for Authorization code grant type
            request.addBodyParameter("code", verifier.getValue());
            request.addBodyParameter("redirect_uri", this.config.getCallback());
            if(this.config.hasScope()) {
                request.addBodyParameter("scope", this.config.getScope());
            }
            request.addBodyParameter("grant_type", "authorization_code");

        } else {
            // for Client Credentials grant type
            request.addBodyParameter("grant_type", "client_credentials");
        }





        Response response = request.send();
        return this.api.getAccessTokenExtractor().extract(response.getBody());
    }

    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use \'getAuthorizationUrl\' and redirect your users there");
    }

    public String getVersion() {
        return "2.0";
    }

    public void signRequest(Token accessToken, OAuthBaseRequest request) {
        request.addQuerystringParameter("access_token", accessToken.getToken());
    }

    public String getAuthorizationUrl(Token requestToken) {
        return this.api.getAuthorizationUrl(this.config);
    }
}