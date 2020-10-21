package com.surfmaster.consigliaviaggi.ui.main;

import android.app.Application;

import com.surfmaster.consigliaviaggi.controllers.ManageUserController;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private ManageUserController manageUserController;
    private MutableLiveData<String> mText;
    private MutableLiveData<String> mCity;


    public MainViewModel(Application application) {
        super(application);
        mCity= new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("Dove vuoi andare?");

        manageUserController= new ManageUserController(application);
        mCity.setValue(manageUserController.getSelectedCity());

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getCity() {
        return mCity;
    }

    public void setCity(String city,Double latitude,Double longitude) {
        mCity.setValue(city);
        manageUserController.updateSelectedCity(city, latitude, longitude);
    }

    public void resetCity() {
        mCity.setValue(manageUserController.resetSelectedCity());
    }
}