package com.surfmaster.consigliaviaggi.ui.map;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationMapViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Accommodation>> mAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationMapViewModel(Application application) {
        super(application);
        viewAccommodationsController = new ViewAccommodationsController(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
        mAccommodationList=new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Accommodation>> getAccommodationList(){
        return mAccommodationList;
    }

    public void setAccommodationList(final LatLng latLng) {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List acList = viewAccommodationsController.getAccommodationList(latLng);
                        mAccommodationList.postValue(acList);
                    }
                },3000);


            }
        });
    }
}