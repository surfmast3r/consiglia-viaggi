package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationFiltersViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Category> mCategory;
    private MutableLiveData<Integer> mSortParam;
    private MutableLiveData<Float> mMinRating;
    private MutableLiveData<Float> mMaxRating;
    private ArrayList<Category> categoryArrayList;

    public AccommodationFiltersViewModel() {

        categoryArrayList=new ArrayList<>();
        categoryArrayList.addAll(createCategoryList());
        mMinRating=new MutableLiveData<>();
        mMaxRating=new MutableLiveData<>();
        mMinRating.setValue(Constants.DEFAULT_MIN_RATING);
        mMaxRating.setValue(Constants.DEFAULT_MAX_RATING);
        mText = new MutableLiveData<>();
        mSortParam=new MutableLiveData<>();
        mCategory=new MutableLiveData<>();
        mSortParam.setValue(Constants.DEFAULT_ORDER);

        mText.setValue("This is accommodation filter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Category> getCategory() {
        return mCategory;
    }

    public LiveData<Integer> getSortParam(){return mSortParam;}

    public MutableLiveData<Float> getMinRating() {
        return mMinRating;
    }

    public MutableLiveData<Float> getMaxRating() {
        return mMaxRating;
    }

    public void setSortParam(Integer sortParam) {
        mSortParam.setValue(sortParam);
    }

    public void setCategory(Category category) {
        mCategory.setValue(category);
    }

    public void setCategory(String categoryName) {
        mCategory.setValue(findCategoryByName(categoryName));
    }

    private Category findCategoryByName(String categoryName) {
        for(int i=0; i<categoryArrayList.size();i++) {
            if (categoryArrayList.get(i).getCategoryName().equals(categoryName)){
                return categoryArrayList.get(i);
            }
        }
        return null;
    }

    public void setMinRating(float minRating) {
        mMinRating.setValue(minRating);
    }

    public void setMaxRating(float maxRating) {
        mMaxRating.setValue(maxRating);
    }

    public List<Category> getCategoryList(){
        return categoryArrayList;
    }

    public List<Category> createCategoryList(){
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("HOTEL","Hotel"));
        categoryArrayList.add(new Category("RESTAURANT","Ristoranti"));
        categoryArrayList.add(new Category("ATTRACTION","Attrazioni"));
        return categoryArrayList;
    }
}