package com.surfmaster.consigliaviaggi.models;

import java.util.List;


public interface ReviewDao {
     List<Review> getReviewList(int id);
     Review getReviewById(int id);
     boolean postReview(Review review);
}
