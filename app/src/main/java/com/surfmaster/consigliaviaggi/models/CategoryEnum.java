package com.surfmaster.consigliaviaggi.models;

public enum CategoryEnum {
    RESTAURANT("Restaurant"),
    ATTRACTION("Attraction"),
    HOTEL("Hotel");

    public final String label;

    CategoryEnum(final String label){
        this.label=label;
    }

    public String getCategoryName(){
        return label;
    }
}
