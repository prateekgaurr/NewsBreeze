package com.prateek.newsbreeze.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.repository.SavedArticlesRepository;
import com.prateek.newsbreeze.util.MyLogger;
import com.prateek.newsbreeze.util.SearchUtils;

import java.util.List;

public class SavedArticlesViewModel extends ViewModel {
    private final SavedArticlesRepository repository;
    private LiveData<List<Article>> articles;
    private MutableLiveData<List<Article>> filteredArticles;
    private final String TAG = getClass().getSimpleName();

    public SavedArticlesViewModel(SavedArticlesRepository repository){
        this.repository = repository;
        this.articles = repository.getArticles();
        MyLogger.d(TAG, "Saved Articles Repository Initialized");
    }

    public LiveData<List<Article>> getAllArticles(){
        MyLogger.d(TAG, "Getting all articles without search terms");
        articles = repository.getArticles();
        return articles;
    }

    public MutableLiveData<List<Article>> getFilteredArticles(String search){
        MyLogger.d(TAG, "Getting all elements with search --> " + search);
        filteredArticles = new MutableLiveData<>();
        filteredArticles.postValue(SearchUtils.searchInArticlesList(search, articles));
        return filteredArticles;
    }


    public void deleteFromDb(int id){
        repository.deleteFromDb(id);
    }

    public void insertInDb(Article article){
        repository.insertInDb(article);
    }
}
