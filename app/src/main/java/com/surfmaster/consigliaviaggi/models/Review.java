package com.surfmaster.consigliaviaggi.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


public class Review implements Comparable{

    private String author;
    private String reviewText;
    private float rating;
    private String data;
    private int accommodationId;
    private String accommodationName;

    public Review(Builder builder) {
        this.author=builder.author;
        this.reviewText=builder.reviewText;
        this.rating=builder.rating;
        this.data= builder.data;
        this.accommodationId=builder.accommodationId;
        this.accommodationName=builder.accommodationName;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getData() {
        return data;
    }

    public float getRating() {
        return rating;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    @Override
    public int compareTo(Object o) {
        Review review = (Review) o ;
        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(data);
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(review.getData());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static class Builder {

        private String data;
        private String author;
        private String reviewText;
        private float rating;
        private int accommodationId;
        private String accommodationName;

        public Builder setReviewText(String reviewText) {
            this.reviewText = reviewText;
            return this;
        }

        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }
        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setAccommodationId(int accommodationId) {
            this.accommodationId = accommodationId;
            return this;
        }

        public Builder setAccommodationName(String accommodationName) {
            this.accommodationName = accommodationName;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    public static class ReviewRatingComparator implements Comparator<Review> {
        @Override
        public int compare(Review review1, Review review2) {
            return Float.compare(review1.getRating(), review2.getRating());
        }
    }
}
