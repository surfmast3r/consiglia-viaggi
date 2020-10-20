package com.surfmaster.consigliaviaggi.models.DAO;


public interface UserDao {

    public void saveUser(Integer id, String user, String pwd, String token);
    public Integer getUserId();
    public String getUserName();
    public String getToken();
    public String getUserPwd();
    public void logOutUser();
    public void saveFbUser(Integer id, String token);
    public void saveFbUserDetails(String name, String pwd);
}
