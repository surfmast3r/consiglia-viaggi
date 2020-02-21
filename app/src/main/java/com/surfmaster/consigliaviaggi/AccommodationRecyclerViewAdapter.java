package com.surfmaster.consigliaviaggi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.models.Accommodation;
import com.surfmaster.consigliaviaggi.ui.accommodation_list.AccommodationListFragmentDirections;
import com.surfmaster.consigliaviaggi.ui.accommodation_list.AccommodationListViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class AccommodationRecyclerViewAdapter extends RecyclerView.Adapter<AccommodationRecyclerViewAdapter.AccommodationViewHolder> {

    private List<Accommodation> accommodations;
    private Context context;

    public AccommodationRecyclerViewAdapter(Context context,List<Accommodation> accommodations){

        this.accommodations = accommodations;
        this.context=context;
    }


    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView accommodationName;
        TextView accommodationAddress;
        ImageView accommodationImage;

        AccommodationViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            accommodationName =  itemView.findViewById(R.id.accommodation_name);
            accommodationAddress =  itemView.findViewById(R.id.accommodation_address);
            accommodationImage =  itemView.findViewById(R.id.accommodation_image);
        }

    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_list_item, parent, false);
        AccommodationViewHolder avh = new AccommodationViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder accommodationViewHolder, final int position) {

        accommodationViewHolder.accommodationName.setText(accommodations.get(position).getName());
        accommodationViewHolder.accommodationAddress.setText(accommodations.get(position).getAddress());
        Picasso.get().load(accommodations.get(position).getImages().get(0)).into(accommodationViewHolder.accommodationImage);
        accommodationViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccommodationListFragmentDirections.ActionNavAccommodationListToNavViewAccommodationActivity action =
                        AccommodationListFragmentDirections.actionNavAccommodationListToNavViewAccommodationActivity(accommodations.get(position).getId());
                Navigation.findNavController((Activity)context, R.id.nav_host_fragment).navigate(action);
            }
        });

    }

    @Override
        public int getItemCount() {

            return accommodations.size();
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
