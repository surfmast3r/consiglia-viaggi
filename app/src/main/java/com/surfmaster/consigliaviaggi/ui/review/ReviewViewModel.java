package com.surfmaster.consigliaviaggi.ui.review;

import android.app.Application;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.CreateReviewController;
import com.surfmaster.consigliaviaggi.controllers.ManageUserController;
import com.surfmaster.consigliaviaggi.controllers.ViewReviewController;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ReviewViewModel extends AndroidViewModel {

    private ManageUserController manageUserController;
    private Integer accommodationId;
    private MutableLiveData<String> mText;
    private List<Review> mReviewList;
    private MutableLiveData<List<Review>> mFilteredReviewList;
    private MutableLiveData<List<Review>> mReviewSublist;
    private MutableLiveData<Boolean> postReviewResponse;
    private ViewReviewController viewReviewController;
    private int currentOrder;
    private CreateReviewController createReviewController;
    private MutableLiveData<Boolean> mUserStatus;

    public ReviewViewModel(Application application) {
        super(application);
        viewReviewController = new ViewReviewController();
        manageUserController = new ManageUserController(application);
        createReviewController=new CreateReviewController(application);

        mUserStatus=new MutableLiveData<>(false);
        mText = new MutableLiveData<>();
        mReviewList=new ArrayList<>();
        mFilteredReviewList=new MutableLiveData<>();
        mReviewSublist=new MutableLiveData<>();
        currentOrder=Constants.DEFAULT_ORDER;
        postReviewResponse=new MutableLiveData<>(false);
        mText.setValue("Lista Recensioni");

        checkIfLoggedIn();


    }

    public Boolean getUserStatus() {
        return mUserStatus.getValue();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Review>> getFilteredReviewList(){
        return mFilteredReviewList;
    }

    public MutableLiveData<List<Review>> getReviewSublist() {
        return mReviewSublist;
    }

    public void setReviewList(final int accommodationId){

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                List<Review> reviewList = viewReviewController.getReviewList(accommodationId);
                reviewList = viewReviewController.orderReviewListByDate(reviewList);
                mReviewList.addAll(reviewList);
                mFilteredReviewList.postValue(reviewList);
            }
        });

    }

    public void setReviewSubList(final int accommodationId){

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                List<Review>  reviewList= viewReviewController.getReviewList(accommodationId);
                reviewList=viewReviewController.orderReviewListByDate(reviewList);
                List<Review>  reviewSubList = new ArrayList<>();
                if(reviewList.size()>Constants.NUM_REVIEW){
                    reviewSubList.addAll(reviewList.subList(0,Constants.NUM_REVIEW));
                }else{
                    reviewSubList.addAll(reviewList);
                }
                mReviewSublist.postValue(reviewSubList);
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

    public Integer getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Integer accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void postReview(final float rating, final String reviewText) {

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                if(createReviewController.createReview(new Review.Builder()
                        .setReviewText(reviewText)
                        .setAccommodationId(accommodationId)
                        .setRating(rating)
                        .build())){
                        postReviewResponse.postValue(true);
                }else{
                    postReviewResponse.postValue(false);
                }
            }
        });

    }

    public MutableLiveData<Boolean> getPostReviewResponse() {
        return postReviewResponse;
    }

    private void checkIfLoggedIn() {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                int userId=manageUserController.getUserId();
                if(userId > 0) {
                    mUserStatus.postValue(true);
                }
                else
                    mUserStatus.postValue(false);
            }
        });
    }

    public void setPostReviewResponse(boolean response) {
        postReviewResponse.setValue(response);
    }

}