package com.greengrowapps.ggarest.authorization;


import java.net.HttpURLConnection;

public interface UrlConnectionAuthorizator {

    HttpURLConnection authorize(HttpURLConnection urlConnection);

}
