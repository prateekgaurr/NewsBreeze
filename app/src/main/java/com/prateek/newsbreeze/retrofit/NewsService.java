package com.prateek.newsbreeze.retrofit;

import com.prateek.newsbreeze.models.NewsCallResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsService {

    @Headers("x-api-key:" + Config.API_KEY)
    @GET("/v2/everything")
    abstract Call<NewsCallResponse> getNews(
            @Query("qInTitle") String search,
            @Query("language") String language,
            @Query("pageSize") int pageSize
    );

    @Headers("x-api-key:" + Config.API_KEY)
    @GET("/v2/top-headlines")
    abstract Call<NewsCallResponse> getNews(
            @Query("language") String language
    );

}
