package com.surfmaster.consigliaviaggi.ui.account;

import android.app.Application;
import com.surfmaster.consigliaviaggi.controllers.RegistrationController;
import com.surfmaster.consigliaviaggi.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class RegisterViewModel extends AndroidViewModel {

    private RegistrationController registrationController;
    private MutableLiveData<Boolean>mResponse;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registrationController=new RegistrationController(application);
        mResponse=new MutableLiveData<>(false);
    }

    public void registerUser(final User user) {

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                boolean response= registrationController.registerUser(user);;
                mResponse.postValue(response);
            }
        });
    }

    public MutableLiveData<Boolean> getResponse() {
        return mResponse;
    }
}
