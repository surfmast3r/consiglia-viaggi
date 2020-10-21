package com.surfmaster.consigliaviaggi.ui.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.surfmaster.consigliaviaggi.controllers.RegistrationController;
import com.surfmaster.consigliaviaggi.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterViewModel extends AndroidViewModel {

    private RegistrationController registrationController;
    private MutableLiveData<Boolean>mResponse;
    private MutableLiveData<String>mUsername;
    private MutableLiveData<String>mName;
    private MutableLiveData<String>mEmail;
    private MutableLiveData<String>mPwd;
    private MutableLiveData<String>mSurname;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registrationController=new RegistrationController(application);
        mResponse=new MutableLiveData<>(false);
        mUsername=new MutableLiveData<>();
        mName=new MutableLiveData<>();
        mEmail=new MutableLiveData<>();
        mPwd=new MutableLiveData<>();
        mSurname=new MutableLiveData<>();
    }

    public void registerUser() {

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response= registrationController.registerUser(new User.Builder()
                        .setNickname(mUsername.getValue())
                        .setName(mName.getValue())
                        .setSurname(mSurname.getValue())
                        .setEmail(mEmail.getValue())
                        .setPwd(mPwd.getValue())
                        .create());
                mResponse.postValue(response);
            }
        });
    }

    public MutableLiveData<Boolean> getResponse() {
        return mResponse;
    }

    public void setUsername(String mUsername) {
        this.mUsername.setValue(mUsername);
    }

    public void setName(String mName) {
        this.mName.setValue(mName);
    }
    public void setEmail(String mEmail) {
        this.mEmail.setValue(mEmail);
    }

    public void setPwd(String mPwd) {
        this.mPwd.setValue(mPwd);
    }
    public void setSurname(String mSurname) {
        this.mSurname.setValue(mSurname);
    }
}
