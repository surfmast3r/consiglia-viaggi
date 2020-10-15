package com.surfmaster.consigliaviaggi.models.DAO;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.CategoryEnum;
import com.surfmaster.consigliaviaggi.Subcategory;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.Location;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccommodationDaoStub implements AccommodationDao {
    private ArrayList<Accommodation> accommodationList;


    public AccommodationDaoStub(){
        accommodationList= new ArrayList<Accommodation>();

    }

    @Override
    public ArrayList<Accommodation> getAccommodationList(String city) {
        accommodationList.clear();
        createStubData();
        return accommodationList;
    }

    @Override
    public List<Accommodation> getAccommodationList(LatLng latLng) {
        accommodationList.clear();
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

    @Override
    public JsonPageResponse<Accommodation> getAccommodationList(SearchParamsAccommodation params) throws DaoException {
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
                    .setCategory(CategoryEnum.RESTAURANT)
                    .setSubcategory(Subcategory.PIZZERIA)
                    .setAccommodationLocation(new Location.Builder()
                            .setCity("Napoli")
                            .setAddress("Via Bernardo Cavallino "+i+27)
                            .setLatitude(lat)
                            .setLongitude(longitude)
                            .build())
                    .setImages(new ArrayList<String>(Arrays.asList("https://www.oasidellapizza.it/wp-content/uploads/revslider/steweysfullslider/5.jpg")))
                    .setRating((float) (1 + Math.random() * (5 - 1)))
                    .create());
        }

    }

    private void createStubDataMilano(){
        accommodationList.add(new Accommodation.Builder()
                .setId(111)
                .setName("Da Peppino")
                .setCategory(CategoryEnum.RESTAURANT)
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
                .setCategory(CategoryEnum.RESTAURANT)
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
