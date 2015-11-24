package com.greengrowapps.sample;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginAndUrl {

    public String url;
    public String origin;
}
