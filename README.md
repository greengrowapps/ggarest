### GgaREST
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.greengrowapps/ggarest/badge.svg?style=flat)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.greengrowapps/ggarest-java/badge.svg?style=flat)
![Travis](https://travis-ci.org/greengrowapps/ggarest.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GgaREST-green.svg?style=true)](https://android-arsenal.com/details/1/2837)

### Introduction
This library makes it easy to work with http connections on Android or Java. 
It's especially designed to work with JSON, as it uses a JSON de/serializer by default.
Its strongest points are the following : 
- When you make a request (get, post, put, delete) and you register the listeners, your listener code will be code in the main thread. This design allows the modification of UI components directly in the listener code.
- You can specify the object that you are expecting and it will be deserialized using the [Jackson library](https://github.com/FasterXML/jackson). If it's a list, you also can use the list listener to deserialize it as an object list.

### Android Configuration

To use the GgaRest class you only have to call the init method when your application start. 
Here is an example by adding default headers like an Api key.

```java
GgaRest.init(this);
GgaRest.addDefaulteader("Accept","application/json");
```

### Java Configuration

You not need to init the Library. You can get a Webservice instance directly calling GgaRest.ws()

### Usage

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
            public void onResponse(int code, Response fullResponse, Exception exception) {
                Toast.makeText(MainActivity.this, "Get failed", Toast.LENGTH_SHORT).show();
            }
        })
        .execute();
```
Or here we use basic auth to retrieve the commits of this repository:

```java
    GgaRest.useBasicAuth(GITHUB_USERNAME,PASSWORD);

    GgaRest.ws()
            .get("https://api.github.com/repos/greengrowapps/ggarest/commits")
            .onSuccess(Commit.class, new OnListResponseListener<Commit>() {
                @Override
                public void onResponse(int code, List<Commit> object, Response fullResponse) {
                    populateCommits(object);
                }
            })
            .onResponse(401, new OnResponseListener() {
                @Override
                public void onResponse(int code, Response fullResponse, Exception exception) {
                    Toast.makeText(MainActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                }
            })
            .execute();
```
It's possible to mock some calls for test prouposes:

```java
        GgaRest.ws().mockGet("http://mysite/someendpoint/")
                .responseCode(200)
                .responseContent("{\"text\":\"hello\"}")
                .save();
```
    
### Android Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.greengrowapps:ggarest:0.16'
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

**3)** Initialize the object in your application onStart or in one of your activities' onCreate to start using it:

```java
GgaRest.init(this);
```

### Java Integration

Add as a dependency to your ``build.gradle`` or ``pom.xml``:

```groovy
dependencies {
    compile 'com.greengrowapps:ggarest-java:0.0.2'
}
```

```xml
<dependency>
    <groupId>com.greengrowapps</groupId>
    <artifactId>ggarest-java</artifactId>
    <version>0.0.2</version>
</dependency>
```

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

