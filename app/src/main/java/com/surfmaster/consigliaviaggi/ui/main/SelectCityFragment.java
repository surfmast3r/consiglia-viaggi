package com.surfmaster.consigliaviaggi.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SelectCityFragment extends DialogFragment{

    private MainViewModel mainViewModel;
    private AutoCompleteTextView cityAutoCompleteTextView;
    private ViewAccommodationsController viewAccommodationsController;


    public static SelectCityFragment newInstance() {
        SelectCityFragment f = new SelectCityFragment();
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

        viewAccommodationsController=new ViewAccommodationsController();

        mainViewModel =
                ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_select_city, container, false);

        View cancelButton= root.findViewById(R.id.fullscreen_dialog_close);
        setButtonListener(cancelButton);

        View resetButton= root.findViewById(R.id.resetCityButton);
        setButtonListener(resetButton);

        View applyButton= root.findViewById(R.id.fullscreen_dialog_action);
        setButtonListener(applyButton);

        // Get a reference to the AutoCompleteTextView in the layout
        cityAutoCompleteTextView = root.findViewById(R.id.autocomplete_city);
        // Get the string array
        String[] cities = getResources().getStringArray(R.array.cities_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, cities);
        cityAutoCompleteTextView.setAdapter(adapter);
        cityAutoCompleteTextView.setHint(mainViewModel.getCity().getValue());

        final TextView textView = root.findViewById(R.id.select_city_fragment_text);
        mainViewModel.getText().observe(this, new Observer<String>() {
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
                        validateCityField();
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
                break;
            case R.id.resetCityButton:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetCityField();
                    }
                });
                break;
        }



    }

    private void resetCityField() {
        viewAccommodationsController.resetSelectedCity(requireContext());
        mainViewModel.setCity(viewAccommodationsController.resetSelectedCity(requireContext()));
        cityAutoCompleteTextView.setHint(mainViewModel.getCity().getValue());
    }

    private void validateCityField(){
        if(new ViewAccommodationsController().cityIsValid(requireContext(),cityAutoCompleteTextView.getText().toString())) {
            updateCityField();
            dismiss();
        }
        else
        cityAutoCompleteTextView.setError("Città non valida");
    }

    private void updateCityField(){
        mainViewModel.setCity(cityAutoCompleteTextView.getText().toString());
        viewAccommodationsController.updateSelectedCity(requireContext(),cityAutoCompleteTextView.getText().toString());

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }


}
