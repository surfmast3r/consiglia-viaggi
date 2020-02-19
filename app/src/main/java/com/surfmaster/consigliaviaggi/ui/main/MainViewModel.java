package com.surfmaster.consigliaviaggi.ui.main;

import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mCity;


    public MainViewModel() {
        mCity= new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("Dove vuoi andare?");

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity.setValue(city);
    }
}