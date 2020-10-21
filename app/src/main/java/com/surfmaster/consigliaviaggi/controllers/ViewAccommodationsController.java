package com.surfmaster.consigliaviaggi.controllers;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.model.LatLng;

import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.DAO.AccommodationDao;
import com.surfmaster.consigliaviaggi.models.DAO.AccommodationDaoJSON;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DAO.UserDao;
import com.surfmaster.consigliaviaggi.models.DAO.UserDaoSharedPrefs;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;
import com.surfmaster.consigliaviaggi.ui.main.MainFragmentDirections;
import com.surfmaster.consigliaviaggi.ui.main.SelectCityFragment;

import java.util.List;

public class ViewAccommodationsController {

    private int pageNumber;
    private int totalPageNumber;
    //private int totalElementNumber;
    private SearchParamsAccommodation currentSearchParams;
    private List<Accommodation> accommodationList;
    private AccommodationDao acDao;
    private Context context;
    private UserDao userDao;

    public ViewAccommodationsController(Context context){

        this.context=context;
        acDao= new AccommodationDaoJSON();
        userDao = new UserDaoSharedPrefs(context);
    }

    public List<Accommodation> getAccommodationList(SearchParamsAccommodation params){
        try {
            currentSearchParams=params;
            JsonPageResponse<Accommodation> jsonPageResponse=acDao.getAccommodationList(params);
            accommodationList=jsonPageResponse.getContent();
            pageNumber=jsonPageResponse.getPage();
            totalPageNumber=jsonPageResponse.getTotalPages();
            //totalElementNumber=jsonPageResponse.getTotalElements();
        } catch (DaoException e) {
            postToastMessage(e.getMessage());
            return null;
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
        Accommodation accommodation;
        try {
            accommodation=acDao.getAccommodationById(id);
        } catch (DaoException e) {
            postToastMessage(e.getMessage());
            return null;
        }
        return accommodation;

    }

    public List<Accommodation> getAccommodationList(LatLng latLng){
        try {
            accommodationList  =  acDao.getAccommodationList(latLng);
        } catch (DaoException e) {
            postToastMessage(e.getMessage());
        }
        return accommodationList;
    }

    public String getSelectedCity() {

        String city = userDao.getSelectedCity();
        if(city.length()>0)
            return city;
        return context.getString(R.string.city_select);
    }

    public void updateSelectedCity(String city, Double lat, Double lon) {
        if(lat!=null&&lon!=null) {
            userDao.updateSelectedCity(city,lat,lon);
        }
    }

    public String resetSelectedCity() {
        userDao.resetSelectedCity();
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

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }


}
