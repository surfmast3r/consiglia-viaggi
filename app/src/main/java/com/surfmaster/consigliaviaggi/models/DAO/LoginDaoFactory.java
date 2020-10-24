package com.surfmaster.consigliaviaggi.models.DAO;

import com.surfmaster.consigliaviaggi.Constants;

public class LoginDaoFactory {
    public static LoginDao getLoginDao(Integer loginType){
        /*Logica per scegliere il dao*/

        if(loginType.equals(Constants.FACEBOOK_USER))
            return new LoginDaoFacebook();
        else
            return  new LoginDaoSpring();
    }
}
