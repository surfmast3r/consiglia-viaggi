package com.surfmaster.consigliaviaggi.controllers;

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

    public User createUser(User user) throws IOException, DaoException {

        return parseUser(createUserJSON(user));

    }

    private User parseUser(JsonObject userJSON) {
        Gson gson = new Gson();
        return gson.fromJson(userJSON,User.class);
    }

    private JsonObject createUserJSON(User user) throws IOException, DaoException {
        Gson gson = new Gson();
        JsonObject userJson = JsonParser.parseString(gson.toJson(user)).getAsJsonObject();
        HttpURLConnection connection = createConnection(Constants.REGISTER, "POST");
        int responseCode;

        OutputStream os = connection.getOutputStream();
        byte[] input = userJson.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        responseCode=connection.getResponseCode();

        BufferedReader jsonResponse;
        if (responseCode== HttpURLConnection.HTTP_CREATED) {
            jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }

        else{
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
}
