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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surfmaster.consigliaviaggi.AccommodationRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.R;

import java.util.List;

public class AccommodationListFragment extends Fragment {

    private AccommodationListViewModel accommodationListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationListViewModel =
                ViewModelProviders.of(this).get(AccommodationListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accommodation_list, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        accommodationListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final RecyclerView rv = (RecyclerView)root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        accommodationListViewModel.getList().observe(this, new Observer<List>() {

            AccommodationRecyclerViewAdapter adapter;
            @Override
            public void onChanged(@Nullable List s) {
                if (adapter==null){
                    adapter=new AccommodationRecyclerViewAdapter(getContext(),s);
                    rv.setAdapter(adapter);
                }
                else
                    adapter.notifyDataSetChanged();

            }
        }
        );


        return root;
    }
}