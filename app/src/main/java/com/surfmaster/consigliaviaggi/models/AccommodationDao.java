package com.surfmaster.consigliaviaggi.models;

import java.util.ArrayList;

public interface AccommodationDao {
     ArrayList<Accommodation> getAccommodationList();
     Accommodation getAccommodationById(int id);
}
