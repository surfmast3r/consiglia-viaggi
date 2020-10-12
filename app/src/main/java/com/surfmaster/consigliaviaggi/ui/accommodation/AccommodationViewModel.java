package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.controllers.ViewReviewController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AccommodationViewModel extends AndroidViewModel {

    private Context context;


    private MutableLiveData<String> mText;
    private MutableLiveData<String> mAccommodationName;
    private MutableLiveData<String> mAccommodationDescription;
    private MutableLiveData<String> mAccommodationCategory;
    private MutableLiveData<String> mAccommodationImage;
    private MutableLiveData<LatLng> mAccommodationLatLng;
    private MutableLiveData<Accommodation> mAccommodation;
    private MutableLiveData<Integer> mAccommodationId;
    private MutableLiveData<Float> mAccommodationRating;
    private ViewAccommodationsController viewAccommodationsController;
    private ViewReviewController viewReviewController;

    public AccommodationViewModel(Application application) {
        super(application);

        context=application;
        mAccommodationId= new MutableLiveData<>();
        mAccommodationRating= new MutableLiveData<>();
        mAccommodationLatLng= new MutableLiveData<>();
        mAccommodationCategory= new MutableLiveData<>();
        mAccommodationDescription = new MutableLiveData<>();
        mAccommodation=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationName = new MutableLiveData<>();
        mAccommodationImage = new MutableLiveData<>();
        mText.setValue("This is Accommodation fragment");

        viewAccommodationsController = new ViewAccommodationsController(application);
        viewReviewController = new ViewReviewController();
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
        return mAccommodationDescription;
    }

    public LiveData<LatLng> getAccommodationLatLng() {
        return mAccommodationLatLng;
    }

    public LiveData<Integer> getAccommodationId(){
        return mAccommodationId;
    }

    public LiveData<Float> getAccommodationRating() {
        return mAccommodationRating;
    }

    public void setAccommodation(final int accommodationId) {
        final int id=accommodationId;
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Accommodation ac = viewAccommodationsController.getAccommodationById(id);
                mAccommodationId.postValue(ac.getId());
                mAccommodationRating.postValue(ac.getRating());
                mAccommodation.postValue(ac);
                mAccommodationName.postValue(ac.getName());
                mAccommodationImage.postValue(ac.getImages().get(0));
                mAccommodationLatLng.postValue(new LatLng(ac.getLatitude(),ac.getLongitude()));
                mAccommodationDescription.postValue(ac.getDescription());
                mAccommodationCategory.postValue(ac.getSubcategory().toString());

            }
        });

    }

}