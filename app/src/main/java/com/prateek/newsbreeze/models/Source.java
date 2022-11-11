package com.prateek.newsbreeze.models;

import androidx.annotation.NonNull;

public class Source {
    public String id;
    public String name;

    public Source(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }


}
