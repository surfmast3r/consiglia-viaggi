package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.ReviewsRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.ui.review.ReviewViewModel;

import java.util.List;

public class AccommodationFragment extends Fragment implements OnMapReadyCallback {

    private AccommodationViewModel accommodationViewModel;
    private ReviewViewModel reviewViewModel;
    private SupportMapFragment mapFragment;
    private RecyclerView reviewsRecyclerView;
    private AppCompatButton readAllButton;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RelativeLayout accommodationDetailLayout;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);
        accommodationViewModel =
                new ViewModelProvider(requireActivity()).get(AccommodationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accommodation_detail, container, false);

        /*bind shimmer container*/
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);

        accommodationDetailLayout = root.findViewById(R.id.accommodation_details_layout);
        /*googleMap*/
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        /*googleMap end*/

        initToolbar(root);
        bindViews(root);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mapFragment.getMapAsync(this);
    }

    private void initToolbar(View root) {
        Toolbar toolbar = root.findViewById(R.id.collapsing_toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)requireActivity()), navController,appBarConfiguration);
    }

    private void bindViews(View root){
        final TextView nametextView = root.findViewById(R.id.accommodation_name);
        accommodationViewModel.getAccommodationName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                nametextView.setText(s);
                stopShimmerAnimation();
            }
        });

        final ImageView accommodationImage = root.findViewById(R.id.collapsing_ac_image);

        accommodationViewModel.getAccommodationImage().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                assert s != null;
                if (s.isEmpty()) {
                    accommodationImage.setImageResource(R.drawable.placeholder);
                } else{
                    Picasso.get().load(s)
                            .placeholder(requireContext().getResources().getDrawable(R.drawable.placeholder,null))
                            .error(requireContext().getResources()
                                    .getDrawable(R.drawable.placeholder,null))
                            .into(accommodationImage);
                }

            }
        });

        final TextView descriptionTextView = root.findViewById(R.id.accommodation_description);
        accommodationViewModel.getAccommodationDescription().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) {
                descriptionTextView.setText(s);
            }
        });

        final TextView categoryTextView = root.findViewById(R.id.category);
        accommodationViewModel.getAccommodationCategory().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) {
                categoryTextView.setText(s);
            }
        });

        final RatingBar accommodationRatingBar = root.findViewById(R.id.accommodation_rating);
        accommodationViewModel.getAccommodationRating().observe(getViewLifecycleOwner(),new Observer<Float>(){
            @Override
            public void onChanged(@Nullable Float s) {
                accommodationRatingBar.setRating(s);
            }
        });

        reviewsRecyclerView = root.findViewById(R.id.recyclerview);
        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        reviewViewModel.getReviewSublist().observe(getViewLifecycleOwner(), new Observer<List>() {

            ReviewsRecyclerViewAdapter adapter;
                    @Override
                    public void onChanged(@Nullable List s) {
                        if (adapter==null){
                            adapter=new ReviewsRecyclerViewAdapter(s);
                            reviewsRecyclerView.setAdapter(adapter);
                        }
                        else
                            adapter.notifyDataSetChanged();

                    }
                }
        );

        readAllButton=root.findViewById(R.id.read_all_button);
        accommodationViewModel.getAccommodationId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer id) {

                reviewViewModel.setReviewSubList(id);
                final int accommodationId =id;
                readAllButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AccommodationFragmentDirections.ActionNavAccommodationDetailToNavReviewList action =
                                AccommodationFragmentDirections.actionNavAccommodationDetailToNavReviewList(accommodationId);
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
                    }
                });
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(reviewViewModel.getUserStatus())
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                            AccommodationFragmentDirections.actionNavAccommodationDetailToNavCreateReview());
                else
                Snackbar.make(view, "Devi autenticarti per pubblicare una recensione", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }

    private void stopShimmerAnimation() {
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        accommodationDetailLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions().liteMode(true);

        MapsInitializer.initialize(requireContext());
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

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();

    }



    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();

    }
}