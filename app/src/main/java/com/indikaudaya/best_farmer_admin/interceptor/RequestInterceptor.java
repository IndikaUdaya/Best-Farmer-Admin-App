package com.indikaudaya.best_farmer_admin.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private static final String TAG = RequestInterceptor.class.getName();

    private String token;

    public RequestInterceptor(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.i(TAG, "Call log interceptor ....");
        Request request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type","application/json")
                .build();

        return chain.proceed(request);
    }
}
