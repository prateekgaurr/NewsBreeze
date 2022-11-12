package com.prateek.newsbreeze;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.room.ArticlesDao;
import com.prateek.newsbreeze.room.DateConvertor;
import com.prateek.newsbreeze.room.SourceConvertor;

@androidx.room.Database(entities = {Article.class}, version = 1)
@TypeConverters({SourceConvertor.class, DateConvertor.class})
public abstract class TestDatabase extends RoomDatabase {
    public abstract ArticlesDao dao();
    private static TestDatabase INSTANCE;

    public static TestDatabase getInstance(Context application) {
        if (INSTANCE == null)

            INSTANCE = Room.databaseBuilder(
                            application.getApplicationContext(),
                            TestDatabase.class,
                            "articles_test_db")
                    .build();

        return INSTANCE;
    }

}
