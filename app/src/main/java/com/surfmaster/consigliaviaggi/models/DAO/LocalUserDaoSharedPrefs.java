package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;
import android.content.SharedPreferences;

import com.surfmaster.consigliaviaggi.Constants;
import com.surfmaster.consigliaviaggi.models.AuthenticatedUser;

public class LocalUserDaoSharedPrefs implements LocalUserDao {
    private Context context;

    public LocalUserDaoSharedPrefs(Context context){
        this.context=context;

    }

    @Override
    public void saveUser(AuthenticatedUser authenticatedUser) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Constants.ID,authenticatedUser.getId());
        editor.putString(Constants.USER,authenticatedUser.getNickname());
        editor.putString(Constants.PWD, authenticatedUser.getPwd());
        editor.putString(Constants.TOKEN, authenticatedUser.getToken());
        editor.putInt(Constants.TYPE, authenticatedUser.getType());
        editor.apply();
    }

    @Override
    public Double getSelectedCityLatitude() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        return Double.longBitsToDouble(pref.getLong(Constants.PREF_LATITUDE, Double.doubleToRawLongBits(40.851799)));
    }

    @Override
    public Double getSelectedCityLongitude() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        return Double.longBitsToDouble(pref.getLong(Constants.PREF_LONGITUDE, Double.doubleToRawLongBits(14.268120)));
    }

    @Override
    public void updateSelectedCity(String city, Double lat, Double lon) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.PREF_CITY, city);
        editor.putLong(Constants.PREF_LATITUDE, Double.doubleToRawLongBits(lat));
        editor.putLong(Constants.PREF_LONGITUDE, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    @Override
    public void resetSelectedCity() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constants.PREF_CITY);
        editor.remove(Constants.PREF_LATITUDE);
        editor.remove(Constants.PREF_LONGITUDE);
        editor.apply();
    }

    @Override
    public String getSelectedCity() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        return pref.getString(Constants.PREF_CITY,"");
    }


    public Integer getUserId(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);

        return pref.getInt(Constants.ID,-1);
    }
    public Integer getType(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);
        return pref.getInt(Constants.TYPE,-1);
    }
    public String getUserName(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);

        return pref.getString(Constants.USER,"");
    }
    public String getToken(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);

        return pref.getString(Constants.TOKEN,"");
    }
    public String getUserPwd(){
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0);

        return pref.getString(Constants.PWD,"");
    }
    public void logOutUser() {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constants.USER);
        editor.remove(Constants.PWD );
        editor.remove(Constants.TOKEN);
        editor.remove(Constants.ID);
        editor.remove(Constants.TYPE);
        editor.apply();
    }
}
