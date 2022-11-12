package com.prateek.newsbreeze.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.prateek.newsbreeze.room.DateConvertor;
import com.prateek.newsbreeze.room.SourceConvertor;
import java.util.Date;

@Entity(tableName = "saved_articles", indices = {@Index(value = "title", unique = true)})
public class Article implements Comparable<Article>{

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "source")
    @TypeConverters(SourceConvertor.class)
    public Source source;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "is_already_saved")
    public boolean isAlreadySaved;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "desc")
    public String description;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "image_url")
    public String urlToImage;

    @ColumnInfo(name = "date")
    @TypeConverters(DateConvertor.class)
    public Date publishedAt;

    @ColumnInfo(name = "content")
    public String content;

    public Article() {
    }

    public Article(Source source, String author, String title, String description, String urlToImage, Date publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    @Override
    public int compareTo(Article article) {
        return this.publishedAt.compareTo(article.publishedAt);
    }
}
