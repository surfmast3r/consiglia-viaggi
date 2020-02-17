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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ViewAccommodationActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener{

    private AccommodationViewModel accommodationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accommodationViewModel =
                ViewModelProviders.of(this).get(AccommodationViewModel.class);

        setContentView(R.layout.activity_view_accommodation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int accommodationId=ViewAccommodationActivityArgs.fromBundle(getIntent().getExtras()).getAccommodationId();
        accommodationViewModel.setAccommodation(accommodationId);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
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
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() != R.id.nav_accommodation_detail) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);
        }
    }

}