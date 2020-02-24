package com.surfmaster.consigliaviaggi.models;

import com.google.android.gms.maps.model.LatLng;
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
    public ArrayList<Accommodation> getAccommodationList(LatLng latLng) {
        LatLng  mDefaultLocation = new LatLng(45.463619, 9.188120);
        if (latLng.equals(mDefaultLocation))
            createStubDataMilano();
        else
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

        for(int i=0 ;i<20;i++){
            double lat = 40.857362 + Math.random() * (40.857362 - 40.870000);

            double longitude = 14.261627 + Math.random() * (14.261627 - 14.300000);
            accommodationList.add(new Accommodation.Builder()
                    .setId(i)
                    .setName("Da Peppino"+i)
                    .setDescription("Descrizione ristorante da Peppino "+i)
                    .setCategory(Category.RESTAURANT)
                    .setSubcategory(Subcategory.PIZZERIA)
                    .setAccommodationLocation(new Location.Builder()
                            .setCity("Napoli")
                            .setAddress("Via Bernardo Cavallino 27")
                            .setLatitude(lat)
                            .setLongitude(longitude)
                            .build())
                    .setImages(new ArrayList<String>(Arrays.asList("https://www.oasidellapizza.it/wp-content/uploads/revslider/steweysfullslider/5.jpg")))
                    .setRating(3)
                    .create());
        }

    }

    private void createStubDataMilano(){
        accommodationList.add(new Accommodation.Builder()
                .setId(111)
                .setName("Da Peppino")
                .setCategory(Category.RESTAURANT)
                .setSubcategory(Subcategory.PIZZERIA)
                .setAccommodationLocation(new Location.Builder()
                        .setCity("Milano")
                        .setAddress("Via Bernardo Cavallino 27")
                        .setLatitude(45.463619)
                        .setLongitude(9.188120)
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
                        .setCity("Milano")
                        .setAddress("Via Bernardo Cavallino 27")
                        .setLatitude(45.463621)
                        .setLongitude(9.188121)
                        .build())
                .setImages(new ArrayList<String>(Arrays.asList("https://www.oasidellapizza.it/wp-content/uploads/revslider/steweysfullslider/5.jpg")))
                .setRating(3)
                .create());

    }
}
