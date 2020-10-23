package com.surfmaster.consigliaviaggi.models.DAO;

public class AccommodationDaoFactory{

    public static AccommodationDao getAccommodationDao(){

        /*Logica per scegliere il dao*/
        return new AccommodationDaoJSON();

    }
}
