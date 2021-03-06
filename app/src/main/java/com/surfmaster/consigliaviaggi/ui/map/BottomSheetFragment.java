package com.surfmaster.consigliaviaggi.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private ImageView logoImageView;
    private RatingBar ratingBar;
    private TextView titleTextView;
    private TextView addressTextView;
    private TextView categoryTextView;
    private TextView ratingTextView;
    private AppCompatButton detailButton;
    private String logo;
    private String title;
    private String address;
    private float rating;
    private int id;
    private String category;

    public BottomSheetFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null) {
            title = args.getString("title");
            address = args.getString("address");
            rating = args.getFloat("rating");
            id = args.getInt("id");
            category = args.getString("category");
            logo = args.getString("logo");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.map_bottom_sheet, container, false);

        logoImageView= view.findViewById(R.id.bottom_sheet_logo);
        titleTextView = view.findViewById(R.id.bottom_sheet_title);
        addressTextView = view.findViewById(R.id.bottom_sheet_address);
        categoryTextView = view.findViewById(R.id.bottom_sheet_category);
        ratingBar= view.findViewById(R.id.bottom_sheet_rating);
        ratingTextView = view.findViewById(R.id.bottom_sheet_rating_text);
        detailButton = view.findViewById(R.id.detail_button);
        setBottomSheetData();
        return view;
    }

    private void setBottomSheetData(){
        titleTextView.setText(title);
        addressTextView.setText(address);
        categoryTextView.setText(category);
        ratingBar.setRating(rating);
        ratingTextView.setText(String.valueOf(rating));
        if (logo.isEmpty()) {
            logoImageView.setImageResource(R.drawable.placeholder);
        } else{
            Picasso.get().load(logo)
                    .placeholder(requireContext().getResources().getDrawable(R.drawable.placeholder,null))
                    .error(requireContext().getResources()
                            .getDrawable(R.drawable.placeholder,null)).
                    into(logoImageView);
        }


        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccommodationMapFragmentDirections.ActionNavMapToNavViewAccommodationActivity action =
                        AccommodationMapFragmentDirections.actionNavMapToNavViewAccommodationActivity(id);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
            }
        });
    }
}