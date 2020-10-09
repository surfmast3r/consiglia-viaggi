package com.surfmaster.consigliaviaggi.ui.account;

import android.app.Application;
import com.surfmaster.consigliaviaggi.controllers.AuthenticationController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> loggedIn;
    private AuthenticationController authenticationController;

    public LoginViewModel(Application application){
        super(application);
        loggedIn=new MutableLiveData<>();
        authenticationController= new AuthenticationController(application);
    }
    public void loginButtonClickAction( final String user, final String pwd) {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response=authenticationController.authenticate(user,pwd);
                loggedIn.postValue(response);
            }
        });

    }

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public String getUserName() {
        return authenticationController.getUserName();
    }

    public void tryLogin() {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response=authenticationController.tryLogin();
                loggedIn.postValue(response);
            }
        });

    }
}
