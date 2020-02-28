package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.facebook.shimmer.ShimmerFrameLayout;
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
    private MutableLiveData<String> mFilterText;
    private MutableLiveData<String> mCategoryText;
    private MutableLiveData<List> mAccommodationList;
    private List unsortedAccommodationList;
    private String sortParam;
    private ViewAccommodationsController viewAccommodationsController;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();
        mCategoryText= new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is Accommodation List fragment");
        mFilterText = new MutableLiveData<>();
        mFilterText.setValue("This is Filters fragment");
        mAccommodationList=new MutableLiveData<>();

        unsortedAccommodationList=new ArrayList();
        sortParam =Constants.DEFAULT;


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

    public void setAccommodationList(String category, final String city){
        setCategoryText(category);

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
                        unsortedAccommodationList=acList;
                        mAccommodationList.postValue(acList);
                    }
                },3000);

            }
        });
    }
    public String getSortParam(){return sortParam;}

    public void orderAccommodationList(String order) {
        switch (order) {
            case Constants.BEST_RATING:
                mAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mAccommodationList.getValue(), Constants.DESCENDING));
                sortParam=Constants.BEST_RATING;
                break;
            case Constants.DEFAULT:
                mAccommodationList.setValue(unsortedAccommodationList);
                sortParam=Constants.DEFAULT;
                break;
            case Constants.WORST_RATING:
                mAccommodationList.setValue(viewAccommodationsController.orderAccommodationListByRating(mAccommodationList.getValue(), Constants.ASCENDING));
                sortParam=Constants.WORST_RATING;
                break;
        }
    }

    private void setCategoryText(String mCategoryText) {
        this.mCategoryText.setValue(mCategoryText);
    }
}