package com.surfmaster.consigliaviaggi;

public final class Constants  {

    public static final String EMPTY_STRING = "";
    public static final String DEFAULT = "default";
    public static final String BEST_RATING = "best";
    public static final String WORST_RATING = "worst";
    public static final String CATEGORY_HOTEL = "Hotel";
    public static final String CATEGORY_RESTAURANT = "Ristoranti";
    public static final String CATEGORY_ATTRACTION = "Attrazioni";
    public static final int ASCENDING=1;
    public static final int DESCENDING=2;
    public static final float DEFAULT_MIN_RATING=0;
    public static final float DEFAULT_MAX_RATING=5;
    public static final int DEFAULT_ORDER = 0;
    public static final int BEST_RATING_ORDER = 1;
    public static final int WORST_RATING_ORDER = 2;
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final String ID = "id";
    public static final String RATING = "rating";

    /*JSON URLs*/
    public static final String SERVER_URL="http://10.0.2.2:5000/";
    public static final String LOGIN_URL = SERVER_URL+"authenticate";
    public static final String REGISTER = SERVER_URL+"register";
    public static final String PAGE_PARAM ="page=";
    //Accommodation
    public static final String GET_ACCOMMODATION_LIST_URL = SERVER_URL+"accommodation";
    public static final String QUERY_PARAM ="query=";
    public static final String CATEGORY_PARAM ="category=";
    public static final String SUBCATEGORY_PARAM ="subCategory=";
    //Review
    public static final String GET_REVIEW_LIST_URL = SERVER_URL+"review_view";
    public static final String REVIEW_ID_PARAM ="reviewId=";
    public static final String STATUS_PARAM ="status=";
    public static final String ACCOMMODATION_ID_PARAM ="accommodationId=";
    public static final String CONTENT_PARAM ="content=" ;
    public static final String ACCOMMODATION_NAME_PARAM ="accommodationName=" ;
    public static final String GET_REVIEW_URL=SERVER_URL+"single_review_view";
    public static final String ORDER_BY_PARAM ="orderBy=" ;
    public static final String MIN_RATING ="minRating=" ;
    public static final String MAX_RATING ="maxRating=" ;
    public static final String DIRECTION_PARAM ="direction=" ;
    public static final String CREATE_REVIEW_URL = SERVER_URL+"/review/create";

    public static final String PREFERENCES="SharedPreferences";
    public static final String LATITUDE="currentLat",LONGITUDE="currentLong";
    public static final String CITY="SelectedCity";
    public static final String USER = "USER" ;
    public static final String PWD = "PWD";
    public static final String TOKEN ="TOKEN" ;

    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
