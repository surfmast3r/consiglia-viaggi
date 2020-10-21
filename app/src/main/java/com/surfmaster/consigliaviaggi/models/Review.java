package com.surfmaster.consigliaviaggi.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


public class Review implements Comparable<Review>{

    private Integer idUser;
    private String author;
    private String content;
    private String accommodationName;
    private float rating;
    private final String data;
    private int idAccommodation;

    public Review(Builder builder) {
        this.author=builder.author;
        this.content =builder.reviewText;
        this.rating=builder.rating;
        this.data= builder.data;
        this.idAccommodation =builder.accommodationId;
        this.idUser =builder.userId;
        this.accommodationName=builder.accommodationName;


    }

    // SI PUO AGGIUNGERE L'ORARIO?
    @Override
    public int compareTo(Review o) {
        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(data);
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(o.getData());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static class ReviewRatingComparator implements Comparator<Review> {
        @Override
        public int compare(Review review1, Review review2) {
            return Float.compare(review1.getRating(), review2.getRating());
        }
    }

    public static class Builder {

        private String accommodationName;
        private Integer userId;
        private String data;
        private String author;
        private String reviewText;
        private float rating;
        private int accommodationId;

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

        public Builder setAccommodationName(String accommodationName) {
            this.accommodationName = accommodationName;
            return this;
        }

        public Builder setAccommodationId(int accommodationId) {
            this.accommodationId = accommodationId;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getData() {
        return data;
    }

    public float getRating() {
        return rating;
    }

    public int getIdAccommodation() {
        return idAccommodation;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setIdAccommodation(int idAccommodation) {
        this.idAccommodation = idAccommodation;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }
}
