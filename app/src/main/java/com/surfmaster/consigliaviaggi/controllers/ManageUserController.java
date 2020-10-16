package com.surfmaster.consigliaviaggi.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.DAO.DaoException;
import com.surfmaster.consigliaviaggi.models.DAO.UserDao;
import com.surfmaster.consigliaviaggi.models.DAO.UserDaoSharedPrefs;
import com.surfmaster.consigliaviaggi.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageUserController {
    private Context context;
    private UserDao userDao;

    public ManageUserController(Context context){
        this.context=context;
        userDao=new UserDaoSharedPrefs(context);
    }
    public Integer getUserId(){
        return userDao.getUserId();
    }
    public String getUserName(){
        return userDao.getUserName();
    }
    public String getToken(){
        return userDao.getToken();
    }
    public String getUserPwd(){
        return userDao.getUserPwd();
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
    private BufferedReader getJSONFromConnection(HttpURLConnection connection) throws DaoException {
        int responseCode;
        BufferedReader json = null;
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
        connection.setRequestProperty("Authorization","Bearer "+userDao.getToken());
        connection.setRequestProperty("Content-Type","application/json");
        Log.i("Query create review",connection.toString());
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
