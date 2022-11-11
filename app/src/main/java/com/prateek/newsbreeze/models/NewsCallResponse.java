package com.prateek.newsbreeze.models;

import java.util.ArrayList;

public class NewsCallResponse {
    public String status;
    public int totalResults;
    public ArrayList<Article> articles;
    public String message;
    public String code;

    public NewsCallResponse(String status, int totalResults, String message, String code) {
        this.status = status;
        this.totalResults = totalResults;
        this.message = message;
        this.code = code;
    }
}
