package com.surfmaster.consigliaviaggi.controllers;

import com.surfmaster.consigliaviaggi.models.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;

import java.util.List;

public class ViewAccommodationsController {

    AccommodationDao acDao;

    public ViewAccommodationsController(){
        acDao= new AccommodationDaoStub();
    }

    public List getAccommodationList(){
        List accommodationList;

        accommodationList=acDao.getAccommodationList();
        return accommodationList;

    }
}
