package com.surfmaster.consigliaviaggi.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView createAccountTextView;
    private EditText userEditText;
    private EditText pwdEditText;
    private LinearLayout loginForm,accountPage;
    private AppCompatButton loginButton,logoutButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        loginForm=root.findViewById(R.id.login_form);
        accountPage=root.findViewById(R.id.account_page);

        userEditText = root.findViewById(R.id.input_username);
        pwdEditText = root.findViewById(R.id.input_password);
        logoutButton=root.findViewById(R.id.logoutButton);
        loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.loginButtonClickAction(userEditText.getText().toString(),pwdEditText.getText().toString());

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logoutButtonClickAction();
            }
        });



        loginViewModel.getLoggedIn().observe(getViewLifecycleOwner(),new Observer<Boolean>() {
            boolean init=true;
            @Override
            public void onChanged(Boolean result) {
                //if(!init) {
                    if (result) {
                        loginForm.setVisibility(View.GONE);
                        accountPage.setVisibility(View.VISIBLE);

                        //TODO:chiamata al server per avere i dati dell'utente
                    }
                    else
                    {
                        loginForm.setVisibility(View.VISIBLE);
                        accountPage.setVisibility(View.GONE);

                    }

                //}
                //init=false;
            }
        });
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
