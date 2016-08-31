package com.megalobiz.megalobiz;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;

import android.content.Context;
import android.net.Uri;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by KeitelRobespierre on 8/13/2016.
 */


/*
 *
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 *
 */
public class MegalobizClient extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = MegalobizApi.class; // Change this
    public static final String REST_URL = MegalobizApi.HOST + "/api/v1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "LIk7iAr3cVyd4rPF";       // Change this
    public static final String REST_CONSUMER_SECRET = "lNWF6IQEG5jmdsCEX0stuwy3NgLn3Ppr"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cbmegalobiz"; // Change this (here and in manifest)

    public MegalobizClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }


    // Retrieves access token given authorization url
    public void clientCredentials(Uri uri, OAuthAccessHandler handler) {
        // pass fake token to dump CodePath implementation authorization grant_type
        Token token = new Token("T5MV1tb4vyXCre10fJfWJCydFQ1ajjaYfPegO2y0", "t86wyN7E798QKjKYUNFumrEmjc0N2ud6");
        client.fetchAccessToken(getRequestToken(), uri);
    }

    // METHOD == ENDPOINT

    // get Top Showbiz (5 Bands, 5 Musicians, 5 Albums, 10 Songs)
    public void getTopShowbiz(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("home/megalohit");

        client.get(apiUrl, handler);
    }

    // get Showbiz list (Band, Musician, Album, Song)
    // ex: http://megalobiz.com/api/v1/band
    // params amount = 24
    // index = 0
    public void getShowbizs(int index, String showbizType, AsyncHttpResponseHandler handler) {
        // make the uri
        String apiUrl = getApiUrl(showbizType.toLowerCase());

        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        int amount = 24;
        if(showbizType.equals("Musician"))
            amount = 48;
        else if (showbizType.equals("Album"))
            amount = 40;
        else if (showbizType.equals("Song"))
            amount = 60;

        params.put("amount", amount);
        params.put("index", index);

        client.get(apiUrl, params, handler);
    }

    // get Showbiz Profile (Band, Musician, Album, Song)
    // ex: http://megalobiz.com/api/v1/band/1000
    public void getShowbiz(int id, String showbizType, AsyncHttpResponseHandler handler) {
        // make the uri
        String apiUrl = getApiUrl(String.format("%s/%d", showbizType.toLowerCase(), id));


        client.get(apiUrl, handler);
    }

    // search Showbiz (Band, Musician, Album, Song)
    // ex: http://megalobiz.com/api/v1/search
    // params qry = aimer
    public void searchShowbizs(String qry, AsyncHttpResponseHandler handler) {
        // make the uri
        String apiUrl = getApiUrl("search");

        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("qry", qry);

        client.get(apiUrl, params, handler);
    }

    // get Authenticated User
    // ex: http://megalobiz.com/api/v1/user/auth/info
    public void getAuthUser(AsyncHttpResponseHandler handler) {
        // make the uri
        String apiUrl = getApiUrl("user/auth/info");
        client.get(apiUrl, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}