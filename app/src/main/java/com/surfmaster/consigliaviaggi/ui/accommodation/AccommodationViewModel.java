package com.surfmaster.consigliaviaggi.ui.accommodation;

import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mAccommodationName;
    private MutableLiveData<String> mAccommodationImage;
    private MutableLiveData<Accommodation> mAccommodation;
    private ViewAccommodationsController va_controller;

    public AccommodationViewModel() {

        mAccommodation=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationName = new MutableLiveData();
        mAccommodationImage = new MutableLiveData<>();
        mText.setValue("This is Accommodation fragment");
        va_controller= new ViewAccommodationsController();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getAccommodationName() {
        return mAccommodationName;
    }

    public LiveData<String> getAccommodationImage() {
        return mAccommodationImage;
    }

    public void setAccommodation(int accommodationId) {
        final int id=accommodationId;
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Accommodation ac =va_controller.getAccommodationById(id);
                        mAccommodation.postValue(ac);
                        mAccommodationName.postValue(ac.getName());
                        mAccommodationImage.postValue(ac.getImages().get(0));
                    }
                },3000);

            }
        });

    }
}