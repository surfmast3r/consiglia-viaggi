package com.surfmaster.consigliaviaggi.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.ReviewsRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.ViewAccommodationActivityArgs;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.List;

public class ReviewListFragment extends Fragment {

    private ReviewViewModel reviewViewModel;

    private RatingBar minRatingBar;
    private RatingBar maxRatingBar;
    private RecyclerView reviewsRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        reviewViewModel =
                ViewModelProviders.of(this).get(ReviewViewModel.class);

        //reviewViewModel.setReviewList(accommodationViewModel.getReviewList().getValue());
        if(getArguments()!=null) {
            int accommodationId = ViewAccommodationActivityArgs.fromBundle(getArguments()).getAccommodationId();
            reviewViewModel.setReviewList(accommodationId);
        }
        View root = inflater.inflate(R.layout.fragment_review_list, container, false);

        bindViews(root);

        initToolbar(root);

        return root;
    }

    private void bindViews(View root){

        final TextView textView = root.findViewById(R.id.review_list_fragment_text);
        reviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        minRatingBar = root.findViewById(R.id.min_rating_filter);
        minRatingBar.setOnRatingBarChangeListener(setRatingFilter());

        maxRatingBar = root.findViewById(R.id.max_rating_filter);
        maxRatingBar.setOnRatingBarChangeListener(setRatingFilter());

        RadioGroup orderRadioGroup = root.findViewById(R.id.order_radio_button);
        orderRadioGroup.setOnCheckedChangeListener(setListOrderFilter());

        reviewsRecyclerView = root.findViewById(R.id.review_recyclerview);
        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        reviewViewModel.getFilteredReviewList().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {

                    ReviewsRecyclerViewAdapter adapter;
                    @Override
                    public void onChanged(@Nullable List<Review> s) {

                        if (adapter==null){
                            adapter=new ReviewsRecyclerViewAdapter(getContext(),s);
                            reviewsRecyclerView.setAdapter(adapter);
                        }
                        else {
                            adapter.refreshList(s);

                        }

                    }
                }
        );

    }

    private void initToolbar(View root) {
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)getActivity()), navController,appBarConfiguration);
    }

    private RatingBar.OnRatingBarChangeListener setRatingFilter(){
         return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                float minRating=minRatingBar.getRating();
                float maxRating=maxRatingBar.getRating();
                reviewViewModel.filterReviewList(minRating,maxRating);
            }
        };
    }

    private RadioGroup.OnCheckedChangeListener setListOrderFilter(){

        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_best_rating:
                        reviewViewModel.orderReviewList(Constants.BEST_RATING_ORDER);
                        break;
                    case R.id.radio_worst_rating:
                        reviewViewModel.orderReviewList(Constants.WORST_RATING_ORDER);
                        break;
                    case R.id.radio_date:
                        reviewViewModel.orderReviewList(Constants.DEFAULT_ORDER);
                        break;
                }
            }
        };
    }


}