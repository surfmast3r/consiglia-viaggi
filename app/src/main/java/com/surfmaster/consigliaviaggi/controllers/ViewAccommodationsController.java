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

    private static final String LATITUDE="currentLat",LONGITUDE="currentLong";
    private static final String CITY="SelectedCity";
    private static final String PREFERENCES="SharedPreferences";
    private int pageNumber;
    private int totalPageNumber;
    private int totalElementNumber;
    private SearchParamsAccommodation currentSearchParams;
    private List<Accommodation> accommodationList;
    private AccommodationDao acDao;

    public ViewAccommodationsController(){
        acDao= new AccommodationDaoJSON();
    }

    public List<Accommodation> getAccommodationList(String city){
        accommodationList=acDao.getAccommodationList(city);
        return accommodationList;

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

    public boolean cityIsValid (Context context, String cityName){
        List<String> cities = Arrays.asList(context.getResources().getStringArray(R.array.cities_array));

        //some logic here returns true or false based on if the text is validated
        for(int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            if(city.equals(cityName)) {
                return true;
            }
        }
        return false;
    }

    public String getSelectedCity(Context context) {

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES, 0);
        if (pref.contains(CITY)) {
            return pref.getString(CITY,"");
        }
        else
        return context.getString(R.string.city_select);
    }

    public void updateSelectedCity(Context context, String city, Double lat, Double lon) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CITY,city);
        editor.putLong(LATITUDE, Double.doubleToRawLongBits(lat));
        editor.putLong(LONGITUDE, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    public String resetSelectedCity(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(CITY);
        editor.remove(LATITUDE);
        editor.remove(LONGITUDE);
        editor.apply();
        return context.getString(R.string.city_select);
    }

    public void navigateToAccommodationListFragment(Context context, String category) {

        MainFragmentDirections.ActionNavHomeToNavAccommodationList action;

        if (!getSelectedCity(context).equals(context.getString(R.string.city_select))) {
            action = MainFragmentDirections.actionNavHomeToNavAccommodationList(category,getSelectedCity(context));
            Navigation.findNavController((AppCompatActivity) context, R.id.nav_host_fragment).navigate(action);
        }
        else {
            navigateToSelectCityFragment(context);
        }
    }

    public void navigateToSelectCityFragment(Context context) {
        AppCompatActivity activity=(AppCompatActivity)context;
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = SelectCityFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    public void navigateToAccommodationMapFragment(Context context) {
        Navigation.findNavController((AppCompatActivity) context, R.id.nav_host_fragment).navigate(MainFragmentDirections.actionNavHomeToNavMap());

    }

    public List<Accommodation> orderAccommodationListByRating(List<Accommodation> accommodationList, int order) {
        if(order== Constants.ASCENDING)
            Collections.sort(accommodationList);
        else if (order == Constants.DESCENDING)
            Collections.sort(accommodationList,Collections.reverseOrder());
        return accommodationList;
    }

    public List<Accommodation> copyList(List<Accommodation> acList) {
        return new ArrayList<>(acList);
    }

    public List<Accommodation> filterAccommodationList(List<Accommodation> accommodationList, float minRating, float maxRating) {

        List<Accommodation> filteredList = new ArrayList<>();
        for(Accommodation accommodation : accommodationList){
            if(accommodation.getRating()>minRating&&accommodation.getRating()<maxRating)
                filteredList.add(accommodation);
        }
        return filteredList;
    }
}
