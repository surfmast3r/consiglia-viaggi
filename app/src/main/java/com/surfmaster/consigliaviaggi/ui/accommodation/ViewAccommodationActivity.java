package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.ui.review.ReviewViewModel;

public class ViewAccommodationActivity extends AppCompatActivity {


    private AccommodationViewModel accommodationViewModel;
    private ReviewViewModel reviewViewModel;
    private int accommodationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

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
