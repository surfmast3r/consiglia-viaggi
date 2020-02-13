package com.surfmaster.consigliaviaggi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.models.Accommodation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
            cv = (CardView) itemView.findViewById(R.id.cv);
            accommodationName = (TextView) itemView.findViewById(R.id.accommodation_name);
            accommodationAddress = (TextView) itemView.findViewById(R.id.accommodation_address);
            accommodationImage = (ImageView) itemView.findViewById(R.id.accommodation_image);
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
    public void onBindViewHolder(@NonNull AccommodationViewHolder accommodationViewHolder, int position) {

        accommodationViewHolder.accommodationName.setText(accommodations.get(position).getName());
        accommodationViewHolder.accommodationAddress.setText(accommodations.get(position).getAddress());
        Picasso.get().load(accommodations.get(position).getImages().get(0)).into(accommodationViewHolder.accommodationImage);
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
