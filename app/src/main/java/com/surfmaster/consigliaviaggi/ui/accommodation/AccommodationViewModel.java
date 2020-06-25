package com.surfmaster.consigliaviaggi.ui.accommodation;

import com.google.android.gms.maps.model.LatLng;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.controllers.ViewReviewController;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {

    private static final int NUM_REVIEW=3;
    private MutableLiveData<String> mText;
    private MutableLiveData<String> mAccommodationName;
    private MutableLiveData<String> mAccommodationDescritpion;
    private MutableLiveData<String> mAccommodationCategory;
    private MutableLiveData<String> mAccommodationImage;
    private MutableLiveData<LatLng> mAccommodationLatLng;
    private MutableLiveData<Accommodation> mAccommodation;
    private MutableLiveData<Integer> mAccommodationId;
    private MutableLiveData<Float> mAccommodationRating;
    private ViewAccommodationsController viewAccommodationsController;
    private ViewReviewController viewReviewController;
    private MutableLiveData<List> mReviewList;
    private MutableLiveData<List> mReviewSubList;

    public AccommodationViewModel() {

        mReviewList=new MutableLiveData<>();
        mReviewSubList=new MutableLiveData<>();
        mAccommodationId= new MutableLiveData<>();
        mAccommodationRating= new MutableLiveData<>();
        mAccommodationLatLng= new MutableLiveData<>();
        mAccommodationCategory= new MutableLiveData<>();
        mAccommodationDescritpion= new MutableLiveData<>();
        mAccommodation=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationName = new MutableLiveData<>();
        mAccommodationImage = new MutableLiveData<>();
        mText.setValue("This is Accommodation fragment");

        viewAccommodationsController = new ViewAccommodationsController();
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
        return mAccommodationDescritpion;
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

    public MutableLiveData<List> getReviewList(){
        return mReviewList;
    }

    public MutableLiveData<List> getReviewSubList(){
        return mReviewSubList;
    }

    public void setAccommodation(final int accommodationId) {
        final int id=accommodationId;
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        List<Review>  reviewList= viewReviewController.getReviewList(id);
                        reviewList=viewReviewController.orderReviewListByDate(reviewList);
                        List<Review>  reviewSubList = new ArrayList<>();
                        if(reviewList.size()>NUM_REVIEW){
                            reviewSubList.addAll(reviewList.subList(0,NUM_REVIEW));
                        }else{
                            reviewSubList.addAll(reviewList);
                        }


                        Accommodation ac = viewAccommodationsController.getAccommodationById(id);
                        mAccommodationId.postValue(ac.getId());
                        mAccommodationRating.postValue(ac.getRating());
                        mAccommodation.postValue(ac);
                        mAccommodationName.postValue(ac.getName());
                        mAccommodationImage.postValue(ac.getImages().get(0));
                        mAccommodationLatLng.postValue(new LatLng(ac.getLatitude(),ac.getLongitude()));
                        mAccommodationDescritpion.postValue(ac.getDescription());
                        mAccommodationCategory.postValue(ac.getSubcategory().toString());
                        mReviewSubList.postValue(reviewSubList);
                        mReviewList.postValue(reviewList);
                    }
                },3000);

            }
        });

    }

}