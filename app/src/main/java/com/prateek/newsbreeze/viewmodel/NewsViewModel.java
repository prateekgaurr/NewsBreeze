package com.prateek.newsbreeze.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prateek.newsbreeze.models.NewsCallResponse;
import com.prateek.newsbreeze.constants.RequestStatusTypes;
import com.prateek.newsbreeze.repository.NewsRepository;
import com.prateek.newsbreeze.util.MyLogger;

public class NewsViewModel extends ViewModel {

   private final NewsRepository repository;
   private final MutableLiveData<NewsCallResponse> responseLiveData;
   private final MutableLiveData<RequestStatusTypes> requestStatus;

    public NewsViewModel(NewsRepository repository){
        String TAG = getClass().getSimpleName();
        MyLogger.d(TAG, "News Main View Model Initialized");
        this.repository = repository;
        responseLiveData = repository.getNews();
        requestStatus = repository.getRequestStatus();
    }

    public void getNews(String s){
        responseLiveData.postValue(repository.getNews(s).getValue());
    }

    public LiveData<NewsCallResponse> getNews(){
        responseLiveData.postValue(repository.getNews().getValue());
        return responseLiveData;
    }

    public LiveData<RequestStatusTypes> getRequestStatus(){
        return requestStatus;
    }



}
