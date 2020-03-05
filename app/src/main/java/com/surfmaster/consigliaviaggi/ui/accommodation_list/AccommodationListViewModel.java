package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.net.HttpCookie;
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
    private List mAccommodationList;
    private List unsortedAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;
    private MutableLiveData<List> mFilteredAccommodationList;
    private int currentOrder;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();

        currentOrder=Constants.DEFAULT_ORDER;
        mFilteredAccommodationList=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is Accommodation List fragment");
        mAccommodationList=new ArrayList();
        unsortedAccommodationList=new ArrayList();


    }


    public LiveData<String> getText() {
        return mText;
    }
    public MutableLiveData<List> getList(){
        return mFilteredAccommodationList;
    }

    public void setAccommodationList(String category, final String city){

        if (mAccommodationList!=null)
            mAccommodationList.clear();
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
                        mAccommodationList=viewAccommodationsController.copyList(acList);
                        mFilteredAccommodationList.postValue(acList);
                    }
                },3000);

            }
        });
    }

    public void orderAccommodationList(int order) {
        currentOrder=order;
        switch (order) {
            case Constants.BEST_RATING_ORDER:
                mFilteredAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mFilteredAccommodationList.getValue(), Constants.DESCENDING));
                break;
            case Constants.DEFAULT_ORDER:
                mFilteredAccommodationList.setValue(unsortedAccommodationList);
                break;
            case Constants.WORST_RATING_ORDER:
                mFilteredAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mFilteredAccommodationList.getValue(), Constants.ASCENDING));
                break;
        }
    }

    public void filterAccommodationList(float minRating, float maxRating) {
        List<Accommodation> unfilteredList=mAccommodationList;
        if (unfilteredList!=null) {
            mFilteredAccommodationList.setValue(viewAccommodationsController.filterAccommodationList(unfilteredList, minRating, maxRating));
            unsortedAccommodationList=viewAccommodationsController.filterAccommodationList(unfilteredList, minRating, maxRating);
            orderAccommodationList(currentOrder);
        }
    }

}