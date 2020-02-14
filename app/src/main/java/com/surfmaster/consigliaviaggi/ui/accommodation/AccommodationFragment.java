package com.surfmaster.consigliaviaggi.ui.accommodation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.surfmaster.consigliaviaggi.R;

public class AccommodationFragment extends Fragment {

    private AccommodationViewModel accommodationViewModel;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationViewModel =
                ViewModelProviders.of(this).get(AccommodationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accommodation_detail, container, false);
        final TextView textView = root.findViewById(R.id.text_accommodation_datail);
        accommodationViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_filter);
        menuItem.setVisible(false);
    }



}