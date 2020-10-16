package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DAO.UserDao;
import com.surfmaster.consigliaviaggi.models.DAO.UserDaoSharedPrefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthenticationController {

    private Context context;

    private UserDao userDao;

    public AuthenticationController(Context context){
        this.context=context;
        userDao=new UserDaoSharedPrefs(context);
    }

    public Boolean authenticate(String user, String pwd)  {
        System.out.println("User: "+user+" pwd: "+pwd);
        BufferedReader jsonResponse = null;
        try {
            jsonResponse = getJsonResponseFromLoginUrl(user,pwd);
        } catch (IOException e) {
            postToastMessage("Login Server error");
            return false;
        }
        if(jsonResponse!=null) {

            JsonElement jsonTree  = JsonParser.parseReader(jsonResponse);
            JsonObject jsonResponseObject = jsonTree.getAsJsonObject();
            String token = jsonResponseObject.get("token").getAsString();
            Integer id = jsonResponseObject.get("userId").getAsInt();
            System.out.print(token);
            if (token != null) {

                userDao.saveUser(id,user,pwd,token);
                postToastMessage("Logged in");
                return true;

            }
        }
        postToastMessage("Logged error");
        return false;
    }

    private BufferedReader getJsonResponseFromLoginUrl(String user, String pwd) throws IOException {
        URL url = new URL(Constants.LOGIN_URL);
        HttpURLConnection connection = null;
        int responseCode = 0;

        String jsonInputString = "{\"username\":\"" + user + "\", \"password\": \"" + pwd + "\"}";
        System.out.print(jsonInputString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

        }
        responseCode=connection.getResponseCode();

        BufferedReader jsonResponse = null;
        if (responseCode== HttpURLConnection.HTTP_OK) {
            jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        return jsonResponse; //potrebbe essere null
    }

    private String getTokenFromJsonResponse(BufferedReader reader){
        JsonElement jsonTree  = JsonParser.parseReader(reader);
        JsonObject jsonResponse = jsonTree.getAsJsonObject();
        return jsonResponse.get("token").getAsString();
    }
    private Integer getIdFromJsonResponse(BufferedReader reader){
        JsonElement jsonTree  = JsonParser.parseReader(reader);
        JsonObject jsonResponse = jsonTree.getAsJsonObject();
        return jsonResponse.get("userId").getAsInt();
    }




    public boolean tryLogin(){
        String userName=userDao.getUserName();
        String pwd=userDao.getUserPwd();
        if(!(userName.isEmpty()||pwd.isEmpty()))
            return authenticate(userName,pwd);
        else
            return false;
    }
    public void logout() {
        userDao.logOutUser();
    }

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }


}
