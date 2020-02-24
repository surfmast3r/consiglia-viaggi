package com.surfmaster.consigliaviaggi.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView createAccountTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        enableSignupLink(root);

        return root;
    }

    private void enableSignupLink(View root){
        createAccountTextView= root.findViewById(R.id.link_signup);
        createAccountTextView.setClickable(true);
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections destination = LoginFragmentDirections.actionNavLoginToNavSignup();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(destination);
            }

        });
    }
}
