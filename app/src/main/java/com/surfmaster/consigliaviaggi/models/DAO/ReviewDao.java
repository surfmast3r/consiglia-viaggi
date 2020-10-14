package com.surfmaster.consigliaviaggi.models.DAO;

import com.surfmaster.consigliaviaggi.models.Review;

import java.util.List;


public interface ReviewDao {
     List<Review> getReviewList(int accommodationId) throws DaoException;
     Review getReviewById(int id);
     Boolean postReview(Review review, String token) throws DaoException;
}
