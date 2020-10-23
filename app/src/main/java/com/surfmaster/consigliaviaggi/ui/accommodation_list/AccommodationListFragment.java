package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.models.Category;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.List;

public class AccommodationListFragment extends Fragment{

    private AccommodationListViewModel accommodationListViewModel;
    private AccommodationFiltersViewModel accommodationFiltersViewModel;
    private RecyclerView rv;
    private TextView categoryTextView;
    private String category;
    private SearchParamsAccommodation currentSearchParams;
    private ShimmerFrameLayout mShimmerViewContainer;
    private AccommodationRecyclerViewAdapter adapter;
    //endless scroll
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private String city;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
        accommodationListViewModel = new ViewModelProvider(this).get(AccommodationListViewModel.class);

        accommodationFiltersViewModel = new ViewModelProvider(requireActivity()).get(AccommodationFiltersViewModel.class);

        if(getArguments()!=null) {
            category = AccommodationListFragmentArgs.fromBundle(getArguments()).getAccommodationCategory();
            city = AccommodationListFragmentArgs.fromBundle(getArguments()).getCity();
            currentSearchParams= new SearchParamsAccommodation.Builder()
                    .setCurrentCategory(category)
                    .setCurrentCity(city)
                    .create();

        }

        View root = inflater.inflate(R.layout.fragment_accommodation_list, container, false);

        bindViews(root);

        initFilters();

        //updateAccommodationList(currentSearchParams);
        return root;
    }

    private void bindViews(View root) {
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);

        categoryTextView = root.findViewById(R.id.category_text_view);
        accommodationFiltersViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<Category>() {
            @Override
            public void onChanged(Category category) {

                categoryTextView.setText(category.getCategoryLabel());

            }
        });

        rv = root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert llm != null;
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("Recyler Endless scroll", "end called");
                    accommodationListViewModel.loadMore();
                    // Do something

                    loading = true;
                }
            }
        });
        accommodationListViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<Accommodation>>() {

                    @Override
                    public void onChanged(@Nullable List<Accommodation> newList) {
                        if (newList!=null) {
                            if (adapter == null) {
                                adapter = new AccommodationRecyclerViewAdapter(getContext(), newList);
                                rv.setAdapter(adapter);
                            } else {
                                adapter.refreshList(newList);
                            }
                            stopShimmerAnimation();
                            previousTotal = 0;
                        }
                        else{
                            stopShimmerAnimation();
                        }

                    }
                }
        );

        accommodationFiltersViewModel.getCurrentSearchParams().observe(getViewLifecycleOwner(), new Observer<SearchParamsAccommodation>() {
            @Override
            public void onChanged(SearchParamsAccommodation searchParamsAccommodation) {
                updateAccommodationList(searchParamsAccommodation);
            }
        });


    }


    private void updateAccommodationList(SearchParamsAccommodation searchParams) {
        if (adapter!=null)
            adapter.clearList();
        startShimmerAnimation();
        accommodationListViewModel.setAccommodationList(searchParams);
    }


    private void initFilters() {
        accommodationFiltersViewModel.setCategory(category);
        accommodationFiltersViewModel.setCity(city);
        accommodationFiltersViewModel.setMinRating(Constants.DEFAULT_MIN_RATING);
        accommodationFiltersViewModel.setMaxRating(Constants.DEFAULT_MAX_RATING);
        accommodationFiltersViewModel.setSortParam(Constants.DEFAULT_ORDER);
        accommodationFiltersViewModel.applySearchParams();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            DialogFragment newFragment = FiltersFragment.newInstance();

            newFragment.show(ft, "dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
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