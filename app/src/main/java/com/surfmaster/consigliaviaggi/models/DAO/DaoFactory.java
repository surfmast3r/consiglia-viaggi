package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;

import com.surfmaster.consigliaviaggi.Constants;

public class DaoFactory {

    public static AccommodationDao getAccommodationDao(){

        /*Logica per scegliere il dao*/
        return new AccommodationDaoJSON();

    }
    public static ReviewDao getReviewDao(){
        /*Logica per scegliere il dao*/
        return new ReviewDaoJSON();

    }

    public static LoginDao getLoginDao(Integer loginType){
        /*Logica per scegliere il dao*/
        if(loginType.equals(Constants.FACEBOOK_USER))
            return new LoginDaoFacebook();
        else
            return  new LoginDaoSpring();
    }

    public static LocalUserDao getLocalUserDao(Context context){
        /*Logica per scegliere il dao*/
        return new LocalUserDaoSharedPrefs(context);

    }
}
