package com.surfmaster.consigliaviaggi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surfmaster.consigliaviaggi.models.Review;
import com.surfmaster.consigliaviaggi.ui.accommodation_list.AccommodationListFragmentDirections;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private Context context;

    public ReviewsRecyclerViewAdapter(Context context, List<Review> reviews){

        this.reviews = reviews;
        this.context=context;
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView reviewAuthorTextView;
        TextView reviewTextView;
        RatingBar reviewRatingBar;

        ReviewViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.review_card_view);
            reviewAuthorTextView =  itemView.findViewById(R.id.review_author);
            reviewTextView =  itemView.findViewById(R.id.review_text);
            reviewRatingBar =  itemView.findViewById(R.id.review_rating);
        }

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ReviewViewHolder avh = new ReviewViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, final int position) {

        reviewViewHolder.reviewAuthorTextView.setText(reviews.get(position).getAuthor());
        reviewViewHolder.reviewTextView.setText(reviews.get(position).getReviewText());
        reviewViewHolder.reviewRatingBar.setRating(reviews.get(position).getRating());

    }

    @Override
        public int getItemCount() {

            return reviews.size();
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
