package com.github.techisfun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.techisfun.api.DarkSkyApi;
import com.github.techisfun.rx_location.RxLocation;
import com.google.android.gms.location.LocationRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

    private DarkSkyApi mDarkSkyApi;
    private Disposable mSubscription;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.text1);

        mDarkSkyApi = Utils.buildDarkSkyInstance(HttpUrl.parse("https://api.darksky.net"));

        checkLocationPermission();

    }

    private void checkLocationPermission() {
        final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        try {
            if (ActivityCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{locationPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationAndWeather();
            } else {
                displayPermissionError();
            }
        }

    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationAndWeather() {
        Log.d(TAG, "requesting location");
        RxLocation rxLocation = new RxLocation(getApplicationContext());

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);

        rxLocation.location().updates(locationRequest)
                .flatMap(location -> {
                    Log.d(TAG, "requesting weather");
                    String key = "abf1b0eed723ba91680f088d90290e6a";
                    return mDarkSkyApi.forecast(key, location.getLatitude(), location.getLongitude())
                            .subscribeOn(Schedulers.io());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherDataJson -> {
                    String currentWeather = weatherDataJson.get("currently").getAsJsonObject()
                            .get("summary").getAsString();
                    mTextView.setText(currentWeather);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationAndWeather();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    private void displayPermissionError() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
