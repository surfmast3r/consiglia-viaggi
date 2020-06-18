package com.surfmaster.consigliaviaggi;

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
