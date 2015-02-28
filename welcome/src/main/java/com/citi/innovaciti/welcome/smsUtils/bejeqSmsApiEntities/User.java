package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String Username;

    private String Password;

    public User(String username, String password) {
        Username = username;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
