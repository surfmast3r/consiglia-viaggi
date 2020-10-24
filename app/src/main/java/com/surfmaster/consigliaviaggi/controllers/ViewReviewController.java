package com.surfmaster.consigliaviaggi.controllers;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DAO.ReviewDaoFactory;
import com.surfmaster.consigliaviaggi.models.Review;
import com.surfmaster.consigliaviaggi.models.DAO.ReviewDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewReviewController {

    private ReviewDao reviewDao;
    public ViewReviewController() {

        reviewDao= ReviewDaoFactory.getReviewDao();
    }

    public List<Review> getReviewList(int id) {
        List<Review> reviewList= null;
        try {
            reviewList = reviewDao.getReviewList(id);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    public List<Review>  orderReviewListByDate(List<Review>  reviewList){
            Collections.sort(reviewList, Collections.<Review>reverseOrder());
        return reviewList;
    }
    public List<Review>  orderReviewListByRating(List<Review>  reviewList, int order){
        if(order== Constants.ASCENDING)
            Collections.sort(reviewList, new Review.ReviewRatingComparator());
        else if (order == Constants.DESCENDING)
            Collections.sort(reviewList, Collections.reverseOrder (new Review.ReviewRatingComparator()));
        return reviewList;
    }

    public List<Review>  filterReviewList(List<Review> reviewList, float minRating, float maxRating) {

        List<Review>  filteredList = new ArrayList<>();
        for(Review review : reviewList){
            if(review.getRating()>=minRating&&review.getRating()<=maxRating)
                filteredList.add(review);
        }
        return filteredList;
    }

}
