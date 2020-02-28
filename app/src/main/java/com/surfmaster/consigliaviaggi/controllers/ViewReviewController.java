package com.surfmaster.consigliaviaggi.controllers;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.Review;
import com.surfmaster.consigliaviaggi.models.ReviewDao;
import com.surfmaster.consigliaviaggi.models.ReviewDaoStub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewReviewController {

    private ReviewDao reviewDao;
    public ViewReviewController() {

        reviewDao= new ReviewDaoStub();
    }

    public List getReviewList(int id) {
        List reviewList= reviewDao.getReviewList(id);
        return reviewList;
    }

    // create sublist of length size
    public List reviewSubList(List reviewList, final int size) {
        List sublist = new ArrayList();
        for(int i=0;i<size;i++){
            sublist.add(reviewList.get(i));
        }
        return sublist;
    }

    public List orderReviewListByDate(List reviewList){
            Collections.sort(reviewList, Collections.reverseOrder());
        return reviewList;
    }
    public List orderReviewListByRating(List reviewList, int order){
        if(order== Constants.ASCENDING)
            Collections.sort(reviewList, new Review.ReviewRatingComparator());
        else if (order == Constants.DESCENDING)
            Collections.sort(reviewList, Collections.reverseOrder (new Review.ReviewRatingComparator()));
        return reviewList;
    }

    public List filterReviewList(List<Review> reviewList, float minRating, float maxRating) {

        List filteredList = new ArrayList();
        for(Review review : reviewList){
            if(review.getRating()>minRating&&review.getRating()<maxRating)
                filteredList.add(review);
        }
        return filteredList;
    }
}
