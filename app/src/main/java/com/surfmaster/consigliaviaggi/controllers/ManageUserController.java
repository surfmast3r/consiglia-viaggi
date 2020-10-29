package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DAO.DaoFactory;
import com.surfmaster.consigliaviaggi.models.DAO.LocalUserDao;
import com.surfmaster.consigliaviaggi.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageUserController {
    private Context context;
    private LocalUserDao localUserDao;

    public ManageUserController(Context context){
        this.context=context;
        localUserDao = DaoFactory.getLocalUserDao(context);
    }
    public Integer getUserId(){
        return localUserDao.getUserId();
    }
    public String getUserName(){
        return localUserDao.getUserName();
    }
    public String getToken(){
        return localUserDao.getToken();
    }
    public String getUserPwd(){
        return localUserDao.getUserPwd();
    }

    public User getUserDetails(int id) {
            String urlString= Constants.GET_USER_DETAILS_URL +"?"+Constants.ID_PARAM+id;
            Gson gson=new Gson();
            BufferedReader bufferedReader;
            try {
                bufferedReader = getJSONFromConnection(createAuthenticatedConnection(urlString,"GET"));
                JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
                JsonObject userJson = jsonTree.getAsJsonObject();
                return gson.fromJson(userJson, User.class);
            } catch (DaoException | IOException e) {
                postToastMessage(e.getMessage());
                return null;
            }
    }

    public Boolean setShowNickname(int id,Boolean value){
        String urlString= Constants.SET_SHOW_NICK_URL +"?"+Constants.ID_PARAM+id+"&"
                +Constants.VALUE_PARAM+value;
        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromConnection(createAuthenticatedConnection(urlString,"PUT"));
            JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
            JsonObject responseJson = jsonTree.getAsJsonObject();
            return responseJson.get("response").getAsBoolean();
        } catch (DaoException | IOException e) {
            postToastMessage(e.getMessage());
            return null;
        }
    }
    private BufferedReader getJSONFromConnection(HttpURLConnection connection) throws DaoException {
        int responseCode;
        BufferedReader json;
        try {
            responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK)
                json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            else if(responseCode==HttpURLConnection.HTTP_UNAUTHORIZED)
                throw new DaoException(DaoException.ERROR,"Unauthorized");
            else
                throw new DaoException(DaoException.ERROR,"Server Error");
        } catch (IOException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }
        return json;
    }

    private HttpURLConnection createAuthenticatedConnection(String urlString,String requestMethod) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        connection.setRequestProperty("Authorization","Bearer "+ localUserDao.getToken());
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

    public Double getSelectedCityLatitude() {
       return localUserDao.getSelectedCityLatitude();
    }

    public Double getSelectedCityLongitude() {
        return localUserDao.getSelectedCityLongitude();
    }

    public String getSelectedCity() {

        String city = localUserDao.getSelectedCity();
        if(city.length()>0)
            return city;
        return context.getString(R.string.city_select);
    }

    public void updateSelectedCity(String city, Double lat, Double lon) {
        if(lat!=null&&lon!=null) {
            localUserDao.updateSelectedCity(city,lat,lon);
        }
    }

    public String resetSelectedCity() {
        localUserDao.resetSelectedCity();
        return context.getString(R.string.city_select);
    }



}
