package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;

public class LocalUserDaoFactory {
    public static LocalUserDao getLocalUserDao(Context context){

        /*Logica per scegliere il dao*/
        return new LocalUserDaoSharedPrefs(context);

    }
}
