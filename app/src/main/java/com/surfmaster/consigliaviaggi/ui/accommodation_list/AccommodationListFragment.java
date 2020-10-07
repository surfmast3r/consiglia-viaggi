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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.surfmaster.consigliaviaggi.AccommodationRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.Category;
import com.surfmaster.consigliaviaggi.models.SearchParamsAccommodation;

import java.util.List;

public class AccommodationListFragment extends Fragment{

    private AccommodationListViewModel accommodationListViewModel;
    private AccommodationFiltersViewModel accommodationFiltersViewModel;
    private RecyclerView rv;
    private TextView categoryTextView;
    private Integer sortOrder;
    private float minRating;
    private float maxRating;
    private String category;
    private String city;
    private SearchParamsAccommodation currentSearchParams;
    private String currentCategory;
    private ShimmerFrameLayout mShimmerViewContainer;
    private AccommodationRecyclerViewAdapter adapter;
    //endless scroll
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    //endless scroll end

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

        updateAccommodationList(currentSearchParams);
        return root;
    }

    private void bindViews(View root) {
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);

        categoryTextView = root.findViewById(R.id.category_text_view);
        accommodationFiltersViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category category) {
               // if(!category.getCategoryName().equals(currentSearchParams.getCurrentCategory())) {
                    categoryTextView.setText(category.getCategoryLabel());
                    //currentSearchParams.setCurrentCategory(category.getCategoryName());
                    //updateAccommodationList(currentSearchParams);
                    //currentCategory=category.getCategoryName();

                //}
            }
        });

        rv = root.findViewById(R.id.accommodation_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
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
        accommodationListViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List>() {

                    @Override
                    public void onChanged(@Nullable List s) {
                        if (adapter==null){
                            adapter=new AccommodationRecyclerViewAdapter(getContext(),s);
                            rv.setAdapter(adapter);
                            stopShimmerAnimation();
                            previousTotal=0;
                        }
                        else {
                            adapter.refreshList(s);
                            stopShimmerAnimation();
                            previousTotal=0;
                        }

                    }
                }
        );

        accommodationFiltersViewModel.getCurrentSearchParams().observe(getViewLifecycleOwner(), new Observer<SearchParamsAccommodation>() {
            @Override
            public void onChanged(SearchParamsAccommodation searchParamsAccommodation) {
                updateAccommodationList( searchParamsAccommodation);
            }
        });

        /*
        accommodationFiltersViewModel.getSortParam().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                    sortAccommodationList(s);
            }
        });

        accommodationFiltersViewModel.getMinRating().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float min) {
                if(!min.equals(minRating)) {
                    minRating=min;
                    filterAccommodationList(minRating,maxRating);
                }

            }
        });
        accommodationFiltersViewModel.getMaxRating().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float max) {
                if(!max.equals(maxRating)) {
                    maxRating = max;
                    filterAccommodationList(minRating, maxRating);
                }
            }
        });

         */

    }

    private void filterAccommodationList(float minRating, float maxRating) {
        accommodationListViewModel.filterAccommodationList(minRating,maxRating);
    }

    private void sortAccommodationList(Integer order) {
        if(!order.equals(sortOrder)) {
            accommodationListViewModel.orderAccommodationList(order);
            sortOrder=order;
        }
    }

    private void updateAccommodationList(SearchParamsAccommodation searchParams) {
        if (adapter!=null)
            adapter.clearList();
        sortOrder=Constants.DEFAULT_ORDER;
        minRating=Constants.DEFAULT_MIN_RATING;
        maxRating=Constants.DEFAULT_MAX_RATING;
        startShimmerAnimation();
        accommodationListViewModel.setAccommodationList(searchParams);
    }


    private void initFilters() {
        sortOrder=Constants.DEFAULT_ORDER;
        minRating=Constants.DEFAULT_MIN_RATING;
        maxRating=Constants.DEFAULT_MAX_RATING;
        accommodationFiltersViewModel.setCategory(category);

        //accommodationFiltersViewModel.setMinRating(minRating);
        //accommodationFiltersViewModel.setMaxRating(maxRating);

        //accommodationFiltersViewModel.setSortParam(Constants.DEFAULT_ORDER);

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