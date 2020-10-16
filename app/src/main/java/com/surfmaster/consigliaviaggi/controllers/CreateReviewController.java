package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DAO.ReviewDao;
import com.surfmaster.consigliaviaggi.models.DAO.ReviewDaoJSON;
import com.surfmaster.consigliaviaggi.models.Review;

public class CreateReviewController {

    private Context context;
    private ReviewDao reviewDao;

    public CreateReviewController(Context context){
        this.context= context;
        reviewDao=new ReviewDaoJSON();
    }

    public Boolean createReview(Review review){
        ManageUserController manageUserController=new ManageUserController(context);
        review.setIdUser(manageUserController.getUserId());

        try {
            return reviewDao.postReview(review,manageUserController.getToken());
        } catch (DaoException e) {
            postToastMessage(e.getMessage());
            return false;
        }

    }

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }


}
