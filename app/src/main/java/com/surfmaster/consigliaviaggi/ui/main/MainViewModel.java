package com.surfmaster.consigliaviaggi.ui.main;

import android.app.Application;

import com.surfmaster.consigliaviaggi.controllers.ManageUserController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public void setCity(final String city, final Double latitude, final Double longitude) {
        mCity.setValue(city);
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                manageUserController.updateSelectedCity(city, latitude, longitude);
            }
        });

    }

    public void resetCity() {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                mCity.postValue(manageUserController.resetSelectedCity());
            }
        });

    }


}