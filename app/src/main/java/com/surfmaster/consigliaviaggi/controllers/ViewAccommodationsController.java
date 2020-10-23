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
import com.surfmaster.consigliaviaggi.models.DAO.AccommodationDaoFactory;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DTO.JsonPageResponse;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;
import com.surfmaster.consigliaviaggi.ui.main.MainFragmentDirections;
import com.surfmaster.consigliaviaggi.ui.main.SelectCityFragment;

import java.util.List;

public class ViewAccommodationsController {

    private int pageNumber;
    private int totalPageNumber;
    private SearchParamsAccommodation currentSearchParams;
    private List<Accommodation> accommodationList;
    private AccommodationDao acDao;
    private Context context;
    private ManageUserController manageUserController;

    public ViewAccommodationsController(Context context){

        this.context=context;
        acDao= AccommodationDaoFactory.getAccommodationDao();
        manageUserController= new ManageUserController(context);
    }

    public List<Accommodation> getAccommodationList(SearchParamsAccommodation params){
        try {
            currentSearchParams=params;
            JsonPageResponse<Accommodation> jsonPageResponse=acDao.getAccommodationList(params);
            accommodationList=jsonPageResponse.getContent();
            pageNumber=jsonPageResponse.getPage();
            totalPageNumber=jsonPageResponse.getTotalPages();
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

    public void navigateToAccommodationListFragment( String category) {

        MainFragmentDirections.ActionNavHomeToNavAccommodationList action;

        if (!manageUserController.getSelectedCity().equals(context.getString(R.string.city_select))) {
            action = MainFragmentDirections.actionNavHomeToNavAccommodationList(category,manageUserController.getSelectedCity());
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
