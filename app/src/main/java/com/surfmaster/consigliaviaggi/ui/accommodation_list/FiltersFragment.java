package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
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
    private String currentSortOrder;
    private String currentCategory;



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

        initCategorySpinner(root);

        final TextView textView = root.findViewById(R.id.filter_fragment_text);
        accommodationFiltersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        RadioGroup orderRadioGroup = root.findViewById(R.id.order_radio_button);
        orderRadioGroup.check(getCurrentSortParam());
        orderRadioGroup.setOnCheckedChangeListener(setListOrderFilter());

        return root;
    }

    private int getCurrentSortParam() {
        int currentSelection= R.id.radio_default;
        if(accommodationFiltersViewModel.getSortParam().getValue()!=null){
            currentSortOrder=accommodationFiltersViewModel.getSortParam().getValue();

            switch (currentSortOrder) {
                case Constants.BEST_RATING:
                    currentSelection= R.id.radio_best_rating;
                    break;
                case Constants.WORST_RATING:
                    currentSelection= R.id.radio_worst_rating;
                    break;
                case Constants.DEFAULT:
                    currentSelection= R.id.radio_default;
                    break;
            }
        }

        return currentSelection;
    }

    private RadioGroup.OnCheckedChangeListener setListOrderFilter(){

        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_best_rating:
                        currentSortOrder=Constants.BEST_RATING;
                        break;
                    case R.id.radio_worst_rating:
                        currentSortOrder=Constants.WORST_RATING;
                        break;
                    case R.id.radio_default:
                        currentSortOrder=Constants.DEFAULT;
                        break;
                }
            }
        };
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

    private void initCategorySpinner(View root){
        categorySpinner=root.findViewById(R.id.category_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        if(accommodationFiltersViewModel.getCategory().getValue()!=null){
            categorySpinner.setSelection(adapter.getPosition(accommodationFiltersViewModel.getCategory().getValue()));
            currentCategory=accommodationFiltersViewModel.getCategory().getValue();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }

    private void applyFilters(){

        accommodationFiltersViewModel.setSortParam(currentSortOrder);

        if (!categorySpinner.getSelectedItem().toString().equals(currentCategory)) {
            accommodationFiltersViewModel.setCategory(categorySpinner.getSelectedItem().toString());
            accommodationFiltersViewModel.setSortParam(Constants.DEFAULT);
        }

    }



}
