package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RegistrationController {

    private Context context;
    public RegistrationController(Context application) {
        context=application;
    }

    public boolean registerUser(User user)  {
       JsonObject userJson = null;
        try {
            userJson=createUserJSON(user);
        } catch (DaoException e) {
            postToastMessage(e.getMessage());
            return false;
        }
        postToastMessage("Registrazione Effettuata");
        return true;
    }

    private User parseUser(JsonObject userJSON) {
        Gson gson = new Gson();
        return gson.fromJson(userJSON,User.class);
    }

    private JsonObject createUserJSON(User user) throws  DaoException {
        int responseCode;
        BufferedReader jsonResponse;
        Gson gson = new Gson();
        JsonObject userJson = JsonParser.parseString(gson.toJson(user)).getAsJsonObject();

        System.out.println(userJson);

        byte[] input = userJson.toString().getBytes(StandardCharsets.UTF_8);
        try {
            HttpURLConnection connection = createConnection(Constants.REGISTER, "POST");
            OutputStream os = connection.getOutputStream();
            os.write(input, 0, input.length);
            responseCode=connection.getResponseCode();
            if (responseCode== HttpURLConnection.HTTP_OK) {
                jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            else if(responseCode== HttpURLConnection.HTTP_INTERNAL_ERROR){
                throw  new DaoException(DaoException.FAIL_TO_INSERT,"Username già in uso");
            }
            else{
                throw  new DaoException(DaoException.ERROR,"Errore di rete");
            }
        } catch (IOException e) {
            throw  new DaoException(DaoException.ERROR,"Errore di rete");
        }



        return JsonParser.parseReader(jsonResponse).getAsJsonObject();

    }

    private HttpURLConnection createConnection(String urlString,String requestMethod) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json");
        return connection;
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
