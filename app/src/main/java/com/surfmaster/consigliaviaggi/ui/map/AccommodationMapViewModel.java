package com.surfmaster.consigliaviaggi.ui.map;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.controllers.ManageUserController;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccommodationMapViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Accommodation>> mAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;
    private ManageUserController manageUserController;

    public AccommodationMapViewModel(Application application) {
        super(application);
        viewAccommodationsController = new ViewAccommodationsController(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
        mAccommodationList=new MutableLiveData<>();
        manageUserController= new ManageUserController(application);
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

                List<Accommodation> acList = viewAccommodationsController.getAccommodationList(latLng);
                mAccommodationList.postValue(acList);

            }
        });
    }

    public LatLng getDefLocation() {
        Double lat = manageUserController.getSelectedCityLatitude();
        Double lon = manageUserController.getSelectedCityLongitude();
        return new LatLng(lat,lon);
    }
}