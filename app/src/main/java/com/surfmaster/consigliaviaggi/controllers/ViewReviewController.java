package com.surfmaster.consigliaviaggi.controllers;

import com.surfmaster.consigliaviaggi.models.ReviewDao;
import com.surfmaster.consigliaviaggi.models.ReviewDaoStub;

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
}
