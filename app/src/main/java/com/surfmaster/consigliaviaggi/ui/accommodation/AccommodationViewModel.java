package com.surfmaster.consigliaviaggi.ui.accommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccommodationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}