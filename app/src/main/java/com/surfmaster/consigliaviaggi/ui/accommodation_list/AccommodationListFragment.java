package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.surfmaster.consigliaviaggi.R;

public class AccommodationListFragment extends Fragment {

    private AccommodationListViewModel accommodationListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationListViewModel =
                ViewModelProviders.of(this).get(AccommodationListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        accommodationListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}