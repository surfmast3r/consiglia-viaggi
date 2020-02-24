package com.surfmaster.consigliaviaggi.models;

import java.util.ArrayList;
import java.util.List;

public class ReviewDaoStub implements ReviewDao{

    @Override
    public List<Review> getReviewList(int id) {
        List reviewList=createReviewList(id);
        return reviewList;
    }

    @Override
    public Review getReviewById(int id) {
        return null;
    }

    @Override
    public boolean postReview(Review review) {
        return false;
    }

    private List createReviewList(int id) {
        List reviewList=new ArrayList();
        for (int i=0; i<10;i++){
            reviewList.add(new Review.Builder()
                    .setAuthor("Paolo")
                    .setReviewText("Peppino è na schifezz "+i)
                    .setRating((float) (1 + Math.random() * (5 - 1)))
                    .build()
            );
        }

        return reviewList;
    }
}
