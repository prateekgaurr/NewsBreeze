package com.prateek.newsbreeze.retrofit;

import androidx.annotation.NonNull;

import com.prateek.newsbreeze.MyApp;
import com.prateek.newsbreeze.util.MyLogger;
import com.prateek.newsbreeze.util.NetworkHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Interceptors {
    public static final String TAG = "Interceptors";

    protected static okhttp3.Interceptor networkInterceptor() {
        return new okhttp3.Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                MyLogger.d(TAG, "network interceptor: called.");

                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build();

                return response.newBuilder()
                        .removeHeader(Config.HEADER_PRAGMA)
                        .removeHeader(Config.HEADER_CACHE_CONTROL)
                        .header(Config.HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    protected static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (@NonNull String message)
                    {
//                        MyLogger.d(TAG, "log: http log: " + message);
                    }
                } );
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    protected static okhttp3.Interceptor offlineInterceptor() {
        return new okhttp3.Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                MyLogger.d(TAG, "offline interceptor: called.");
                Request request = chain.request();

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!NetworkHelper.isNetworkConnected(MyApp.getAppContext())) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(30, TimeUnit.MINUTES)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(Config.HEADER_PRAGMA)
                            .removeHeader(Config.HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

}
