package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Activity;
import android.content.Context;
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
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;

import java.util.List;

public class AccommodationListFragment extends Fragment{

    private AccommodationListViewModel accommodationListViewModel;
    private AccommodationFiltersViewModel accommodationFiltersViewModel;
    private RecyclerView rv;
    private TextView textView;
    private String sortOrder;
    private Activity activity;
    private String category;
    private String currentCategory;
    private ShimmerFrameLayout mShimmerViewContainer;
    private AccommodationRecyclerViewAdapter adapter;

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

        accommodationFiltersViewModel =
                ViewModelProviders.of(requireActivity()).get(AccommodationFiltersViewModel.class);

        currentCategory="";
        category=AccommodationListFragmentArgs.fromBundle(getArguments()).getAccommodationCategory();

        View root = inflater.inflate(R.layout.fragment_accommodation_list, container, false);

        bindViews(root);

        initFilters(category);

        return root;
    }

    private void bindViews(View root) {
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);

        textView = root.findViewById(R.id.text_send);
        accommodationFiltersViewModel.getCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!s.equals(currentCategory)) {
                    textView.setText(s);
                    updateAccommodationList(s, "Napoli");
                    currentCategory=s;
                }
            }
        });

        rv = root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        accommodationListViewModel.getList().observe(this, new Observer<List>() {

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

        accommodationFiltersViewModel.getSortParam().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.equals(sortOrder)) {
                    sortAccommodationList(s);
                    sortOrder=s;
                }
            }
        });


    }

    private void sortAccommodationList(String order) {
        accommodationListViewModel.orderAccommodationList(order);
    }

    private void updateAccommodationList(String category, String city) {
        if (adapter!=null)
            adapter.clearList();
        sortOrder=Constants.DEFAULT;
        startShimmerAnimation();
        accommodationListViewModel.setAccommodationList(category,city);
    }


    private void initFilters(String category) {
        sortOrder=Constants.DEFAULT;
        accommodationFiltersViewModel.setCategory(category);
        accommodationFiltersViewModel.setSortParam(Constants.DEFAULT);

    }

    private void stopShimmerAnimation() {
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
    }
    private void startShimmerAnimation() {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
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