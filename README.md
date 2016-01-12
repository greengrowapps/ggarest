### GgaREST
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.greengrowapps/ggarest/badge.svg?style=flat)
![Travis](https://travis-ci.org/greengrowapps/ggarest.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GgaREST-green.svg?style=true)](https://android-arsenal.com/details/1/2837)

### Introduction
This library makes it easy to work with http connections on Android, but you can also make it work on Java vanilla by modifying only a couple of classes. 
It's especially designed to work with JSON, as it uses a JSON de/serializer by default.
Its strongest points are the following : 
- When you make a request (get, post, put, delete) and you register the listeners, your listener code will be code in the main thread. This design allows the modification of UI components directly in the listener code.
- You can specify the object that you are expecting and it will be deserialized using the [Jackson library](https://github.com/FasterXML/jackson). If it's a list, you also can use the list listener to deserialize it as an object list.

### Usage

To use the GgaRest class you only have to call the init method when your application start. 
Here is an example by adding default headers like an Api key.

```java
GgaRest.init(this);
GgaRest.addDefaulteader("Accept","application/json");
```
This example gets the content of the url given and deserializes the content as an OriginAndUrl object:

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
Or here we use basic auth to retrieve the closest trends using the Twitter api:

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
    compile 'com.greengrowapps:ggarest:0.6'
}
```

**2)** Avoid duplicate files copied in APK adding this inside the android tag in your ``build.gradle``:

```groovy
    packagingOptions {
        exclude 'META-INF/ASL2.0'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/MANIFEST.MF'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }
```

**3)** Initialize the object in one of your activities' onCreate to start using it:

```java
GgaRest.init(this);
```
    
### Things to develop
This version is still not 1.0, which means api changes could happen. It needs some test and development.

If you want to collaborate, here is a checklist of planned features you can help working on :
+ More authentication methods like OAuth, OAuth2
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
    

