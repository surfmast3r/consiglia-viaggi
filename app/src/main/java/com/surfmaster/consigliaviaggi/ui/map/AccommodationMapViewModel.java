package com.surfmaster.consigliaviaggi.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationMapViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Accommodation>> mAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationMapViewModel() {
        viewAccommodationsController = new ViewAccommodationsController();
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



                // on background thread, obtain a fresh list accommodations
                //List acList = viewAccommodationsController.getAccommodationList();

                // make it available to outside observers of the "mAccommodationList"
                // MutableLiveData object
                //mAccommodationList.postValue(acList);

            }
        });
    }
}