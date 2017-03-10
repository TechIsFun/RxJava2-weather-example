---

# Weather Android App with RxJava 2

## Andrea Maglie

---

# Introduction

---

# Dependencies

```java
    // rxjava 2
    compile 'io.reactivex.rxjava2:rxjava:2.0.7'

    // retrofit 2
    compile 'com.squareup.retrofit2:retrofit:2.2.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
    compile 'com.squareup.retrofit2:converter-gson:2.2.0'
```

---

# Create Retrofit2 service

```java
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.darksky.net")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    mDarkSkyApi = retrofit.create(DarkSkyApi.class);
```

---