package com.surfmaster.consigliaviaggi.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import com.surfmaster.consigliaviaggi.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView createAccountTextView;
    private EditText userEditText;
    private EditText pwdEditText;
    private LinearLayout loginForm,accountPage;
    private AppCompatButton loginButton,logoutButton;
    private TextView usernameTextView,nameTextView,surnameTextView,emailTextView;
    private Switch showUsernameSwitch;
    private CallbackManager callbackManager;
    private LoginButton loginFacebookButton;
    private Boolean switchPreviousValue=false;
    private Snackbar loadingBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        /*create loading bar*/
        loadingBar = Snackbar.make(root, R.string.data_updating, Snackbar.LENGTH_INDEFINITE);
        ViewGroup contentLay = (ViewGroup) loadingBar.getView().findViewById(com.google.android.material.R.id.snackbar_text).getParent();
        ProgressBar item = new ProgressBar(requireContext());
        contentLay.addView(item,0);

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

                loadingBar.show();
            }
        });

        usernameTextView=root.findViewById(R.id.usernameTextView);
        nameTextView=root.findViewById(R.id.nameTextView);
        surnameTextView=root.findViewById(R.id.surnameTextView);
        emailTextView=root.findViewById(R.id.emailTextView);
        showUsernameSwitch=root.findViewById(R.id.usernameSwitch);
        loginFacebookButton = root.findViewById(R.id.loginFacebookButton);
        loginFacebookButton.setFragment(this);
        loginFacebookButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginViewModel.loginFb(getFbToken());
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }

        });
        loginViewModel.getNickname().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(String s) {
                usernameTextView.setText(s);
                loadingBar.dismiss();
            }
        });
        loginViewModel.getName().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nameTextView.setText(s);
            }
        });
        loginViewModel.getSurname().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(String s) {
                surnameTextView.setText(s);
            }
        });
        loginViewModel.getEmail().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(String s) {
                emailTextView.setText(s);
            }
        });
        loginViewModel.getShowUsername().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newValue) {
                if(newValue!=null){
                    switchPreviousValue=newValue;
                    showUsernameSwitch.setChecked(newValue);

                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logoutButtonClickAction();
                LoginManager.getInstance().logOut();
            }
        });


        showUsernameSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if(switchPreviousValue!=value)
                    loginViewModel.showUsernameChanged(value);
            }
        });

        loginViewModel.getLoggedIn().observe(getViewLifecycleOwner(),new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                    if (result) {
                        loginForm.setVisibility(View.GONE);
                        accountPage.setVisibility(View.VISIBLE);
                        loginViewModel.getUserData();
                    }
                    else
                    {
                        loginForm.setVisibility(View.VISIBLE);
                        accountPage.setVisibility(View.GONE);

                    }
            }
        });

        enableSignupLink(root);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String getFbToken() {
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        AccessToken token = AccessToken.getCurrentAccessToken();
        //System.out.println("LOGIN FB:"+token.getToken());
        return token.getToken();
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

