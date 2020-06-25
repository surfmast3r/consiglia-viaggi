package com.surfmaster.consigliaviaggi.models.DAO;

import com.surfmaster.consigliaviaggi.models.DAO.ReviewDao;
import com.surfmaster.consigliaviaggi.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDaoStub implements ReviewDao {

    @Override
    public List<Review> getReviewList(int accommodationId) {
        List<Review> reviewList=createReviewList(accommodationId);
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

    private List<Review> createReviewList(int id) {
        List<Review> reviewList=new ArrayList<>();
        for (int i=0; i<10;i++){
            String sDate=(i+1)+"/12/2020";
            reviewList.add(new Review.Builder()
                    .setAuthor("Paolo")
                    .setReviewText("Peppino è na schifezz "+i)
                    .setRating((float) (1 + Math.random() * (5 - 1)))
                    .setData(sDate)
                    .build()
            );

        }

        return reviewList;
    }
}
