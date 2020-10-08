package com.surfmaster.consigliaviaggi.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private AppCompatButton loginButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        userEditText = root.findViewById(R.id.input_username);
        pwdEditText = root.findViewById(R.id.input_password);
        loginButton = root.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.loginButtonClickAction(requireContext(),userEditText.getText().toString(),pwdEditText.getText().toString());

            }
        });

        loginViewModel.getLoggedIn().observe(getViewLifecycleOwner(),new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if(result)
                    Toast.makeText(getContext(),"Logged in",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
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
