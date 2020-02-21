package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mFilterText;

    private MutableLiveData<String> mCategoryText;
    private MutableLiveData<List> mAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();
        mCategoryText= new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is Accommodation List fragment");
        mFilterText = new MutableLiveData<>();
        mFilterText.setValue("This is Filters fragment");
        mAccommodationList=new MutableLiveData<>();

        // do async operation to fetch data
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List acList = viewAccommodationsController.getAccommodationList("Napoli");
                        mAccommodationList.postValue(acList);
                    }
                },3000);



                // on background thread, obtain a fresh list accommodations
                //List acList = viewAccommodationsController.getAccommodationList();

                // make it available to outside observers of the "mAccommodationList"
                // MutableLiveData object
                //mAccommodationList.postValue(acList);

            }
        });
    }

    public LiveData<String> getCategoryText() {
        return mCategoryText;
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getFilterText() {
        return mFilterText;
    }
    public MutableLiveData<List> getList(){
        return mAccommodationList;
    }

    /*public int getAccommodationId(int position) {
        return viewAccommodationsController.getAccommodationId(position);
    }*/

    public void setCategoryText(String mCategoryText) {
        this.mCategoryText.setValue(mCategoryText);
    }
}