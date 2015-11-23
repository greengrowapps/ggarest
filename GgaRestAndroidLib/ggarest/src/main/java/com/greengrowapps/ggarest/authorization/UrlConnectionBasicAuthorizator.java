package com.greengrowapps.ggarest.authorization;

import android.util.Base64;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;


public class UrlConnectionBasicAuthorizator implements UrlConnectionAuthorizator {

    private final Credentials credentials;

    public UrlConnectionBasicAuthorizator(Credentials credentials){
        if(credentials==null){
            throw new InvalidParameterException("Credentials must not be null");
        }
        this.credentials = credentials;
    }


    @Override
    public HttpURLConnection authorize(HttpURLConnection urlConnection) {
        String userpass = credentials.getUsername() + ":" + credentials.getPassword();
        String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);

        urlConnection.setRequestProperty("Authorization", basicAuth);
        return urlConnection;
    }
}
