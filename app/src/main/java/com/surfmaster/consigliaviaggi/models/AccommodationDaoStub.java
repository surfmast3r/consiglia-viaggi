package com.surfmaster.consigliaviaggi.models;

import com.surfmaster.consigliaviaggi.Category;
import com.surfmaster.consigliaviaggi.Subcategory;

import java.util.ArrayList;
import java.util.Arrays;

public class AccommodationDaoStub implements AccommodationDao {
    private ArrayList<Accommodation> accommodationList;


    public AccommodationDaoStub(){
        accommodationList= new ArrayList<Accommodation>();

    }

    @Override
    public ArrayList<Accommodation> getAccommodationList(String city) {
        createStubData();
        return accommodationList;
    }

    @Override
    public ArrayList<Accommodation> getAccommodationList(Double latitude, Double longitude) {
        createStubData();
        return accommodationList;

    }

    @Override
    public Accommodation getAccommodationById(int id) {
        createStubData();
        for (Accommodation ac : accommodationList) {
            if (ac.getId().equals(id)) {
                return ac;
            }
        }
        return null;
    }

    private void createStubData(){
        accommodationList.add(new Accommodation.Builder()
                .setId(111)
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
        accommodationList.add(new Accommodation.Builder()
                .setId(222)
                .setName("Da Pasquale")
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

}
