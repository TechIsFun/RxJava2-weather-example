package com.github.techisfun;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.github.techisfun.api.DarkSkyApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MockApiTest {

    /**
     * https://api.darksky.net/forecast/abf1b0eed723ba91680f088d90290e6a/37.8267,-122.4233
     */
    private final String responseExample = "{\"latitude\":37.8267,\"longitude\":-122.4233,\"timezone\":\"America/Los_Angeles\",\"offset\":-8,\"currently\":{\"time\":1489156010,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-night\",\"nearestStormDistance\":29,\"nearestStormBearing\":36,\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":53.99,\"apparentTemperature\":53.99,\"dewPoint\":51.56,\"humidity\":0.91,\"windSpeed\":3.47,\"windBearing\":295,\"visibility\":6.4,\"cloudCover\":0.91,\"pressure\":1021.28,\"ozone\":275.11},\"minutely\":{\"summary\":\"Mostly cloudy for the hour.\",\"icon\":\"partly-cloudy-night\",\"data\":[{\"time\":1489155960,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156020,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156080,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156140,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156200,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156260,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156320,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156380,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156440,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156500,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156560,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156620,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156680,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156740,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156800,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156860,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156920,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489156980,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157040,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157100,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157160,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157220,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157280,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157340,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157400,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157460,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157520,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157580,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157640,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157700,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157760,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157820,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157880,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489157940,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158000,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158060,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158120,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158180,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158240,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158300,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158360,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158420,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158480,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158540,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158600,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158660,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158720,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158780,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158840,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158900,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489158960,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159020,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159080,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159140,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159200,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159260,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159320,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159380,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159440,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159500,\"precipIntensity\":0,\"precipProbability\":0},{\"time\":1489159560,\"precipIntensity\":0,\"precipProbability\":0}]},\"hourly\":{\"summary\":\"Mostly cloudy until this evening.\",\"icon\":\"partly-cloudy-day\",\"data\":[{\"time\":1489154400,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":53.99,\"apparentTemperature\":53.99,\"dewPoint\":51.57,\"humidity\":0.92,\"windSpeed\":3.35,\"windBearing\":294,\"visibility\":6.29,\"cloudCover\":0.92,\"pressure\":1021.28,\"ozone\":274.77},{\"time\":1489158000,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":52.67,\"apparentTemperature\":52.67,\"dewPoint\":50.24,\"humidity\":0.91,\"windSpeed\":3.62,\"windBearing\":297,\"visibility\":6.53,\"cloudCover\":0.88,\"pressure\":1021.29,\"ozone\":275.52},{\"time\":1489161600,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":54.33,\"apparentTemperature\":54.33,\"dewPoint\":51.53,\"humidity\":0.9,\"windSpeed\":3.67,\"windBearing\":305,\"visibility\":6.84,\"cloudCover\":0.89,\"pressure\":1021.49,\"ozone\":275.74},{\"time\":1489165200,\"summary\":\"Overcast\",\"icon\":\"cloudy\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":56.65,\"apparentTemperature\":56.65,\"dewPoint\":52.44,\"humidity\":0.86,\"windSpeed\":4.04,\"windBearing\":311,\"visibility\":7.79,\"cloudCover\":0.94,\"pressure\":1021.78,\"ozone\":275.8},{\"time\":1489168800,\"summary\":\"Overcast\",\"icon\":\"cloudy\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":58.34,\"apparentTemperature\":58.34,\"dewPoint\":52.85,\"humidity\":0.82,\"windSpeed\":4.02,\"windBearing\":319,\"visibility\":8.59,\"cloudCover\":1,\"pressure\":1021.92,\"ozone\":276.25},{\"time\":1489172400,\"summary\":\"Overcast\",\"icon\":\"cloudy\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":59.89,\"apparentTemperature\":59.89,\"dewPoint\":52.89,\"humidity\":0.78,\"windSpeed\":4.22,\"windBearing\":324,\"visibility\":9.39,\"cloudCover\":1,\"pressure\":1021.81,\"ozone\":277.47},{\"time\":1489176000,\"summary\":\"Overcast\",\"icon\":\"cloudy\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":62.33,\"apparentTemperature\":62.33,\"dewPoint\":54.24,\"humidity\":0.75,\"windSpeed\":5.07,\"windBearing\":322,\"visibility\":9.56,\"cloudCover\":1,\"pressure\":1021.54,\"ozone\":279.08},{\"time\":1489179600,\"summary\":\"Overcast\",\"icon\":\"cloudy\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":64.73,\"apparentTemperature\":64.73,\"dewPoint\":55.68,\"humidity\":0.73,\"windSpeed\":5.28,\"windBearing\":318,\"visibility\":9.58,\"cloudCover\":1,\"pressure\":1021.15,\"ozone\":280.53},{\"time\":1489183200,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":66.67,\"apparentTemperature\":66.67,\"dewPoint\":56.72,\"humidity\":0.7,\"windSpeed\":5.99,\"windBearing\":316,\"visibility\":9.69,\"cloudCover\":0.88,\"pressure\":1020.58,\"ozone\":281.61},{\"time\":1489186800,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":67.93,\"apparentTemperature\":67.93,\"dewPoint\":57.51,\"humidity\":0.69,\"windSpeed\":6.91,\"windBearing\":314,\"visibility\":9.66,\"cloudCover\":0.73,\"pressure\":1019.91,\"ozone\":282.52},{\"time\":1489190400,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":67.07,\"apparentTemperature\":67.07,\"dewPoint\":57.09,\"humidity\":0.7,\"windSpeed\":7.16,\"windBearing\":311,\"visibility\":9.52,\"cloudCover\":0.67,\"pressure\":1019.42,\"ozone\":283.33},{\"time\":1489194000,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":64.85,\"apparentTemperature\":64.85,\"dewPoint\":56.47,\"humidity\":0.74,\"windSpeed\":6.82,\"windBearing\":305,\"visibility\":9,\"cloudCover\":0.64,\"pressure\":1019.2,\"ozone\":284.02},{\"time\":1489197600,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":61.46,\"apparentTemperature\":61.46,\"dewPoint\":54.94,\"humidity\":0.79,\"windSpeed\":6.65,\"windBearing\":299,\"visibility\":8.12,\"cloudCover\":0.5,\"pressure\":1019.15,\"ozone\":284.61},{\"time\":1489201200,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":59.24,\"apparentTemperature\":59.24,\"dewPoint\":54.14,\"humidity\":0.83,\"windSpeed\":6.45,\"windBearing\":300,\"visibility\":7.47,\"cloudCover\":0.41,\"pressure\":1019.17,\"ozone\":285.14},{\"time\":1489204800,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":57.77,\"apparentTemperature\":57.77,\"dewPoint\":53.37,\"humidity\":0.85,\"windSpeed\":6.45,\"windBearing\":302,\"visibility\":7.34,\"cloudCover\":0.3,\"pressure\":1019.27,\"ozone\":285.66},{\"time\":1489208400,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":56.68,\"apparentTemperature\":56.68,\"dewPoint\":52.78,\"humidity\":0.87,\"windSpeed\":5.82,\"windBearing\":307,\"visibility\":7.45,\"cloudCover\":0.23,\"pressure\":1019.44,\"ozone\":286.13},{\"time\":1489212000,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":55.89,\"apparentTemperature\":55.89,\"dewPoint\":52.23,\"humidity\":0.88,\"windSpeed\":4.74,\"windBearing\":311,\"visibility\":7.46,\"cloudCover\":0.18,\"pressure\":1019.54,\"ozone\":286.39},{\"time\":1489215600,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":55.17,\"apparentTemperature\":55.17,\"dewPoint\":51.84,\"humidity\":0.89,\"windSpeed\":4.35,\"windBearing\":310,\"visibility\":7.44,\"cloudCover\":0.18,\"pressure\":1019.54,\"ozone\":286.23},{\"time\":1489219200,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":54.64,\"apparentTemperature\":54.64,\"dewPoint\":51.55,\"humidity\":0.89,\"windSpeed\":4.51,\"windBearing\":308,\"visibility\":8.04,\"cloudCover\":0.17,\"pressure\":1019.48,\"ozone\":285.87},{\"time\":1489222800,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":54.35,\"apparentTemperature\":54.35,\"dewPoint\":51.29,\"humidity\":0.89,\"windSpeed\":4.58,\"windBearing\":306,\"visibility\":8.69,\"cloudCover\":0.21,\"pressure\":1019.42,\"ozone\":285.8},{\"time\":1489226400,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":53.57,\"apparentTemperature\":53.57,\"dewPoint\":50.91,\"humidity\":0.91,\"windSpeed\":4.32,\"windBearing\":306,\"visibility\":9.21,\"cloudCover\":0.31,\"pressure\":1019.36,\"ozone\":286.09},{\"time\":1489230000,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":52.83,\"apparentTemperature\":52.83,\"dewPoint\":50.09,\"humidity\":0.9,\"windSpeed\":3.94,\"windBearing\":306,\"visibility\":9.56,\"cloudCover\":0.38,\"pressure\":1019.31,\"ozone\":286.68},{\"time\":1489233600,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":51.73,\"apparentTemperature\":51.73,\"dewPoint\":49.36,\"humidity\":0.92,\"windSpeed\":3.82,\"windBearing\":304,\"visibility\":10,\"cloudCover\":0.51,\"pressure\":1019.38,\"ozone\":287.96},{\"time\":1489237200,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":51.09,\"apparentTemperature\":51.09,\"dewPoint\":48.98,\"humidity\":0.92,\"windSpeed\":3.86,\"windBearing\":303,\"visibility\":10,\"cloudCover\":0.66,\"pressure\":1019.64,\"ozone\":290.35},{\"time\":1489240800,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":50.48,\"apparentTemperature\":50.48,\"dewPoint\":48.38,\"humidity\":0.92,\"windSpeed\":4.07,\"windBearing\":302,\"visibility\":10,\"cloudCover\":0.83,\"pressure\":1020.04,\"ozone\":293.43},{\"time\":1489244400,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":51.83,\"apparentTemperature\":51.83,\"dewPoint\":49.39,\"humidity\":0.91,\"windSpeed\":4.1,\"windBearing\":300,\"visibility\":10,\"cloudCover\":0.88,\"pressure\":1020.45,\"ozone\":296.33},{\"time\":1489248000,\"summary\":\"Mostly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":54.07,\"apparentTemperature\":54.07,\"dewPoint\":50.63,\"humidity\":0.88,\"windSpeed\":3.32,\"windBearing\":296,\"visibility\":10,\"cloudCover\":0.73,\"pressure\":1020.89,\"ozone\":299.01},{\"time\":1489251600,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":56.28,\"apparentTemperature\":56.28,\"dewPoint\":51.35,\"humidity\":0.83,\"windSpeed\":2.62,\"windBearing\":295,\"visibility\":10,\"cloudCover\":0.45,\"pressure\":1021.35,\"ozone\":301.51},{\"time\":1489255200,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":58.1,\"apparentTemperature\":58.1,\"dewPoint\":51.55,\"humidity\":0.79,\"windSpeed\":2.75,\"windBearing\":296,\"visibility\":10,\"cloudCover\":0.22,\"pressure\":1021.63,\"ozone\":303.05},{\"time\":1489258800,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":60.54,\"apparentTemperature\":60.54,\"dewPoint\":52.1,\"humidity\":0.74,\"windSpeed\":4.09,\"windBearing\":292,\"visibility\":10,\"cloudCover\":0.13,\"pressure\":1021.61,\"ozone\":303.15},{\"time\":1489262400,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":64.06,\"apparentTemperature\":64.06,\"dewPoint\":53.61,\"humidity\":0.69,\"windSpeed\":6.39,\"windBearing\":288,\"visibility\":10,\"cloudCover\":0.12,\"pressure\":1021.4,\"ozone\":302.29},{\"time\":1489266000,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":67.5,\"apparentTemperature\":67.5,\"dewPoint\":55.53,\"humidity\":0.65,\"windSpeed\":7.94,\"windBearing\":288,\"visibility\":10,\"cloudCover\":0.12,\"pressure\":1021.14,\"ozone\":301.16},{\"time\":1489269600,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":69.37,\"apparentTemperature\":69.37,\"dewPoint\":56.77,\"humidity\":0.64,\"windSpeed\":9.27,\"windBearing\":290,\"visibility\":10,\"cloudCover\":0.11,\"pressure\":1020.79,\"ozone\":299.6},{\"time\":1489273200,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":68.31,\"apparentTemperature\":68.31,\"dewPoint\":55.92,\"humidity\":0.65,\"windSpeed\":10.66,\"windBearing\":293,\"visibility\":10,\"cloudCover\":0.09,\"pressure\":1020.41,\"ozone\":297.76},{\"time\":1489276800,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":66.42,\"apparentTemperature\":66.42,\"dewPoint\":54.91,\"humidity\":0.66,\"windSpeed\":11.38,\"windBearing\":295,\"visibility\":10,\"cloudCover\":0.09,\"pressure\":1020.22,\"ozone\":296.73},{\"time\":1489280400,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":63.63,\"apparentTemperature\":63.63,\"dewPoint\":53.94,\"humidity\":0.71,\"windSpeed\":11.48,\"windBearing\":297,\"visibility\":10,\"cloudCover\":0.09,\"pressure\":1020.39,\"ozone\":297.34},{\"time\":1489284000,\"summary\":\"Clear\",\"icon\":\"clear-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":60.1,\"apparentTemperature\":60.1,\"dewPoint\":52.71,\"humidity\":0.77,\"windSpeed\":10.93,\"windBearing\":301,\"visibility\":10,\"cloudCover\":0.09,\"pressure\":1020.75,\"ozone\":298.75},{\"time\":1489287600,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":57.34,\"apparentTemperature\":57.34,\"dewPoint\":51.73,\"humidity\":0.82,\"windSpeed\":10.45,\"windBearing\":303,\"visibility\":10,\"cloudCover\":0.11,\"pressure\":1021.11,\"ozone\":299.58},{\"time\":1489291200,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":55.81,\"apparentTemperature\":55.81,\"dewPoint\":51.23,\"humidity\":0.85,\"windSpeed\":9.48,\"windBearing\":304,\"visibility\":10,\"cloudCover\":0.14,\"pressure\":1021.43,\"ozone\":299.22},{\"time\":1489294800,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":55,\"apparentTemperature\":55,\"dewPoint\":51.09,\"humidity\":0.87,\"windSpeed\":8.26,\"windBearing\":305,\"visibility\":10,\"cloudCover\":0.17,\"pressure\":1021.75,\"ozone\":298.26},{\"time\":1489298400,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":54.44,\"apparentTemperature\":54.44,\"dewPoint\":50.99,\"humidity\":0.88,\"windSpeed\":7.39,\"windBearing\":306,\"visibility\":10,\"cloudCover\":0.2,\"pressure\":1021.98,\"ozone\":297.1},{\"time\":1489302000,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":53.78,\"apparentTemperature\":53.78,\"dewPoint\":50.66,\"humidity\":0.89,\"windSpeed\":6.91,\"windBearing\":306,\"visibility\":10,\"cloudCover\":0.21,\"pressure\":1022.1,\"ozone\":295.72},{\"time\":1489305600,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":53.15,\"apparentTemperature\":53.15,\"dewPoint\":50.19,\"humidity\":0.9,\"windSpeed\":6.56,\"windBearing\":306,\"visibility\":10,\"cloudCover\":0.2,\"pressure\":1022.13,\"ozone\":294.13},{\"time\":1489309200,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":52.63,\"apparentTemperature\":52.63,\"dewPoint\":49.69,\"humidity\":0.9,\"windSpeed\":6.29,\"windBearing\":307,\"visibility\":10,\"cloudCover\":0.2,\"pressure\":1022.08,\"ozone\":292.72},{\"time\":1489312800,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":52.24,\"apparentTemperature\":52.24,\"dewPoint\":49.15,\"humidity\":0.89,\"windSpeed\":6.28,\"windBearing\":312,\"visibility\":10,\"cloudCover\":0.19,\"pressure\":1021.89,\"ozone\":291.59},{\"time\":1489316400,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":51.94,\"apparentTemperature\":51.94,\"dewPoint\":48.55,\"humidity\":0.88,\"windSpeed\":6.43,\"windBearing\":319,\"visibility\":10,\"cloudCover\":0.19,\"pressure\":1021.6,\"ozone\":290.64},{\"time\":1489320000,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":51.36,\"apparentTemperature\":51.36,\"dewPoint\":47.65,\"humidity\":0.87,\"windSpeed\":6.51,\"windBearing\":325,\"visibility\":10,\"cloudCover\":0.18,\"pressure\":1021.41,\"ozone\":289.99},{\"time\":1489323600,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":50.51,\"apparentTemperature\":50.51,\"dewPoint\":46.54,\"humidity\":0.86,\"windSpeed\":6.28,\"windBearing\":329,\"visibility\":10,\"cloudCover\":0.18,\"pressure\":1021.45,\"ozone\":289.84},{\"time\":1489327200,\"summary\":\"Clear\",\"icon\":\"clear-night\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":49.77,\"apparentTemperature\":47.48,\"dewPoint\":45.5,\"humidity\":0.85,\"windSpeed\":5.83,\"windBearing\":332,\"visibility\":10,\"cloudCover\":0.17,\"pressure\":1021.58,\"ozone\":289.99}]},\"daily\":{\"summary\":\"Drizzle on Wednesday, with temperatures peaking at 73°F on Monday.\",\"icon\":\"rain\",\"data\":[{\"time\":1489132800,\"summary\":\"Mostly cloudy until evening.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1489156150,\"sunsetTime\":1489198402,\"moonPhase\":0.45,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":52.67,\"temperatureMinTime\":1489158000,\"temperatureMax\":67.93,\"temperatureMaxTime\":1489186800,\"apparentTemperatureMin\":52.67,\"apparentTemperatureMinTime\":1489158000,\"apparentTemperatureMax\":67.93,\"apparentTemperatureMaxTime\":1489186800,\"dewPoint\":53.21,\"humidity\":0.83,\"windSpeed\":4.48,\"windBearing\":306,\"visibility\":7.87,\"cloudCover\":0.73,\"pressure\":1020.69,\"ozone\":279.46},{\"time\":1489219200,\"summary\":\"Mostly cloudy in the morning.\",\"icon\":\"partly-cloudy-night\",\"sunriseTime\":1489242460,\"sunsetTime\":1489284859,\"moonPhase\":0.48,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":50.48,\"temperatureMinTime\":1489240800,\"temperatureMax\":69.37,\"temperatureMaxTime\":1489269600,\"apparentTemperatureMin\":50.48,\"apparentTemperatureMinTime\":1489240800,\"apparentTemperatureMax\":69.37,\"apparentTemperatureMaxTime\":1489269600,\"dewPoint\":51.86,\"humidity\":0.82,\"windSpeed\":6.49,\"windBearing\":299,\"visibility\":9.81,\"cloudCover\":0.29,\"pressure\":1020.67,\"ozone\":295.95},{\"time\":1489305600,\"summary\":\"Partly cloudy starting in the afternoon.\",\"icon\":\"partly-cloudy-night\",\"sunriseTime\":1489328770,\"sunsetTime\":1489371316,\"moonPhase\":0.51,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":49.77,\"temperatureMinTime\":1489327200,\"temperatureMax\":71.99,\"temperatureMaxTime\":1489359600,\"apparentTemperatureMin\":47.48,\"apparentTemperatureMinTime\":1489327200,\"apparentTemperatureMax\":71.99,\"apparentTemperatureMaxTime\":1489359600,\"dewPoint\":51.59,\"humidity\":0.78,\"windSpeed\":6.26,\"windBearing\":303,\"visibility\":10,\"cloudCover\":0.2,\"pressure\":1021.01,\"ozone\":287.62},{\"time\":1489388400,\"summary\":\"Partly cloudy throughout the day.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1489415079,\"sunsetTime\":1489457772,\"moonPhase\":0.54,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":49.1,\"temperatureMinTime\":1489413600,\"temperatureMax\":73.31,\"temperatureMaxTime\":1489446000,\"apparentTemperatureMin\":49.1,\"apparentTemperatureMinTime\":1489413600,\"apparentTemperatureMax\":73.31,\"apparentTemperatureMaxTime\":1489446000,\"dewPoint\":50.94,\"humidity\":0.75,\"windSpeed\":3.02,\"windBearing\":285,\"visibility\":10,\"cloudCover\":0.36,\"pressure\":1017.38,\"ozone\":287.04},{\"time\":1489474800,\"summary\":\"Partly cloudy starting in the afternoon.\",\"icon\":\"partly-cloudy-night\",\"sunriseTime\":1489501388,\"sunsetTime\":1489544228,\"moonPhase\":0.57,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":53.73,\"temperatureMinTime\":1489496400,\"temperatureMax\":69.01,\"temperatureMaxTime\":1489532400,\"apparentTemperatureMin\":53.73,\"apparentTemperatureMinTime\":1489496400,\"apparentTemperatureMax\":69.01,\"apparentTemperatureMaxTime\":1489532400,\"dewPoint\":53.32,\"humidity\":0.79,\"windSpeed\":3.81,\"windBearing\":258,\"cloudCover\":0.46,\"pressure\":1014.89,\"ozone\":293.85},{\"time\":1489561200,\"summary\":\"Drizzle in the afternoon.\",\"icon\":\"rain\",\"sunriseTime\":1489587697,\"sunsetTime\":1489630684,\"moonPhase\":0.61,\"precipIntensity\":0.0019,\"precipIntensityMax\":0.0075,\"precipIntensityMaxTime\":1489611600,\"precipProbability\":0.37,\"precipType\":\"rain\",\"temperatureMin\":54.04,\"temperatureMinTime\":1489586400,\"temperatureMax\":61.29,\"temperatureMaxTime\":1489622400,\"apparentTemperatureMin\":54.04,\"apparentTemperatureMinTime\":1489586400,\"apparentTemperatureMax\":61.29,\"apparentTemperatureMaxTime\":1489622400,\"dewPoint\":52.63,\"humidity\":0.87,\"windSpeed\":5.22,\"windBearing\":246,\"cloudCover\":0.85,\"pressure\":1018.91,\"ozone\":299.99},{\"time\":1489647600,\"summary\":\"Partly cloudy throughout the day.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1489674006,\"sunsetTime\":1489717140,\"moonPhase\":0.64,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureMin\":51.53,\"temperatureMinTime\":1489669200,\"temperatureMax\":65.23,\"temperatureMaxTime\":1489705200,\"apparentTemperatureMin\":51.53,\"apparentTemperatureMinTime\":1489669200,\"apparentTemperatureMax\":65.23,\"apparentTemperatureMaxTime\":1489705200,\"dewPoint\":51.04,\"humidity\":0.82,\"windSpeed\":4.17,\"windBearing\":263,\"cloudCover\":0.57,\"pressure\":1022.16,\"ozone\":313.62},{\"time\":1489734000,\"summary\":\"Light rain overnight.\",\"icon\":\"rain\",\"sunriseTime\":1489760314,\"sunsetTime\":1489803596,\"moonPhase\":0.67,\"precipIntensity\":0.0006,\"precipIntensityMax\":0.0017,\"precipIntensityMaxTime\":1489816800,\"precipProbability\":0.04,\"precipType\":\"rain\",\"temperatureMin\":50.56,\"temperatureMinTime\":1489748400,\"temperatureMax\":66.34,\"temperatureMaxTime\":1489791600,\"apparentTemperatureMin\":50.56,\"apparentTemperatureMinTime\":1489748400,\"apparentTemperatureMax\":66.34,\"apparentTemperatureMaxTime\":1489791600,\"dewPoint\":47.61,\"humidity\":0.71,\"windSpeed\":3.52,\"windBearing\":265,\"cloudCover\":0.37,\"pressure\":1021.32,\"ozone\":290.54}]},\"flags\":{\"sources\":[\"lamp\",\"gfs\",\"cmc\",\"nam\",\"rap\",\"rtma\",\"sref\",\"fnmoc\",\"isd\",\"madis\",\"nearest-precip\",\"nwspa\",\"darksky\"],\"lamp-stations\":[\"KAPC\",\"KCCR\",\"KHWD\",\"KLVK\",\"KNUQ\",\"KOAK\",\"KPAO\",\"KSFO\",\"KSQL\"],\"isd-stations\":[\"724943-99999\",\"745039-99999\",\"745045-99999\",\"745060-23239\",\"745065-99999\",\"994016-99999\",\"994033-99999\",\"994036-99999\",\"997734-99999\",\"998163-99999\",\"998197-99999\",\"998476-99999\",\"998477-99999\",\"998479-99999\",\"999999-23239\",\"999999-23272\"],\"madis-stations\":[\"AU915\",\"C5988\",\"C8158\",\"C9629\",\"CQ147\",\"D5422\",\"D8008\",\"E0426\",\"E6067\",\"E9227\",\"FTPC1\",\"GGBC1\",\"OKXC1\",\"OMHC1\",\"PPXC1\",\"SFOC1\"],\"units\":\"us\"}}";

    @Rule
    public final MockWebServer server = new MockWebServer();

    @Test
    public void testForecast() throws Exception {
        server.enqueue(new MockResponse().setBody(responseExample));

        DarkSkyApi darkSkyApi = Utils.buildDarkSkyInstance(server.url("/"));

        Observable<JsonObject> forecast = darkSkyApi.forecast("", -1, -1);
        TestObserver<JsonObject> test = forecast.test();
        test.assertNoErrors();
        JsonObject expected = new Gson().fromJson(responseExample, JsonObject.class);
        test.assertResult(expected);
    }

}
