package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.util.Log;
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
    private List<Accommodation> mAccommodationList;
    private ViewAccommodationsController viewAccommodationsController;
    private MutableLiveData<List<Accommodation>> mFilteredAccommodationList;
    private SearchParamsAccommodation currentSearchParams;

    public AccommodationListViewModel() {
        viewAccommodationsController =new ViewAccommodationsController();
        mFilteredAccommodationList=new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mAccommodationList= new ArrayList<>();

        mText.setValue("This is Accommodation List fragment");


    }

    public LiveData<String> getText() {
        return mText;
    }
    public MutableLiveData<List<Accommodation>> getList(){
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

                        List<Accommodation> acList = viewAccommodationsController.getAccommodationList(currentSearchParams);

                        mAccommodationList=viewAccommodationsController.copyList(acList);
                        mFilteredAccommodationList.postValue(acList);
                    }
                },3000);

            }
        });
    }

    public void loadMore(){
        Log.i("Recyler Endless scroll", "Load More");
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List<Accommodation> acList = viewAccommodationsController.nextPage();
                        if (acList!= null) {
                            mAccommodationList.addAll(acList);
                            mFilteredAccommodationList.postValue(mAccommodationList);
                        }
                    }
                },3000);

            }
        });
    }


}