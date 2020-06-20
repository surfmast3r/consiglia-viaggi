package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.Category;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private SearchParamsAccommodation searchParamsAccommodation;

    public AccommodationFiltersViewModel() {

        mCurrentSearchParams =new MutableLiveData<>();
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

        mText.setValue("This is accommodation filter fragment");
        searchParamsAccommodation= new SearchParamsAccommodation.Builder().create();
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
        //searchParamsAccommodation.setDirection(1);
    }

    public void setCategory(Category category) {
        mCategory.setValue(category);
        searchParamsAccommodation.setCurrentCategory(category.getCategoryName());
    }
    public void setSubCategory(Category newSubCategory) {
        mSubCategory.setValue(newSubCategory);
        searchParamsAccommodation.setCurrentSubCategory(newSubCategory.getCategoryName());
    }

    /*pensare a qualcosa di meglio*/
    public void setCategory(String categoryName) {
        Category cat = findCategoryByName(categoryName);
        if (cat!=null) {
            Category subCat = cat.getSubcategoryList().get(0);
            mCategory.setValue(cat);
            mSubCategory.setValue(subCat);
            searchParamsAccommodation.setCurrentCategory(cat.getCategoryName());
            searchParamsAccommodation.setCurrentSubCategory(subCat.getCategoryName());

        }

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
        mCurrentSearchParams.setValue(searchParamsAccommodation);
    }
}