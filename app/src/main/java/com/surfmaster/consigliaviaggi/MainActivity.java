package com.surfmaster.consigliaviaggi;

import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.surfmaster.consigliaviaggi.ui.account.LoginViewModel;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private LoginViewModel loginViewModel;
    private TextView userTextView;
    private MenuItem loginMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Facebook Init
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.tryLogin();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        userTextView= navigationView.getHeaderView(0).findViewById(R.id.usernameTextView);
        loginMenuItem= navigationView.getMenu().findItem(R.id.nav_login);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_tools);
            }
        });

        loginViewModel.getLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean logged) {
                if(logged){
                    userTextView.setText("Benvenuto "+loginViewModel.getUserName());
                    loginMenuItem.setTitle("My Account");
                }else{
                    userTextView.setText(R.string.no_access);
                    loginMenuItem.setTitle(R.string.menu_login);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() != R.id.nav_accommodation_detail) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);
        }
    }
}
