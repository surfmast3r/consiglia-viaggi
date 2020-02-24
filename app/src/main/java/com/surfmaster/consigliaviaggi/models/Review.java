package com.surfmaster.consigliaviaggi.models;

public class Review {

    private String author;
    private String reviewText;
    private float rating;

    public Review(Builder builder) {
        this.author=builder.author;
        this.reviewText=builder.reviewText;
        this.rating=builder.rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }

    static class Builder {

        private String author;
        private String reviewText;
        private float rating;

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
        public Review build() {
            return new Review(this);
        }
    }
}
