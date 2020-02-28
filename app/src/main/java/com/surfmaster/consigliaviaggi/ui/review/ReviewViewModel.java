package com.surfmaster.consigliaviaggi.ui.review;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewReviewController;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List> mReviewList;
    private MutableLiveData<List> mFilteredReviewList;
    private ViewReviewController viewReviewController;

    public ReviewViewModel() {

        viewReviewController = new ViewReviewController();
        mText = new MutableLiveData<>();
        mReviewList=new MutableLiveData<>();
        mFilteredReviewList=new MutableLiveData<>();

        mText.setValue("This is review list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List> getFilteredReviewList(){
        return mFilteredReviewList;
    }

    public void setReviewList(List reviewList){
        mReviewList.setValue(reviewList);
        mFilteredReviewList.setValue(reviewList);

    }

    public void orderReviewList(String order){
        switch (order) {
            case Constants.BEST_RATING:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByRating(mFilteredReviewList.getValue(), Constants.DESCENDING));
                break;
            case Constants.DEFAULT:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByDate(mFilteredReviewList.getValue()));
                break;
            case Constants.WORST_RATING:
                mFilteredReviewList.setValue(viewReviewController.orderReviewListByRating(mFilteredReviewList.getValue(), Constants.ASCENDING));
                break;
        }
    }

    public void filterReviewList(float minRating, float maxRating) {
        List<Review> unfilteredList=mReviewList.getValue();
        if (unfilteredList!=null)
            mFilteredReviewList.setValue(viewReviewController.filterReviewList(unfilteredList,minRating,maxRating));
    }
}