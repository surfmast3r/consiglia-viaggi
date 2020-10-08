package com.surfmaster.consigliaviaggi.ui.account;

import android.content.Context;

import com.surfmaster.consigliaviaggi.controllers.AuthenticationController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> loggedIn;
    private AuthenticationController authenticationController;

    public LoginViewModel(){
        loggedIn=new MutableLiveData<>();
        loggedIn.setValue(false);
        authenticationController= new AuthenticationController();
    }
    public void loginButtonClickAction(final Context context, final String user, final String pwd) {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response=authenticationController.authenticate(context,user,pwd);
                loggedIn.postValue(response);
            }
        });

    }

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public String getUserName(Context applicationContext) {
        return authenticationController.getUserName(applicationContext);
    }

    public void tryLogin(final Context context) {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response=authenticationController.tryLogin(context);
                loggedIn.postValue(response);
            }
        });

    }
}
