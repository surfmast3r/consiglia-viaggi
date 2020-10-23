package com.surfmaster.consigliaviaggi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.surfmaster.consigliaviaggi.CategoryEnum;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;

public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;
    private ViewAccommodationsController viewAccommodationsController;
    private Button citySelectButton;
    private Button mapViewButton;
    private AppCompatButton hotelButton;
    private AppCompatButton restaurantButton;
    private AppCompatButton attractionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewAccommodationsController=new ViewAccommodationsController(requireContext());


        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mapViewButton=root.findViewById(R.id.explore_map_button);
        hotelButton = root.findViewById(R.id.hotel_button);
        restaurantButton = root.findViewById(R.id.restaurant_button);
        attractionButton = root.findViewById(R.id.attraction_button);
        citySelectButton = root.findViewById(R.id.text_select_city);

        bindViews();

        return root;
    }

    private void bindViews() {

        mainViewModel.getCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                citySelectButton.setText(s);
            }
        });

        setButtonListener(citySelectButton);
        setButtonListener(hotelButton);
        setButtonListener(restaurantButton);
        setButtonListener(attractionButton);
        setButtonListener(mapViewButton);

    }


    private void setButtonListener(View view){
        int viewId=view.getId();
        switch (viewId){

            case R.id.text_select_city:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewAccommodationsController.navigateToSelectCityFragment();
                    }
                });
                break;

            case R.id.hotel_button:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewAccommodationsController.navigateToAccommodationListFragment( CategoryEnum.HOTEL.name());
                    }
                });
                break;
            case R.id.restaurant_button:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewAccommodationsController.navigateToAccommodationListFragment( CategoryEnum.RESTAURANT.name());
                    }
                });
                break;
            case R.id.attraction_button:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewAccommodationsController.navigateToAccommodationListFragment( CategoryEnum.ATTRACTION.name());
                    }
                });
                break;
            case R.id.explore_map_button:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewAccommodationsController.navigateToAccommodationMapFragment();
                    }
                });
                break;
        }



    }

}