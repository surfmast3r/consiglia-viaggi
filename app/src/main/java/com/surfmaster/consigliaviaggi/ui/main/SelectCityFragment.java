package com.surfmaster.consigliaviaggi.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

import java.util.Objects;

public class SelectCityFragment extends DialogFragment implements PlacesAutoCompleteAdapter.ClickListener{

    private MainViewModel mainViewModel;
    private EditText cityAutoCompleteTextView;
    private ViewAccommodationsController viewAccommodationsController;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private Double latitude,longitude;



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
        Places.initialize(requireContext(), getResources().getString(R.string.google_maps_key));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewAccommodationsController=new ViewAccommodationsController(requireContext());

        mainViewModel =
                ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_select_city, container, false);

        View cancelButton= root.findViewById(R.id.fullscreen_dialog_close);
        setButtonListener(cancelButton);

        View resetButton= root.findViewById(R.id.resetCityButton);
        setButtonListener(resetButton);

        View applyButton= root.findViewById(R.id.fullscreen_dialog_action);
        setButtonListener(applyButton);

        /*google places autocomplete*/
        // Get a reference to the AutoCompleteTextView in the layout
        cityAutoCompleteTextView = root.findViewById(R.id.place_search);
        recyclerView = root.findViewById(R.id.places_recycler_view);
        ((EditText) root.findViewById(R.id.place_search)).addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
        /*google places autocomplete end*/

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

                        updateCityField();
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
        //viewAccommodationsController.resetSelectedCity(requireContext());
        mainViewModel.setCity(viewAccommodationsController.resetSelectedCity());
        cityAutoCompleteTextView.setHint(mainViewModel.getCity().getValue());
    }


    private void updateCityField(){
        if(latitude!=null&&longitude!=null) {
            mainViewModel.setCity(cityAutoCompleteTextView.getText().toString());
            viewAccommodationsController.updateSelectedCity(cityAutoCompleteTextView.getText().toString(), latitude, longitude);
        }

    }


    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }


    @Override
    public void click(Place place, String placeName) {
        cityAutoCompleteTextView.setText(placeName);
        latitude= Objects.requireNonNull(place.getLatLng()).latitude;
        longitude=place.getLatLng().longitude;
        Toast.makeText(requireContext(), place.getName()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }
}
