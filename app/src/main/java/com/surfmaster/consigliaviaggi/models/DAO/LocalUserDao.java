package com.surfmaster.consigliaviaggi.models.DAO;


public interface LocalUserDao {

    public void saveUser(Integer id, String user, String pwd, String token, Integer type);
    public Integer getUserId();
    public String getUserName();
    public String getToken();
    public String getUserPwd();
    public void logOutUser();
    public Double getSelectedCityLatitude();
    public Double getSelectedCityLongitude();
    public void updateSelectedCity(String city, Double lat, Double lon);
    public void resetSelectedCity();
    public String getSelectedCity();
    public Integer getType();
}
