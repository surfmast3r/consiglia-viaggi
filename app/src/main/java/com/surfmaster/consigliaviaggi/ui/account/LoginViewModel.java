package com.surfmaster.consigliaviaggi.ui.account;

import android.app.Application;
import com.surfmaster.consigliaviaggi.controllers.AuthenticationController;
import com.surfmaster.consigliaviaggi.controllers.ManageUserController;
import com.surfmaster.consigliaviaggi.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> loggedIn;
    private AuthenticationController authenticationController;
    private ManageUserController manageUserController;
    private MutableLiveData<String>name,surname, nickname,email;
    private MutableLiveData<Boolean>showUsername;

    public LoginViewModel(Application application){
        super(application);
        loggedIn=new MutableLiveData<>();
        authenticationController= new AuthenticationController(application);
        manageUserController=new ManageUserController(application);
        name=new MutableLiveData<>();
        surname=new MutableLiveData<>();
        nickname =new MutableLiveData<>();
        email=new MutableLiveData<>();
        showUsername=new MutableLiveData<>();

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
        return manageUserController.getUserName();
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

    public void getUserData(){
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                User user=manageUserController.getUserDetails(manageUserController.getUserId());
                name.postValue(user.getNome());
                nickname.postValue(user.getNickname());
                email.postValue(user.getEmail());
                surname.postValue(user.getCognome());
                showUsername.postValue(user.getShowNickname());
            }
        });
    }

    public void logoutButtonClickAction() {
        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                authenticationController.logout();
                loggedIn.postValue(false);
            }
        });

    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getSurname() {
        return surname;
    }

    public MutableLiveData<String> getNickname() {
        return nickname;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<Boolean> getShowUsername() {
        return showUsername;
    }
}
