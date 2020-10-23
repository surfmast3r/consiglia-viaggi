package com.surfmaster.consigliaviaggi.models.DAO;

import android.content.Context;

import java.io.IOException;

public interface LoginDao {

     Boolean authenticate(String user, String pwd, Context context) throws IOException;
     Boolean authenticate(String token, Context context) throws IOException;

}
