package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DAO.LocalUserDao;
import com.surfmaster.consigliaviaggi.models.DAO.LocalUserDaoFactory;
import com.surfmaster.consigliaviaggi.models.DAO.LoginDao;
import com.surfmaster.consigliaviaggi.models.DAO.LoginDaoFactory;
import java.io.IOException;

public class AuthenticationController {

    private Context context;
    private LocalUserDao localUserDao;
    private LoginDao loginDao;

    public AuthenticationController(Context context){
        this.context=context;
        localUserDao = LocalUserDaoFactory.getLocalUserDao(context);
    }

    public boolean tryLogin() {
        String userName = localUserDao.getUserName();
        String pwd = localUserDao.getUserPwd();
        Integer userType=localUserDao.getType();

        if (userType.equals(Constants.NORMAL_USER)){
            if (!(userName.isEmpty() || pwd.isEmpty())) {
                return serverLogin(userName, pwd);
            }
        }
        if (userType.equals(Constants.FACEBOOK_USER)){
            return socialLogin(localUserDao.getUserPwd(),Constants.FACEBOOK_USER);
        }

        return false;
    }
    public void logout() {
        localUserDao.logOutUser();
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

    //need type param because socialLogin could be used
    // by different type of social auth (Facebook,Google,..)
    public Boolean socialLogin(String token,Integer type)  {
        loginDao= LoginDaoFactory.getLoginDao(type);
        try {
            if(loginDao.authenticate(token,context))
            {
                localUserDao.saveUser(loginDao.getAuthenticatedUser());
                postToastMessage("Logged in");
                return true;
            }
        } catch (IOException e) {
            postToastMessage("Login error");
            return false;
        }
        postToastMessage("Login error");
        return false;
    }

    public Boolean serverLogin(String user, String pwd)  {
        loginDao= LoginDaoFactory.getLoginDao(Constants.NORMAL_USER);
        try {
            if(loginDao.authenticate(user,pwd,context))
            {
                localUserDao.saveUser(loginDao.getAuthenticatedUser());
                postToastMessage("Logged in");
                return true;
            }

        } catch (IOException e) {
            postToastMessage("Login error");
            return false;
        }
        postToastMessage("Login error");
        return false;
    }

}
