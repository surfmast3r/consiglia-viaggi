package com.surfmaster.consigliaviaggi.ui.login;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

public class SignupFragment extends Fragment {

    private LoginViewModel loginViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);



        return root;
    }
}
