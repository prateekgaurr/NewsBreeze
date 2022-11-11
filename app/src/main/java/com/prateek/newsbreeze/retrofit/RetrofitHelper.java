package com.prateek.newsbreeze.retrofit;

import com.prateek.newsbreeze.MyApp;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final String TAG = "RetrofitHelper";

    private static RetrofitHelper instance;

    public static RetrofitHelper getInstance(){
        if(instance == null){
            instance = new RetrofitHelper();
        }
        return instance;
    }


    private static Retrofit retrofit(){
        return new Retrofit.Builder()
                .baseUrl(Config.NEWS_API_URL)
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(Interceptors.httpLoggingInterceptor())
                .addNetworkInterceptor(Interceptors.networkInterceptor())
                .addInterceptor(Interceptors.offlineInterceptor())
                .build();
    }

    private static Cache cache(){
        File cacheFile = new File(MyApp.getAppContext().getCacheDir(), "cache");
        return new Cache(cacheFile, Config.CACHE_SIZE);
    }

    public NewsService getApi(){
        return retrofit().create(NewsService.class);
    }

}
