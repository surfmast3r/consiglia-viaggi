package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.AuthenticatedUser;
import com.surfmaster.consigliaviaggi.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginDaoSpring implements LoginDao{

    private AuthenticatedUser authenticatedUser;

    @Override
    public Boolean authenticate(String user, String pwd, Context context) throws IOException {
        System.out.println("User: "+user+" pwd: "+pwd);
        BufferedReader jsonResponse;
        jsonResponse = getJsonResponseFromLoginUrl(user,pwd);
        if(jsonResponse!=null) {

            JsonElement jsonTree  = JsonParser.parseReader(jsonResponse);
            JsonObject jsonResponseObject = jsonTree.getAsJsonObject();
            String token = jsonResponseObject.get("token").getAsString();
            Integer id = jsonResponseObject.get("userId").getAsInt();
            System.out.print(token);
            if (token != null) {
                authenticatedUser= new AuthenticatedUser( new AuthenticatedUser.Builder().setId(id)
                        .setNickname(user)
                        .setPwd(pwd));
                authenticatedUser.setToken(token);
                authenticatedUser.setType(Constants.NORMAL_USER);
                return true;

            }
        }
        return false;
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
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
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
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
        return jsonResponse;
    }

    @Override
    public Boolean authenticate(String token, Context context) {
        return false;
    }
}
