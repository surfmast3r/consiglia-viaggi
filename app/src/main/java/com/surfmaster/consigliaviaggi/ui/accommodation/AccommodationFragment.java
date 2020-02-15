package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.navigation.ui.NavigationUI;

import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.R;

public class AccommodationFragment extends Fragment {

    private AccommodationViewModel accommodationViewModel;
    private Toolbar mainToolbar;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationViewModel =
                ViewModelProviders.of(this).get(AccommodationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accommodation_detail, container, false);

        initToolbar(root);

        final TextView textView = root.findViewById(R.id.text_accommodation_datail);
        accommodationViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ImageView accommodationImage = root.findViewById(R.id.collapsing_ac_image);
        Picasso.get().load("https://www.oasidellapizza.it/wp-content/uploads/revslider/steweysfullslider/5.jpg").into(accommodationImage);
        return root;
    }

    private void initToolbar(View root) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mainToolbar=getActivity().findViewById(R.id.toolbar);
        Toolbar toolbar = root.findViewById(R.id.collapsing_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity)getActivity()), navController);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        menuItem.setVisible(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }


}