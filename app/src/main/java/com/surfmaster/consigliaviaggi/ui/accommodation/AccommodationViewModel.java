package com.surfmaster.consigliaviaggi.ui.accommodation;

import com.google.android.gms.maps.model.LatLng;
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
    private MutableLiveData<String> mAccommodationDescritpion;
    private MutableLiveData<String> mAccommodationCategory;
    private MutableLiveData<String> mAccommodationImage;
    private MutableLiveData<LatLng> mAccommodationLatLng;
    private MutableLiveData<Accommodation> mAccommodation;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationViewModel() {

        mAccommodationLatLng= new MutableLiveData<>();
        mAccommodationCategory= new MutableLiveData<>();
        mAccommodationDescritpion= new MutableLiveData<>();
        mAccommodation=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationName = new MutableLiveData<>();
        mAccommodationImage = new MutableLiveData<>();
        mText.setValue("This is Accommodation fragment");
        viewAccommodationsController = new ViewAccommodationsController();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getAccommodationName() {
        return mAccommodationName;
    }

    public LiveData<String> getAccommodationCategory() {
        return mAccommodationCategory;
    }

    public LiveData<String> getAccommodationImage() {
        return mAccommodationImage;
    }

    public LiveData<String> getAccommodationDescription() {
        return mAccommodationDescritpion;
    }

    public LiveData<LatLng> getAccommodationLatLng() {
        return mAccommodationLatLng;
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
                        Accommodation ac = viewAccommodationsController.getAccommodationById(id);
                        mAccommodation.postValue(ac);
                        mAccommodationName.postValue(ac.getName());
                        mAccommodationImage.postValue(ac.getImages().get(0));
                        mAccommodationLatLng.postValue(new LatLng(ac.getLatitude(),ac.getLongitude()));
                        mAccommodationDescritpion.postValue(ac.getDescription());
                        mAccommodationCategory.postValue(ac.getSubcategory().toString());
                    }
                },3000);

            }
        });

    }
}