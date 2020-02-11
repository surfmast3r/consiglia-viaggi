package com.surfmaster.consigliaviaggi.ui.main;

import com.surfmaster.consigliaviaggi.models.AccommodationDaoStub;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List> mAccommodationList;

    public MainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mAccommodationList=new MutableLiveData<>();
        mAccommodationList.setValue(new AccommodationDaoStub().getAccommodationList());
    }

    public LiveData<String> getText() {
        return mText;
    }
    public List getList(){
        return mAccommodationList.getValue();
    }
}