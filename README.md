### GgaREST
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.greengrowapps/ggarest/badge.svg?style=flat)
![Travis](https://travis-ci.org/greengrowapps/ggarest.svg)
### Introduction
This library pretends to make easy do http connections in Android (Modifying a couple of classes can work in regular Java too). It's prepared specially for using JSON, by default uses a JSON de/serializer.
The strongest points that have is: when you make a request (get, post, put, delete) and you register the listeners, your listener code will be code in the main thread. Thats allow modifying UI elements directly in the listener code. And the second thing is that you can specify the class that you are expecting and will be deserialized using Jackson library, if is it a list, you also can use the list listener to deserialize as object list.

### Usage

To use the GgaRest class you only have to call the init method when your application start. If you want you can add here default headers like an Api key.

```java
GgaRest.init(this);
GgaRest.addDefaulteader("Accept","application/json");
```
Here the library does a get over the url and deserializes the content as OriginAndUrl class:

```java
GgaRest.ws().get("http://httpbin.org/get")
        .onSuccess(OriginAndUrl.class, new OnObjResponseListener<OriginAndUrl>() {
            @Override
            public void onResponse(int code, OriginAndUrl object, Response fullResponse) {
                txtUrl.setText(object.url);
                txtIp.setText(object.origin);
            }
        })
        .onOther(new OnResponseListener() {
            @Override
            public void onResponse(int code, Response fullResponse) {
                Toast.makeText(MainActivity.this, "Get failed", Toast.LENGTH_SHORT).show();
            }
        })
        .execute();
```
Or here we use basic auth to retrieve the closest trends using the twitter api:

```java
    GgaRest.useBasicAuth("username","password");

    GgaRest.ws()
            .get("https://api.twitter.com/1.1/trends/closest.json")
            .onSuccess(Trend.class, new OnListResponseListener<Trend>() {
                @Override
                public void onResponse(int code, List<Trend> trends, Response fullResponse) {
                    adapter.updateData(trends);
                }
            })
            .onResponse(401, new OnResponseListener() {
                @Override
                public void onResponse(int code, Response fullResponse) {
                    Toast.makeText(MainActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                }
            })
            .execute();
```
    
### Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.greengrowapps:ggarest:0.3'
}
```

**2)** Initialize the class in your application onCreate:

```java
GgaRest.init(this);
```
    
### Things to develop
This versión is not still the 1.0. Need some test and development. If you want to collaborate would be nice to have this features:
+ More authentication methods like OAuth OAuth2
+ Response stream to string using the headers encoding
+ Request cache
+ More tests

### License

```
   Apache License Version 2.0, January 2004

   Copyright 2015 Green Grow Apps SC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```
    

