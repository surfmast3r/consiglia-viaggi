package com.surfmaster.consigliaviaggi.models.DAO;
import com.surfmaster.consigliaviaggi.models.AuthenticatedUser;

public interface LocalUserDao {

     void saveUser(AuthenticatedUser authenticatedUser);
     void saveUser(Integer id, String user, String pwd, String token, Integer type);
     Integer getUserId();
     String getUserName();
     String getToken();
     String getUserPwd();
     void logOutUser();
     Double getSelectedCityLatitude();
     Double getSelectedCityLongitude();
     void updateSelectedCity(String city, Double lat, Double lon);
     void resetSelectedCity();
     String getSelectedCity();
     Integer getType();

}
