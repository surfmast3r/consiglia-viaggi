package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

import java.util.ArrayList;
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
    private MutableLiveData<List> mAccommodationList;
    private List unsortedAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();

        mText = new MutableLiveData<>();
        mText.setValue("This is Accommodation List fragment");
        mAccommodationList=new MutableLiveData<>();
        unsortedAccommodationList=new ArrayList();


    }


    public LiveData<String> getText() {
        return mText;
    }
    public MutableLiveData<List> getList(){
        return mAccommodationList;
    }

    public void setAccommodationList(String category, final String city){

        if (mAccommodationList.getValue()!=null)
            mAccommodationList.getValue().clear();
        // do async operation to fetch data
        final String currentCity=city;
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List acList = viewAccommodationsController.getAccommodationList(currentCity);
                        unsortedAccommodationList = viewAccommodationsController.copyList(acList);
                        mAccommodationList.postValue(acList);
                    }
                },3000);

            }
        });
    }

    public void orderAccommodationList(String order) {
        switch (order) {
            case Constants.BEST_RATING:
                mAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mAccommodationList.getValue(), Constants.DESCENDING));
                break;
            case Constants.DEFAULT:
                mAccommodationList.setValue(unsortedAccommodationList);
                break;
            case Constants.WORST_RATING:
                mAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mAccommodationList.getValue(), Constants.ASCENDING));
                break;
        }
    }

}