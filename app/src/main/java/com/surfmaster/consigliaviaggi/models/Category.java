package com.surfmaster.consigliaviaggi.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String categoryName, categoryLabel;

    public Category(String name, String label){
        categoryLabel=label;
        categoryName=name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }
}
