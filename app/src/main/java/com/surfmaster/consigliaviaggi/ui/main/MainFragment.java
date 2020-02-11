package com.surfmaster.consigliaviaggi.ui.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surfmaster.consigliaviaggi.AccommodationRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.R;

public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        mainViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        RecyclerView rv = (RecyclerView)root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        AccommodationRecyclerViewAdapter adapter=new AccommodationRecyclerViewAdapter(getContext(),mainViewModel.getList());
        rv.setAdapter(adapter);
        return root;
    }
}