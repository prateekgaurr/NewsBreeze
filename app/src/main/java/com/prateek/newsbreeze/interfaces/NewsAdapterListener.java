package com.prateek.newsbreeze.interfaces;

import android.widget.Button;

import com.prateek.newsbreeze.models.Article;

import java.io.Serializable;

public interface NewsAdapterListener extends Serializable {
    void onNewsItemClicked(Article article);
    void onNewsSaveButtonClicked(Article article, Button button);
}