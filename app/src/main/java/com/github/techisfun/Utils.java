package com.github.techisfun;

import android.support.annotation.NonNull;

import com.github.techisfun.api.DarkSkyApi;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrea Maglie on 10/03/17.
 */

public abstract class Utils {

    public static DarkSkyApi buildDarkSkyInstance(@NonNull HttpUrl endpoint) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(DarkSkyApi.class);
    }

    private Utils() {
        // nothing
    }
}
