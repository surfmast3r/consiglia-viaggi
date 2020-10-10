package com.surfmaster.consigliaviaggi.ui.review;

import android.app.Application;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewReviewController;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private List<Review> mReviewList;
    private MutableLiveData<List<Review>> mFilteredReviewList;
    private ViewReviewController viewReviewController;
    private int currentOrder;

    public ReviewViewModel(Application application) {
        super(application);
        viewReviewController = new ViewReviewController();
        mText = new MutableLiveData<>();
        mReviewList=new ArrayList<>();
        mFilteredReviewList=new MutableLiveData<>();
        currentOrder=Constants.DEFAULT_ORDER;
        mText.setValue("This is review list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Review>> getFilteredReviewList(){
        return mFilteredReviewList;
    }

    public void setReviewList(final int accommodationId){

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        List<Review>  reviewList= viewReviewController.getReviewList(accommodationId);
                        reviewList=viewReviewController.orderReviewListByDate(reviewList);
                        mReviewList=viewReviewController.copyList(reviewList);
                        mFilteredReviewList.postValue(reviewList);
                    }
                },3000);

            }
        });

    }

    public void orderReviewList(int order){
        currentOrder=order;
        switch (order) {

            case Constants.BEST_RATING_ORDER:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByRating(mFilteredReviewList.getValue(), Constants.DESCENDING));
                break;
            case Constants.DEFAULT_ORDER:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByDate(mFilteredReviewList.getValue()));
                break;
            case Constants.WORST_RATING_ORDER:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByRating(mFilteredReviewList.getValue(), Constants.ASCENDING));
                break;
        }
    }

    public void filterReviewList(float minRating, float maxRating) {
        List<Review> unfilteredList=mReviewList;
        if (unfilteredList!=null){
            mFilteredReviewList.setValue(viewReviewController.filterReviewList(unfilteredList,minRating,maxRating));
            orderReviewList(currentOrder);
        }

    }
}