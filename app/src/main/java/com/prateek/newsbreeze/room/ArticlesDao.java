package com.prateek.newsbreeze.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prateek.newsbreeze.models.Article;

import java.util.List;

@Dao
public interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInDb(Article article);

    @Query("SELECT * from saved_articles ORDER BY date")
    LiveData<List<Article>> getAllSavedArticles();

    @Query("DELETE FROM saved_articles WHERE id=:id")
    void deleteFromDb(int id);

}
