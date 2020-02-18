package com.surfmaster.consigliaviaggi.models;

import java.util.ArrayList;

public interface AccommodationDao {
     ArrayList<Accommodation> getAccommodationList(String city);
     ArrayList<Accommodation> getAccommodationList(Double latitude,Double longitude);
     Accommodation getAccommodationById(int id);
}
