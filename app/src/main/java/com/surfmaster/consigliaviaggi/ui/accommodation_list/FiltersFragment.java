package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Category;

import java.util.ArrayList;
import java.util.Objects;

public class FiltersFragment extends DialogFragment{

    private AccommodationFiltersViewModel accommodationFiltersViewModel;
    private RadioGroup orderRadioGroup;
    private Spinner categorySpinner;
    private Spinner subCategorySpinner;
    private RatingBar minRatingBar;
    private RatingBar maxRatingBar;
    private int currentSortOrder;
    private Category currentCategory;
    private Category currentSubCategory;
    private Float minRating;
    private Float maxRating;

    private boolean ratingChanged=false,orderChanged=false;

    private boolean initialize =true;


    public static FiltersFragment newInstance() {
        FiltersFragment f = new FiltersFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationFiltersViewModel = new ViewModelProvider(requireActivity()).get(AccommodationFiltersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filters, container, false);

        View cancelButton= root.findViewById(R.id.fullscreen_dialog_close);
        setButtonListener(cancelButton);

        View applyButton= root.findViewById(R.id.fullscreen_dialog_action);
        setButtonListener(applyButton);

        initRatingBars(root);

        initOrderRadioGroup(root);

        initCategorySpinner(root);

        final TextView textView = root.findViewById(R.id.filter_fragment_text);
        accommodationFiltersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }


    private void setButtonListener(View view){
        int viewId=view.getId();
        switch (viewId){
            case R.id.fullscreen_dialog_action:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applyFilters();
                        dismiss();
                    }
                });
                break;
            case R.id.fullscreen_dialog_close:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
        }



    }

    private void initRatingBars(View root){
        minRatingBar =root.findViewById(R.id.min_rating_filter);
        minRating=accommodationFiltersViewModel.getMinRating().getValue();
        if (minRating != null)
            minRatingBar.setRating(minRating);

        maxRatingBar = root.findViewById(R.id.max_rating_filter);
        maxRating=accommodationFiltersViewModel.getMaxRating().getValue();
        if(maxRating!=null)
            maxRatingBar.setRating(maxRating);

        minRatingBar.setOnRatingBarChangeListener(setRatingFilter());
        maxRatingBar.setOnRatingBarChangeListener(setRatingFilter());
    }

    private RatingBar.OnRatingBarChangeListener setRatingFilter(){
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                ratingChanged=true;
                minRating=minRatingBar.getRating();
                maxRating=maxRatingBar.getRating();

            }
        };
    }

    private int getCurrentSortParam() {
        int currentSelection= R.id.radio_a_z;
        if(accommodationFiltersViewModel.getSortParam().getValue()!=null){
            currentSortOrder=accommodationFiltersViewModel.getSortParam().getValue();

            switch (currentSortOrder) {
                case Constants.BEST_RATING_ORDER:
                    currentSelection= R.id.radio_best_rating;
                    break;
                case Constants.WORST_RATING_ORDER:
                    currentSelection= R.id.radio_worst_rating;
                    break;
                case Constants.DEFAULT_ORDER:
                    currentSelection= R.id.radio_a_z;
                    break;
                case Constants.Z_A_ORDER:
                    currentSelection= R.id.radio_z_a;
                    break;
            }
        }

        return currentSelection;
    }


    private void initOrderRadioGroup(View root){
        orderRadioGroup = root.findViewById(R.id.order_radio_button);
        orderRadioGroup.check(getCurrentSortParam());

        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                orderChanged=true;
                switch (checkedId) {
                    case R.id.radio_best_rating:
                        currentSortOrder=Constants.BEST_RATING_ORDER;
                        break;
                    case R.id.radio_worst_rating:
                        currentSortOrder=Constants.WORST_RATING_ORDER;
                        break;
                    case R.id.radio_a_z:
                        currentSortOrder=Constants.DEFAULT_ORDER;
                        break;
                    case R.id.radio_z_a:
                        currentSortOrder=Constants.Z_A_ORDER;
                        break;
                }
            }
        });

    }

    private void initCategorySpinner(View root){
        categorySpinner=root.findViewById(R.id.category_spinner);
        subCategorySpinner=root.findViewById(R.id.sub_category_spinner);

        final CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this.getContext(),
                android.R.layout.simple_spinner_item,
                accommodationFiltersViewModel.getCategoryList());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        final CategorySpinnerAdapter subCategoryAdapter = new CategorySpinnerAdapter(this.getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<Category>());
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinner.setAdapter(subCategoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!initialize) {
                    subCategorySpinner.setSelection(0);
                    subCategoryAdapter.clear();
                    subCategoryAdapter.addAll(Objects.requireNonNull(adapter.getItem(i)).getSubcategoryList());
                }else
                    initialize=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        currentCategory=accommodationFiltersViewModel.getCategory().getValue();
        if(currentCategory!=null){
            categorySpinner.setSelection(adapter.getPosition(currentCategory));
            subCategoryAdapter.addAll(accommodationFiltersViewModel.getCategory().getValue().getSubcategoryList());
        }

        currentSubCategory=accommodationFiltersViewModel.getSubCategory().getValue();
        if(currentSubCategory!=null){
            subCategorySpinner.setSelection(subCategoryAdapter.getPosition(currentSubCategory));
        }else{
            subCategorySpinner.setSelection(0);
        }



    }

    private boolean checkCategoryChanged(Category newCategory){

        return !newCategory.getCategoryName().equals(currentCategory.getCategoryName());
    }
    private boolean checkSubCategoryChanged(Category newCategory){

        return !newCategory.getCategoryName().equals(currentSubCategory.getCategoryName());
    }

    private void applyFilters(){
        boolean applyFlag=false;
        Category newCategory= (Category) categorySpinner.getSelectedItem();
        Category newSubCategory= (Category) subCategorySpinner.getSelectedItem();
        if (checkCategoryChanged(newCategory)) {
            applyFlag=true;
            accommodationFiltersViewModel.setSubCategory(newSubCategory);
            accommodationFiltersViewModel.setCategory(newCategory);
            accommodationFiltersViewModel.setSortParam(Constants.DEFAULT_ORDER);
            accommodationFiltersViewModel.setMinRating(Constants.DEFAULT_MIN_RATING);
            accommodationFiltersViewModel.setMaxRating(Constants.DEFAULT_MAX_RATING);
        }else if(checkSubCategoryChanged(newSubCategory)) {
            applyFlag=true;
            accommodationFiltersViewModel.setSubCategory(newSubCategory);
            accommodationFiltersViewModel.setSortParam(Constants.DEFAULT_ORDER);
            accommodationFiltersViewModel.setMinRating(Constants.DEFAULT_MIN_RATING);
            accommodationFiltersViewModel.setMaxRating(Constants.DEFAULT_MAX_RATING);
        }
        if(ratingChanged){
            applyFlag=true;

            accommodationFiltersViewModel.setMinRating(minRating);
            accommodationFiltersViewModel.setMaxRating(maxRating);
        }
        if(orderChanged){
            applyFlag=true;
            accommodationFiltersViewModel.setSortParam(currentSortOrder);
        }


        if(applyFlag) {
            accommodationFiltersViewModel.applySearchParams();
            ratingChanged=false;
            orderChanged=false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }



}
