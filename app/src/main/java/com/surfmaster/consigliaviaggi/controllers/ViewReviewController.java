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
    public List getFirstNReviewList(int id, int n) {
        List list = reviewDao.getReviewList(id);
        return list.subList(0,n);
    }
}
