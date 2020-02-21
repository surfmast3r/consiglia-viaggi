package com.surfmaster.consigliaviaggi.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface AccommodationDao {
     ArrayList<Accommodation> getAccommodationList(String city);
     ArrayList<Accommodation> getAccommodationList(LatLng latLng);
     Accommodation getAccommodationById(int id);
}
