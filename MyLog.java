package com.rangermerah.recyletor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by NajibSaurus on 1/5/17.
 */
public class MyLog {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public OkHttpClient getLog(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :HttpLoggingInterceptor.Level.NONE); // add log for debug mode

         OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(500, TimeUnit.MINUTES);
        httpClient.readTimeout(600, TimeUnit.MINUTES);
        httpClient.writeTimeout(600, TimeUnit.MINUTES);//set timeout

        OkHttpClient client = httpClient.build();

        return  client;

    }

    private void testLogMethod() {

    }
}
