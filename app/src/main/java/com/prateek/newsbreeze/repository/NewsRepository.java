package com.prateek.newsbreeze.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.prateek.newsbreeze.models.NewsCallResponse;
import com.prateek.newsbreeze.constants.RequestStatusTypes;
import com.prateek.newsbreeze.retrofit.NewsService;
import com.prateek.newsbreeze.util.MyLogger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private final NewsService service;
    private final MutableLiveData<NewsCallResponse> responseLiveData;
    private final String TAG = getClass().getSimpleName();
    private final MutableLiveData<RequestStatusTypes> requestStatusTypesMutableLiveData;

    public NewsRepository(NewsService service) {
        this.service = service;
        this.responseLiveData = new MutableLiveData<>();
        this.requestStatusTypesMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RequestStatusTypes> getRequestStatus(){
        return requestStatusTypesMutableLiveData;
    }

    public MutableLiveData<NewsCallResponse> getNews(String search){
        MyLogger.d(TAG, "Enqueuing a  network request with some search elements --> "+search);
        requestStatusTypesMutableLiveData.postValue(RequestStatusTypes.PROCESSING);
        service.getNews(search, "en", 50).enqueue(new Callback<NewsCallResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsCallResponse> call, @NonNull Response<NewsCallResponse> response) {
                handleApiResponse(response);
            }
            @Override
            public void onFailure(@NonNull Call<NewsCallResponse> call, @NonNull Throwable t) {
                handleApiFailedResponse(t);
            }
        });
        return responseLiveData;
    }

    private void handleApiResponse(Response<NewsCallResponse> response) {
        MyLogger.d(TAG, "Anything received");
        if(response.isSuccessful() && response.body() != null){
            if(response.body().status.equals("ok") && response.body().totalResults < 1){
                MyLogger.d(TAG, "Response received from API successfully but empty, List Size is "+response.body().totalResults);
                requestStatusTypesMutableLiveData.setValue(RequestStatusTypes.EMPTY);
            }
            else if(response.body().status.equals("ok")) {
                MyLogger.d(TAG, "Response received from API successfully and setted to live data, List Size is "+response.body().totalResults);
                requestStatusTypesMutableLiveData.setValue(RequestStatusTypes.COMPLETED);
                responseLiveData.setValue(response.body());
            }
            else if(response.body().status.equals("error")){
                MyLogger.d(TAG, "Response received is either not successful or the body is null. Response: "+response.message());
                MyLogger.d(TAG, response.raw().toString());
                requestStatusTypesMutableLiveData.setValue(RequestStatusTypes.FAILED);
                responseLiveData.setValue(response.body());
            }
        }else{
            requestStatusTypesMutableLiveData.setValue(RequestStatusTypes.FAILED);
            responseLiveData.setValue(new NewsCallResponse(
                    "error",
                    0,
                    "Response Code = "+response.code()+ "   "+ response,
                    "Some Issue Occurred in Connecting to Server"));
        }
    }

    public MutableLiveData<NewsCallResponse> getNews(){
        MyLogger.d(TAG, "Enqueuing a  network request");
        requestStatusTypesMutableLiveData.postValue(RequestStatusTypes.PROCESSING);
        service.getNews("en").enqueue(new Callback<NewsCallResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsCallResponse> call, @NonNull Response<NewsCallResponse> response) {
               handleApiResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<NewsCallResponse> call, @NonNull Throwable t) {
               handleApiFailedResponse(t);
            }
        });
        return responseLiveData;
    }

    private void handleApiFailedResponse(Throwable t) {
        MyLogger.d(TAG, "Response from API failed, Error: "+t.toString());
        requestStatusTypesMutableLiveData.setValue(RequestStatusTypes.FAILED);
        responseLiveData.setValue(new NewsCallResponse(
                "error",
                0,
                t.toString(),
                t.getLocalizedMessage()
        ));
    }
}
