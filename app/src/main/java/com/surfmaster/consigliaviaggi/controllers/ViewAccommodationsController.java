package com.surfmaster.consigliaviaggi.controllers;

import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;

import java.util.List;

public class ViewAccommodationsController {

    private List accommodationList;
    private AccommodationDao acDao;

    public ViewAccommodationsController(){
        acDao= new AccommodationDaoStub();
    }

    public List getAccommodationList(String city){
        accommodationList=acDao.getAccommodationList(city);
        return accommodationList;

    }

    public Accommodation getAccommodationById(int id){
        Accommodation accommodation;
        accommodation=acDao.getAccommodationById(id);
        return accommodation;

    }

    public int getAccommodationId(int position){
        Accommodation ac = (Accommodation) accommodationList.get(position);
        return ac.getId();
    }
}
