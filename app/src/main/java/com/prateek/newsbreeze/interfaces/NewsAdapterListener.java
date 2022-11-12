package com.prateek.newsbreeze.interfaces;

import com.prateek.newsbreeze.models.Article;

import java.io.Serializable;

public interface NewsAdapterListener extends Serializable {
    void onNewsItemClicked(Article article);
    void onNewsSaveButtonClicked(Article article);
}