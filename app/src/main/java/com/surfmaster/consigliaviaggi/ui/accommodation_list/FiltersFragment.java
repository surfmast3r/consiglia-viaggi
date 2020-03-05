package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FiltersFragment extends DialogFragment{

    private DialogInterface.OnDismissListener onDismissListener;

    private AccommodationFiltersViewModel accommodationFiltersViewModel;
    private Spinner categorySpinner;
    private RatingBar minRatingBar;
    private RatingBar maxRatingBar;
    private int currentSortOrder;
    private String currentCategory;
    private Float minRating;
    private Float maxRating;



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
        accommodationFiltersViewModel =
                ViewModelProviders.of(requireActivity()).get(AccommodationFiltersViewModel.class);
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

                minRating=minRatingBar.getRating();
                maxRating=maxRatingBar.getRating();

            }
        };
    }

    private int getCurrentSortParam() {
        int currentSelection= R.id.radio_default;
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
                    currentSelection= R.id.radio_default;
                    break;
            }
        }

        return currentSelection;
    }


    private void initOrderRadioGroup(View root){
        RadioGroup orderRadioGroup = root.findViewById(R.id.order_radio_button);
        orderRadioGroup.check(getCurrentSortParam());
        orderRadioGroup.setOnCheckedChangeListener(setListOrderFilter());

    }

    private RadioGroup.OnCheckedChangeListener setListOrderFilter(){

        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_best_rating:
                        currentSortOrder=Constants.BEST_RATING_ORDER;
                        break;
                    case R.id.radio_worst_rating:
                        currentSortOrder=Constants.WORST_RATING_ORDER;
                        break;
                    case R.id.radio_default:
                        currentSortOrder=Constants.DEFAULT_ORDER;
                        break;
                }
            }
        };
    }

    private void initCategorySpinner(View root){
        categorySpinner=root.findViewById(R.id.category_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        currentCategory=accommodationFiltersViewModel.getCategory().getValue();
        if(currentCategory!=null){
            categorySpinner.setSelection(adapter.getPosition(currentCategory));

        }

    }

    private boolean checkCategoryChanged(String newCategory){

        if(newCategory.equals(currentCategory))
            return false;
        else
            return true;
    }

    private void applyFilters(){
        String newCategory=categorySpinner.getSelectedItem().toString();

        if (checkCategoryChanged(newCategory)) {
            accommodationFiltersViewModel.setCategory(newCategory);
            accommodationFiltersViewModel.setSortParam(Constants.DEFAULT_ORDER);
            accommodationFiltersViewModel.setMinRating(Constants.DEFAULT_MIN_RATING);
            accommodationFiltersViewModel.setMaxRating(Constants.DEFAULT_MAX_RATING);

        }else{
            accommodationFiltersViewModel.setSortParam(currentSortOrder);
            accommodationFiltersViewModel.setMinRating(minRating);
            accommodationFiltersViewModel.setMaxRating(maxRating);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }



}
