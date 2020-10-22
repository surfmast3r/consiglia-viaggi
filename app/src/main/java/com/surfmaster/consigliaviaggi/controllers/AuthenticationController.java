package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
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
    private ManageUserController manageUserController;

    private UserDao userDao;

    public AuthenticationController(Context context){
        this.context=context;
        userDao=new UserDaoSharedPrefs(context);
        manageUserController=new ManageUserController(context);
    }

    public Boolean authenticate(String user, String pwd)  {
        System.out.println("User: "+user+" pwd: "+pwd);
        BufferedReader jsonResponse;
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

                userDao.saveUser(id,user,pwd,token,0);
                postToastMessage("Logged in");
                return true;

            }
        }
        postToastMessage("Logged error");
        return false;
    }

    private BufferedReader getJsonResponseFromLoginUrl(String user, String pwd) throws IOException {

        URL url = new URL(Constants.LOGIN_URL);
        HttpURLConnection connection;
        int responseCode;

        String jsonInputString = "{\"username\":\"" + user + "\", \"password\": \"" + pwd + "\"}";
        System.out.print(jsonInputString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } // ?
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




    public boolean tryLogin() {
        String userName = userDao.getUserName();
        String pwd = userDao.getUserPwd();

        if (userDao.getType() == Constants.NORMAL){
            if (!(userName.isEmpty() || pwd.isEmpty()))
                return authenticate(userName, pwd);
        }
        if (userDao.getType() == Constants.BYFACEBOOK){
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
            AccessToken token = AccessToken.getCurrentAccessToken();
            return authenticateByFb(token.getToken());
        }
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


    public Boolean authenticateByFb(String fbToken){
        String urlString= Constants.FACEBOOK_LOGIN +fbToken;
        HttpURLConnection connection;
        int responseCode;
        BufferedReader json;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            connection.setRequestMethod("GET");
            responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK) {
                json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JsonElement jsonTree = JsonParser.parseReader(json);
                JsonObject responseJson = jsonTree.getAsJsonObject();

                String jwtToken = responseJson.get("token").getAsString();
                int userId = responseJson.get("userId").getAsInt();
                String username = responseJson.get("username").getAsString();
                if (jwtToken!=null){
                    userDao.saveUser(userId,username,fbToken,jwtToken,1);
                    postToastMessage("Logged in");
                    return true;
                }
            }
        } catch (IOException e) {
            postToastMessage("Logged error");
            return false;
        }
        postToastMessage("Logged error");
        return false;
    }
}
