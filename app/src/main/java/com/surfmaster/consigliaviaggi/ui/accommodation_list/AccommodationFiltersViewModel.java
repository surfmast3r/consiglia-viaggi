package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationFiltersViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mCategory;
    private ViewAccommodationsController viewAccommodationsController;
    private MutableLiveData<String> mSortParam;

    public AccommodationFiltersViewModel() {

        viewAccommodationsController = new ViewAccommodationsController();
        mText = new MutableLiveData<>();
        mSortParam=new MutableLiveData<>();
        mCategory=new MutableLiveData<>();
        mSortParam.setValue(Constants.DEFAULT);

        mText.setValue("This is accommodation filter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getCategory() {
        return mCategory;
    }


    public LiveData<String> getSortParam(){return mSortParam;}

    public void setSortParam(String sortParam) {
        mSortParam.setValue(sortParam);

    }
    public void setCategory(String category) {
        mCategory.setValue(category);

    }
}