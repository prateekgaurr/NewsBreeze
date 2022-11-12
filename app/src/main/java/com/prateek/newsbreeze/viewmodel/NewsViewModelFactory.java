package com.prateek.newsbreeze.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.prateek.newsbreeze.repository.NewsRepository;


public class NewsViewModelFactory implements ViewModelProvider.Factory {

    NewsRepository repository;

    public NewsViewModelFactory(NewsRepository repository){
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       return (T) new NewsViewModel(repository);
    }
}
