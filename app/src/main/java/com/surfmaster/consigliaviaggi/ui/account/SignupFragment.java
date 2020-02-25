package com.surfmaster.consigliaviaggi.ui.account;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class SignupFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView loginTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        enableSignupLink(root);


        return root;
    }

    private void enableSignupLink(View root){
        loginTextView = root.findViewById(R.id.link_login);
        loginTextView.setClickable(true);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
            }

        });
    }
}
