package com.surfmaster.consigliaviaggi.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.android.material.snackbar.Snackbar;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Review;
import com.surfmaster.consigliaviaggi.ui.accommodation.AccommodationViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class CreateReviewFragment extends Fragment {

    private RatingBar ratingBar;
    private ReviewViewModel reviewViewModel;
    private AppCompatEditText reviewEditText;
    private AppCompatButton publishButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        reviewViewModel =
                ViewModelProviders.of(requireActivity()).get(ReviewViewModel.class);


        View root = inflater.inflate(R.layout.fragment_create_review, container, false);

        bindViews(root);

        initToolbar(root);

        return root;
    }

    private void bindViews(View root){

        ratingBar = root.findViewById(R.id.min_rating_filter);
        publishButton = root.findViewById(R.id.publish_button);
        reviewEditText = root.findViewById(R.id.review_text);

        publishButton.setOnClickListener(createPublishButtonClickListener());
    }

    private View.OnClickListener createPublishButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with publish review action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        };
    }

    private void initToolbar(View root) {
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)getActivity()), navController,appBarConfiguration);
    }
}
