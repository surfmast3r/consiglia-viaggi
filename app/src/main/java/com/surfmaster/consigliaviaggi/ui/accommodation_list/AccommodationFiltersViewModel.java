package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationFiltersViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mCategory;
    private MutableLiveData<Integer> mSortParam;
    private MutableLiveData<Float> mMinRating;
    private MutableLiveData<Float> mMaxRating;

    public AccommodationFiltersViewModel() {

        mMinRating=new MutableLiveData<>();
        mMaxRating=new MutableLiveData<>();
        mMinRating.setValue(Constants.DEFAULT_MIN_RATING);
        mMaxRating.setValue(Constants.DEFAULT_MAX_RATING);
        mText = new MutableLiveData<>();
        mSortParam=new MutableLiveData<>();
        mCategory=new MutableLiveData<>();
        mSortParam.setValue(Constants.DEFAULT_ORDER);

        mText.setValue("This is accommodation filter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getCategory() {
        return mCategory;
    }

    public LiveData<Integer> getSortParam(){return mSortParam;}

    public MutableLiveData<Float> getMinRating() {
        return mMinRating;
    }

    public MutableLiveData<Float> getMaxRating() {
        return mMaxRating;
    }

    public void setSortParam(Integer sortParam) {
        mSortParam.setValue(sortParam);
    }

    public void setCategory(String category) {
        mCategory.setValue(category);
    }

    public void setMinRating(float minRating) {
        mMinRating.setValue(minRating);
    }

    public void setMaxRating(float maxRating) {
        mMaxRating.setValue(maxRating);
    }
}