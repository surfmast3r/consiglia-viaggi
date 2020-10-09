package com.surfmaster.consigliaviaggi.controllers;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DAO.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.DAO.AccommodationDaoJSON;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;
import com.surfmaster.consigliaviaggi.ui.main.MainFragmentDirections;
import com.surfmaster.consigliaviaggi.ui.main.SelectCityFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

public class ViewAccommodationsController {

    private int pageNumber;
    private int totalPageNumber;
    private int totalElementNumber;
    private SearchParamsAccommodation currentSearchParams;
    private List<Accommodation> accommodationList;
    private AccommodationDao acDao;
    private Context context;

    public ViewAccommodationsController(Context context){

        this.context=context;
        acDao= new AccommodationDaoJSON();
    }

    public List<Accommodation> getAccommodationList(SearchParamsAccommodation params){
        try {
            currentSearchParams=params;
            JsonPageResponse<Accommodation> jsonPageResponse=acDao.getAccommodationList(params);
            accommodationList=jsonPageResponse.getContent();
            pageNumber=jsonPageResponse.getPage();
            totalPageNumber=jsonPageResponse.getTotalPages();
            totalElementNumber=jsonPageResponse.getTotalElements();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return accommodationList;

    }

    public List<Accommodation> nextPage() {
        if (pageNumber+1<totalPageNumber) {
            currentSearchParams.setCurrentPage(pageNumber+1);
            return getAccommodationList(currentSearchParams);
        }
        else return null;
    }
    public Accommodation getAccommodationById(int id){
        Accommodation accommodation = null;
        try {
            accommodation=acDao.getAccommodationById(id);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return accommodation;

    }

    public List<Accommodation> getAccommodationList(LatLng latLng){
        accommodationList  =  acDao.getAccommodationList(latLng);
        return accommodationList;
    }

    public String getSelectedCity() {

        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        if (pref.contains(Constants.CITY)) {
            return pref.getString(Constants.CITY,"");
        }
        else
        return context.getString(R.string.city_select);
    }

    public void updateSelectedCity(String city, Double lat, Double lon) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.CITY,city);
        editor.putLong(Constants.LATITUDE, Double.doubleToRawLongBits(lat));
        editor.putLong(Constants.LONGITUDE, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    public String resetSelectedCity() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constants.CITY);
        editor.remove(Constants.LATITUDE);
        editor.remove(Constants.LONGITUDE);
        editor.apply();
        return context.getString(R.string.city_select);
    }

    public void navigateToAccommodationListFragment( String category) {

        MainFragmentDirections.ActionNavHomeToNavAccommodationList action;

        if (!getSelectedCity().equals(context.getString(R.string.city_select))) {
            action = MainFragmentDirections.actionNavHomeToNavAccommodationList(category,getSelectedCity());
            Navigation.findNavController((AppCompatActivity) context, R.id.nav_host_fragment).navigate(action);
        }
        else {
            navigateToSelectCityFragment();
        }
    }

    public void navigateToSelectCityFragment() {
        AppCompatActivity activity=(AppCompatActivity)context;
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = SelectCityFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    public void navigateToAccommodationMapFragment() {
        Navigation.findNavController((AppCompatActivity) context, R.id.nav_host_fragment).navigate(MainFragmentDirections.actionNavHomeToNavMap());

    }


}
