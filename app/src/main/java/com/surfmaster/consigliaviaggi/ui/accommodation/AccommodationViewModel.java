package com.surfmaster.consigliaviaggi.ui.accommodation;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List> mAccommodationList;

    public AccommodationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Accommodation fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}