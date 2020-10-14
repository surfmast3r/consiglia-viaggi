package com.surfmaster.consigliaviaggi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.surfmaster.consigliaviaggi.ui.accommodation.AccommodationViewModel;
import com.surfmaster.consigliaviaggi.ui.review.ReviewViewModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ViewAccommodationActivity extends AppCompatActivity {


    private AccommodationViewModel accommodationViewModel;
    private ReviewViewModel reviewViewModel;
    private int accommodationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        accommodationViewModel = ViewModelProviders.of(this).get(AccommodationViewModel.class);

        setContentView(R.layout.activity_view_accommodation);


        accommodationId = ViewAccommodationActivityArgs.fromBundle(getIntent().getExtras()).getAccommodationId();
        accommodationViewModel.setAccommodation(accommodationId);

        //Set Accommodation Id into ReviewViewModel
        reviewViewModel.setAccommodationId(accommodationId);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_view_accommodations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                // Create and show the dialog.
                /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogFragment newFragment = FiltersFragment.newInstance();
                newFragment.show(ft, "dialog");*/
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}
