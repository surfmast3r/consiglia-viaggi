package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.R;

public class AccommodationFragment extends Fragment implements OnMapReadyCallback {

    private AccommodationViewModel accommodationViewModel;
    private GoogleMap googleMap;
    private GoogleMapOptions googleMapOptions;
    private SupportMapFragment mapFragment;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationViewModel =
                ViewModelProviders.of(requireActivity()).get(AccommodationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accommodation_detail, container, false);

        /*googleMap*/
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        /*googleMap end*/

        initToolbar(root);
        bindViews(root);


        return root;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mapFragment.getMapAsync(this);
    }

    private void initToolbar(View root) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        Toolbar toolbar = root.findViewById(R.id.collapsing_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)getActivity()), navController,appBarConfiguration);
    }

    private void bindViews(View root){
        final TextView nametextView = root.findViewById(R.id.accommodation_name);
        accommodationViewModel.getAccommodationName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                nametextView.setText(s);
            }
        });

        final ImageView accommodationImage = root.findViewById(R.id.collapsing_ac_image);

        accommodationViewModel.getAccommodationImage().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                Picasso.get().load(s)
                        .placeholder(getContext().getResources().getDrawable(R.drawable.placeholder))
                        .error(getContext().getResources()
                                .getDrawable(R.drawable.placeholder)).
                        into(accommodationImage);
            }
        });

        final TextView descriptionTextView = root.findViewById(R.id.accommodation_description);
        accommodationViewModel.getAccommodationDescription().observe(this,new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) {
                descriptionTextView.setText(s);
            }
        });

        final TextView categoryTextView = root.findViewById(R.id.category);
        accommodationViewModel.getAccommodationCategory().observe(this,new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) {
                categoryTextView.setText(s);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //MenuItem menuItem = menu.findItem(R.id.action_settings);
        //menuItem.setVisible(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMapOptions = new GoogleMapOptions().liteMode(true);

        MapsInitializer.initialize(requireContext());
        this.googleMap=googleMap;
        accommodationViewModel.getAccommodationLatLng().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng accommodationPosition) {
                CameraPosition position = CameraPosition.builder()
                        .target(accommodationPosition)
                        .zoom(15)
                        .build();

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                googleMap.addMarker(new MarkerOptions()
                        .position(accommodationPosition)
                        .title(accommodationViewModel.getAccommodationName().getValue()));
            }
        });

    }
}