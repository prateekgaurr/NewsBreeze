package com.prateek.newsbreeze.repository;

import androidx.lifecycle.LiveData;

import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.room.ArticlesDao;

import java.util.List;

public class SavedArticlesRepository {
    private final ArticlesDao dao;
    private LiveData<List<Article>> articles;

    public SavedArticlesRepository(ArticlesDao dao){
        this.dao=dao;
        articles = dao.getAllSavedArticles();
    }

   public void deleteFromDb(int id){
        dao.deleteFromDb(id);
    }

    public void insertInDb(Article article){
        dao.insertInDb(article);
    }

    public LiveData<List<Article>> getArticles(){
        articles = dao.getAllSavedArticles();
        return articles;
    }


}
