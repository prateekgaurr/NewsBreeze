package com.prateek.newsbreeze.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prateek.newsbreeze.models.Article;

@androidx.room.Database(entities = {Article.class}, version = 1)
@TypeConverters({SourceConvertor.class, DateConvertor.class})
public abstract class ArticlesDatabase extends RoomDatabase {
    public abstract ArticlesDao dao();
    private static ArticlesDatabase INSTANCE;

    public static ArticlesDatabase getInstance(Context application) {
        if (INSTANCE == null)

            INSTANCE = Room.databaseBuilder(
                            application.getApplicationContext(),
                            ArticlesDatabase.class,
                            "articles_db")
                    .build();

        return INSTANCE;
    }

}
