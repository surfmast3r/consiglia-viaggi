package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.surfmaster.consigliaviaggi.AccommodationRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.R;

import java.util.List;

public class AccommodationListFragment extends Fragment{

    private AccommodationListViewModel accommodationListViewModel;
    private RecyclerView rv;
    private TextView textView;
    private Activity activity;
    private String category;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_view_accommodations, menu);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationListViewModel =
                ViewModelProviders.of(this).get(AccommodationListViewModel.class);


        View root = inflater.inflate(R.layout.fragment_accommodation_list, container, false);


        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);

        category=AccommodationListFragmentArgs.fromBundle(getArguments()).getAccommodationCategory();

        accommodationListViewModel.setAccommodationList(category,"Napoli");

        textView = root.findViewById(R.id.text_send);
        accommodationListViewModel.getCategoryText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        rv = root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        accommodationListViewModel.getList().observe(this, new Observer<List>() {

            AccommodationRecyclerViewAdapter adapter;
            @Override
            public void onChanged(@Nullable List s) {
                if (adapter==null){
                    adapter=new AccommodationRecyclerViewAdapter(getContext(),s);
                    rv.setAdapter(adapter);
                    stopShimmerAnimation();
                }
                else {
                    adapter.refreshList(s);
                    stopShimmerAnimation();
                }

            }
        }
        );

        return root;
    }

    private void stopShimmerAnimation() {
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                // Create and show the dialog.

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = FiltersFragment.newInstance();

                newFragment.show(ft, "dialog");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override

    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();

    }



    @Override

    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();

    }

}