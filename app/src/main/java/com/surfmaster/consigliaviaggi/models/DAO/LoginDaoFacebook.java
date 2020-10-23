package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginDaoFacebook implements LoginDao{
    @Override
    public Boolean authenticate(String user, String pwd, Context context) {
        return null;
    }

    @Override
    public Boolean authenticate(String token, Context context) throws IOException {
        String urlString= Constants.FACEBOOK_LOGIN +token;
        HttpURLConnection connection;
        int responseCode;
        BufferedReader json;

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
                LocalUserDaoFactory.getLocalUserDao(context).saveUser(userId,username,token,jwtToken,Constants.FACEBOOK_USER);
                return true;
            }
        }
        return false;
    }
}
