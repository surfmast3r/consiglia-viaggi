package com.surfmaster.consigliaviaggi.models.DAO;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.ArrayList;
import java.util.List;

public interface AccommodationDao {
     ArrayList<Accommodation> getAccommodationList(String city);
     List<Accommodation> getAccommodationList(LatLng latLng) throws DaoException;


     // GET
     JsonPageResponse<Accommodation> getAccommodationList(SearchParamsAccommodation params) throws DaoException;
     Accommodation getAccommodationById(int id) throws DaoException;
}
