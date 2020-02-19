package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;

import java.util.Arrays;
import java.util.List;

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

    public int getAccommodationId(int position){
        Accommodation ac = (Accommodation) accommodationList.get(position);
        return ac.getId();
    }

    public boolean cityIsValid (Context context, String text){
        List<String> cities = Arrays.asList(context.getResources().getStringArray(R.array.cities_array));

        //some logic here returns true or false based on if the text is validated
        for(int i = 0; i < cities.size(); i++) {
            String temp = cities.get(i);
            if(temp.equals(text)) {
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
}
