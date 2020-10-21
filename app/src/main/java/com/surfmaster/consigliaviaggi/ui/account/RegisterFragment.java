package com.surfmaster.consigliaviaggi.ui.account;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText pwdEditText;
    private EditText reEnterPwdEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel = ViewModelProviders.of(requireActivity()).get(RegisterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        usernameEditText = root.findViewById(R.id.usernameEditText);
        nameEditText = root.findViewById(R.id.nameEditText);
        surnameEditText = root.findViewById(R.id.surnameEditText);
        emailEditText = root.findViewById(R.id.emailEditText);
        pwdEditText = root.findViewById(R.id.pwdEditText);
        reEnterPwdEditText = root.findViewById(R.id.reEnterPwdEditText);

        Button registerButton = root.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    registerViewModel.setUsername(usernameEditText.getText().toString());
                    registerViewModel.setName(nameEditText.getText().toString());
                    registerViewModel.setSurname(surnameEditText.getText().toString());
                    registerViewModel.setEmail(emailEditText.getText().toString());
                    registerViewModel.setPwd(pwdEditText.getText().toString());
                    registerViewModel.registerUser();
                }

            }
        });

        registerViewModel.getResponse().observe(getViewLifecycleOwner(),new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean response) {

                    if (response) {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
                        registerViewModel.getResponse().setValue(false);
                    }


            }
        });

        enableLoginLink(root);

        return root;
    }

    private boolean validateFields() {
       boolean valid=true;
        if(isEmpty(usernameEditText))
        {
            usernameEditText.setError("Empty");
            valid= false;
        }
        if(isEmpty(nameEditText))
        {
            nameEditText.setError("Empty");
            valid= false;
        }

        if(isEmpty(surnameEditText))
        {
            surnameEditText.setError("Empty");
            valid= false;
        }
        if(isEmpty(emailEditText))
        {
            emailEditText.setError("Empty");
            valid= false;
        }
        if(isEmpty(pwdEditText))
        {
            pwdEditText.setError("Empty");
            valid= false;
        }
        if(isEmpty(reEnterPwdEditText))
        {
            reEnterPwdEditText.setError("Empty");
            valid= false;
        }
        if(!pwdEditText.getText().toString().equals(reEnterPwdEditText.getText().toString()))
        {
            reEnterPwdEditText.setError("Password need to match");
            valid= false;

        }
        return valid;
    }

    private void enableLoginLink(View root){
        TextView loginTextView = root.findViewById(R.id.login_link);
        loginTextView.setClickable(true);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
            }

        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() <= 0;
    }
}
