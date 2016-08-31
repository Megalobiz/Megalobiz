package com.megalobiz.megalobiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Created by KeitelRobespierre on 8/15/2016.
 */

public class MegalobizApi extends DefaultApi20
{
    public static final String HOST = "http://192.168.37.53";

    private static final String AUTHORIZATION_URL =
            HOST + "/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";

    @Override
    public String getAuthorizationUrl( OAuthConfig config ) {
        return String.format(AUTHORIZATION_URL, config.getApiKey(), config.getCallback());
    }

    @Override
    public String getAccessTokenEndpoint(){
        return HOST + "/oauth/access_token";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }


    @Override
    public OAuthService createService(OAuthConfig config) {
        return new MegalobizOAuthServiceImpl(this, config);
    }
}
