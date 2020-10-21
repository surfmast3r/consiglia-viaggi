package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.Category;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationFiltersViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Category> mCategory;
    private MutableLiveData<Category> mSubCategory;
    private MutableLiveData<Integer> mSortParam;
    private MutableLiveData<Float> mMinRating;
    private MutableLiveData<Float> mMaxRating;
    private ArrayList<Category> categoryArrayList;
    private MutableLiveData<SearchParamsAccommodation> mCurrentSearchParams;
    private String orderBy;
    private MutableLiveData<String> mCity;

    public AccommodationFiltersViewModel() {

        mCurrentSearchParams =new MutableLiveData<>();
        mCity=new MutableLiveData<>();
        categoryArrayList=new ArrayList<>();
        categoryArrayList.addAll(Category.createCategoryList());
        mMinRating=new MutableLiveData<>();
        mMaxRating=new MutableLiveData<>();
        mMinRating.setValue(Constants.DEFAULT_MIN_RATING);
        mMaxRating.setValue(Constants.DEFAULT_MAX_RATING);
        mText = new MutableLiveData<>();
        mSortParam=new MutableLiveData<>();
        mCategory=new MutableLiveData<>();
        mSubCategory=new MutableLiveData<>();

        mSortParam.setValue(Constants.DEFAULT_ORDER);
        orderBy=Constants.ID;
        mText.setValue("This is accommodation filter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Category> getCategory() {
        return mCategory;
    }
    public LiveData<Category> getSubCategory() {
        return mSubCategory;
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
        if (sortParam==Constants.DEFAULT_ORDER)
            orderBy=Constants.ID;
        else
            orderBy=Constants.RATING;
    }

    public void setCategory(Category category) {
        mCategory.setValue(category);
    }
    public void setSubCategory(Category newSubCategory) {
        mSubCategory.setValue(newSubCategory);

    }

    /*pensare a qualcosa di meglio*/
    public void setCategory(String categoryName) {
        Category cat = findCategoryByName(categoryName);
        if (cat!=null) {
            Category subCat = cat.getSubcategoryList().get(0);
            mCategory.setValue(cat);
            mSubCategory.setValue(subCat);

        }

    }
    public void setCity(String city) {
        mCity.setValue(city);
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

    public MutableLiveData<SearchParamsAccommodation> getCurrentSearchParams() {
        return mCurrentSearchParams;
    }

    public List<Category> getCategoryList(){
        return categoryArrayList;
    }


    public void applySearchParams() {
        SearchParamsAccommodation searchParamsAccommodation = new SearchParamsAccommodation.Builder()
                .setCurrentCategory(mCategory.getValue().getCategoryName())
                .setCurrentCity(mCity.getValue())
                .setCurrentSubCategory(mSubCategory.getValue().getCategoryName())
                .setDirection(mSortParam.getValue().toString())
                .setMinRating(mMinRating.getValue())
                .setMaxRating(mMaxRating.getValue())
                .setOrderBy(orderBy)
                .setDirection(getDirection(mSortParam.getValue()))
                .create();
        mCurrentSearchParams.setValue(searchParamsAccommodation);
    }

    private String getDirection(int order) {
        String direction= Constants.DESC;
        switch (order) {
            case Constants.BEST_RATING_ORDER:
                direction= Constants.DESC;
                break;
            case Constants.WORST_RATING_ORDER:
                direction= Constants.ASC;
                break;
        }
        return direction;
    }


}