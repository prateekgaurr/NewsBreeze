package com.prateek.newsbreeze.util;

import com.prateek.newsbreeze.constants.SortingTypes;
import com.prateek.newsbreeze.models.Article;

import java.util.Comparator;
import java.util.List;

public class SortArticles {
    public static List<Article> sortArticles(List<Article> articles, SortingTypes sortingType){
        switch(sortingType){
            case SORT_BY_DATE:
                articles.sort(new Comparator<Article>() {
                    @Override
                    public int compare(Article article, Article t1) {
                        return article.publishedAt.compareTo(t1.publishedAt);
                    }
                });
                break;

            case SORT_BY_PUBLISHER:
                articles.sort((article, t1) -> {
                    if(article.source == null || article.source.name == null) article.source.name = "Unknown Source";
                    if(t1.source == null || t1.source.name == null) t1.source.name = "Unknown Source";
                    return article.source.name
                            .compareTo(t1.source.name);
                });
                break;
        }
        return articles;
    }
}
