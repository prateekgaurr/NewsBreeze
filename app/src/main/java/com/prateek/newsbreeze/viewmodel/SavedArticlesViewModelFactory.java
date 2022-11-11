package com.prateek.newsbreeze.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.prateek.newsbreeze.repository.NewsRepository;
import com.prateek.newsbreeze.repository.SavedArticlesRepository;

public class SavedArticlesViewModelFactory implements ViewModelProvider.Factory {

    SavedArticlesRepository repository;

    public SavedArticlesViewModelFactory(SavedArticlesRepository repository){
        this.repository = repository;
    };

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SavedArticlesViewModel(repository);
    }
}
