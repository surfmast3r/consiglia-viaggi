package com.surfmaster.consigliaviaggi.controllers;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;
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

    private static final String CITY="SelectedCity";
    private static final String PREFERENCES="SharedPreferences";

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

    public List getAccommodationList(LatLng latLng){
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

    public void updateSelectedCity(Context context, String city) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CITY,city);
        editor.apply();
    }

    public String resetSelectedCity(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(CITY);
        editor.apply();
        return context.getString(R.string.city_select);
    }

    public void navigateToAccommodationListFragment(Context context, String category) {

        MainFragmentDirections.ActionNavHomeToNavAccommodationList action;

        if (!getSelectedCity(context).equals(context.getString(R.string.city_select))) {
            action = MainFragmentDirections.actionNavHomeToNavAccommodationList(category);
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

    public void navigateToAccommodationMapFragment(Context context, int nav_host_fragment) {
        Navigation.findNavController((AppCompatActivity) context, R.id.nav_host_fragment).navigate(MainFragmentDirections.actionNavHomeToNavMap());

    }

    public List orderAccommodationListByRating(List accommodationList, int order) {
        if(order== Constants.ASCENDING)
            Collections.sort(accommodationList);
        else if (order == Constants.DESCENDING)
            Collections.sort(accommodationList,Collections.reverseOrder());
        return accommodationList;
    }

    public List copyList(List acList) {

        List copyList= new ArrayList();
        for(Accommodation ac : (ArrayList<Accommodation>) acList){
            copyList.add(ac);
        }

        return copyList;
    }

    public List filterAccommodationList(List<Accommodation> accommodationList, float minRating, float maxRating) {

        List filteredList = new ArrayList();
        for(Accommodation review : accommodationList){
            if(review.getRating()>minRating&&review.getRating()<maxRating)
                filteredList.add(review);
        }
        return filteredList;
    }
}
