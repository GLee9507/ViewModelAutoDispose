package com.gene.sample;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class SingleTask extends Single<Boolean> {
    String TAG = "glee";

    @Override
    protected void subscribeActual(SingleObserver<? super Boolean> observer) {
        String uri = "http://www.google.com";
        URL url = null;
        try {
            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(60000);
            Log.d(TAG, "1subscribe() called with: emitter = [" + observer + "]");
            urlConnection.connect();
            Log.d(TAG, "2subscribe() called with: emitter = [" + observer + "]");
            urlConnection.disconnect();
            Log.d(TAG, "3subscribe() called with: emitter = [" + observer + "]");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "5subscribe() called with: emitter = [" + observer + "]");

        observer.onSuccess(true);
    }
}
