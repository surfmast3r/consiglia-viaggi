package com.surfmaster.consigliaviaggi.models.DAO;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.ArrayList;

public interface AccommodationDao {
     ArrayList<Accommodation> getAccommodationList(String city);
     ArrayList<Accommodation> getAccommodationList(LatLng latLng);
     Accommodation getAccommodationById(int id);

     // GET
     JsonPageResponse<Accommodation> getAccommodationList(SearchParamsAccommodation params) throws DaoException;
}
