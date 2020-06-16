package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

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
    private MutableLiveData<Integer> pageNumber;
    private SearchParamsAccommodation currentSearchParams;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();
        pageNumber=new MutableLiveData<>();
        mFilteredAccommodationList=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationList=new ArrayList<Accommodation>();
        unsortedAccommodationList=new ArrayList<Accommodation>();

        mText.setValue("This is Accommodation List fragment");
        currentOrder=Constants.DEFAULT_ORDER;


    }

    public MutableLiveData<Integer> getPageNumber(){return pageNumber;}
    public LiveData<String> getText() {
        return mText;
    }
    public MutableLiveData<List> getList(){
        return mFilteredAccommodationList;
    }

    public void setAccommodationList(SearchParamsAccommodation searchParams){

        if (mAccommodationList!=null)
            mAccommodationList.clear();
        // do async operation to fetch data
        currentSearchParams= searchParams;
        //final String currentCity=city;
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //List acList = viewAccommodationsController.getAccommodationList(currentCity);
                        List acList = viewAccommodationsController.getAccommodationList(currentSearchParams);
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