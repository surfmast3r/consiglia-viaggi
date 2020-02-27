package com.surfmaster.consigliaviaggi.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.ReviewsRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.ui.accommodation.AccommodationViewModel;

import java.util.List;

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

public class ReviewListFragment extends Fragment {

    private ReviewViewModel reviewViewModel;
    private AccommodationViewModel accommodationViewModel;

    private RecyclerView reviewsRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        reviewViewModel =
                ViewModelProviders.of(this).get(ReviewViewModel.class);
        accommodationViewModel =
                ViewModelProviders.of(requireActivity()).get(AccommodationViewModel.class);


        accommodationViewModel.orderReviewList(Constants.DEFAULT);
        View root = inflater.inflate(R.layout.fragment_review_list, container, false);

        bindViews(root);

        initToolbar(root);

        return root;
    }

    private void bindViews(View root){

        final TextView textView = root.findViewById(R.id.review_list_fragment_text);
        reviewViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final RatingBar minRatingBar = root.findViewById(R.id.min_rating_filter);
        minRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });
        final RatingBar maxRatingBar = root.findViewById(R.id.max_rating_filter);
        maxRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        RadioGroup orderRadioGroup = root.findViewById(R.id.order_radio_button);
        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_best_rating:
                        accommodationViewModel.orderReviewList(Constants.BEST_RATING);
                        break;
                    case R.id.radio_worst_rating:
                        accommodationViewModel.orderReviewList(Constants.WORST_RATING);
                        break;
                    case R.id.radio_default:
                        accommodationViewModel.orderReviewList(Constants.DEFAULT);
                        break;
                }
            }
        });

        reviewsRecyclerView = root.findViewById(R.id.review_recyclerview);
        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        accommodationViewModel.getReviewList().observe(this, new Observer<List>() {

                    ReviewsRecyclerViewAdapter adapter;
                    @Override
                    public void onChanged(@Nullable List s) {
                        if (adapter==null){
                            adapter=new ReviewsRecyclerViewAdapter(getContext(),s);
                            reviewsRecyclerView.setAdapter(adapter);
                        }
                        else
                            adapter.notifyDataSetChanged();

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
}