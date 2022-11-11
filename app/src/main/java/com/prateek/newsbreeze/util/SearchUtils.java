package com.prateek.newsbreeze.util;

import androidx.lifecycle.LiveData;

import com.prateek.newsbreeze.models.Article;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {

    public static List<Article> searchInArticlesList(String search, LiveData<List<Article>> unfiltered){
        List<Article> filtered = new ArrayList<>();
        if(unfiltered != null && unfiltered.getValue() != null){
            for(Article article : unfiltered.getValue()){
                if(article.title.toLowerCase().contains(search.trim().toLowerCase()))
                    filtered.add(article);
            }
        }
        return filtered;
    }
}
