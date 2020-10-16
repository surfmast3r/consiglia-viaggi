package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;
import android.content.SharedPreferences;

import com.surfmaster.consigliaviaggi.Constants;

public class UserDaoSharedPrefs implements UserDao{
    private Context context;

    public UserDaoSharedPrefs(Context context){
        this.context=context;

    }

    public void saveUser(Integer id, String user, String pwd, String token) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Constants.ID,id);
        editor.putString(Constants.USER,user);
        editor.putString(Constants.PWD, pwd);
        editor.putString(Constants.TOKEN, token);
        editor.apply();
    }



    public Integer getUserId(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        Integer userId=pref.getInt(Constants.ID,-1);

        return userId;
    }
    public String getUserName(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        String userName=pref.getString(Constants.USER,"");

        return userName;
    }
    public String getToken(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        String token=pref.getString(Constants.TOKEN,"");

        return token;
    }
    public String getUserPwd(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        String pwd=pref.getString(Constants.PWD,"");

        return pwd;
    }
    public void logOutUser() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constants.USER);
        editor.remove(Constants.PWD );
        editor.remove(Constants.TOKEN);
        editor.apply();
    }
}
