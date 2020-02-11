package com.surfmaster.consigliaviaggi.models;

import com.surfmaster.consigliaviaggi.Category;
import com.surfmaster.consigliaviaggi.Subcategory;

import java.util.ArrayList;
import java.util.Arrays;

public class AccommodationDaoStub implements AccommodationDao {

    private ArrayList<Accommodation> accommodationList;

    public AccommodationDaoStub(){
        accommodationList= new ArrayList<Accommodation>();
        accommodationList.add(new Accommodation.Builder()
                .setName("Da Peppino")
                .setCategory(Category.RESTAURANT)
                .setSubcategory(Subcategory.PIZZERIA)
                .setAccommodationLocation(new Location.Builder()
                        .setCity("Napoli")
                        .setAddress("Via Bernardo Cavallino 27")
                        .setLatitude(40.858560)
                        .setLongitude(14.230360)
                        .build())
                .setImages(new ArrayList<String>(Arrays.asList("https://www.oasidellapizza.it/wp-content/uploads/revslider/steweysfullslider/5.jpg")))
                .setRating(3)
                .create());
    }
    @Override
    public ArrayList<Accommodation> getAccommodationList() {
        return accommodationList;
    }
}
