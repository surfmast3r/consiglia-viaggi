package com.surfmaster.consigliaviaggi.models.DAO;

import com.surfmaster.consigliaviaggi.models.Review;

import java.util.List;


public interface ReviewDao {
     List<Review> getReviewList(int accommodationId) throws DaoException;
     Review getReviewById(int id);
     Review postReview(Review review) throws DaoException;
}
