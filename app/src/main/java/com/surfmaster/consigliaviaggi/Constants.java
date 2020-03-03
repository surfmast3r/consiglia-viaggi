package com.surfmaster.consigliaviaggi;

public final class Constants  {

    public static final String EMPTY_STRING = "";
    public static final String DEFAULT = "default";
    public static final String BEST_RATING = "best";
    public static final String WORST_RATING = "worst";
    public static final String CATEGORY_HOTEL = "-HOTEL-";
    public static final String CATEGORY_RESTAURANT = "-RISTORANTI-";
    public static final String CATEGORY_ATTRACTION = "-ATTRAZIONI-";
    public static final int ASCENDING=1;
    public static final int DESCENDING=2;
    public static final float DEFAULT_MIN_RATING=0;
    public static final float DEFAULT_MAX_RATING=5;


    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
