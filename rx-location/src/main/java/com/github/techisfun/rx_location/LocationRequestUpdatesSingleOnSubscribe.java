package com.github.techisfun.rx_location;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleEmitter;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
class LocationRequestUpdatesSingleOnSubscribe extends RxLocationSingleOnSubscribe<Status> {

    private final LocationRequest locationRequest;
    private final PendingIntent pendingIntent;

    LocationRequestUpdatesSingleOnSubscribe(@NonNull RxLocation rxLocation, LocationRequest locationRequest, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        super(rxLocation, timeout, timeUnit);
        this.locationRequest = locationRequest;
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<Status> emitter) {
        //noinspection MissingPermission
        setupLocationPendingResult(
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent),
                SingleResultCallBack.get(emitter)
        );
    }
}
