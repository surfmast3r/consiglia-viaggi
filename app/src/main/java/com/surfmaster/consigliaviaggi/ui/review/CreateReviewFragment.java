package com.surfmaster.consigliaviaggi.ui.review;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.surfmaster.consigliaviaggi.R;

public class CreateReviewFragment extends Fragment {

    private RatingBar ratingBar;
    private ReviewViewModel reviewViewModel;
    private AppCompatEditText reviewEditText;
    private AppCompatButton publishButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);


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

        reviewViewModel.getPostReviewResponse().observe(getViewLifecycleOwner(),new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean response) {
                if(response){
                    buildAlert("la recensione sarà pubblicata in seguito all'approvazione").show();
                    Snackbar.make(requireView(), "Recensione creata con successo", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    reviewViewModel.setPostReviewResponse(false);
                }
            }
        });
    }


    private View.OnClickListener createPublishButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewEditText.getText().toString().length()>4&&reviewEditText.getText().toString().length()<500)
                    reviewViewModel.postReview(ratingBar.getRating(),reviewEditText.getText().toString());
                else
                    Snackbar.make(requireView(), "Il testo della recensione deve essere compreso tra 5 e 500 caratteri", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            }
        };
    }

    private void initToolbar(View root) {
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)requireActivity()), navController,appBarConfiguration);
    }

    private AlertDialog buildAlert(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setMessage(s)
                .setTitle(R.string.review_info_alert);


        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
            }
        });


        return builder.create();


    }

}
