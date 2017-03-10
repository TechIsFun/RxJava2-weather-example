package com.github.techisfun.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DarkSkyApi {

    /**
     * https://api.darksky.net/forecast/[key]/[latitude],[longitude]
     */
    @GET("forecast/{key}/{latitude},{longitude}")
    Observable<JsonObject> forecast(@Path("key") String apiKey,
                                    @Path("latitude") float latitude,
                                    @Path("longitude") float longitude);

}
