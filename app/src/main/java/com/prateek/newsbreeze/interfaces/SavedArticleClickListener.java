package com.prateek.newsbreeze.interfaces;

import com.prateek.newsbreeze.models.Article;

public interface SavedArticleClickListener {
    void onSavedArticleClicked(Article article);
    void onSavedArticleLongClick(int articleId);
}
