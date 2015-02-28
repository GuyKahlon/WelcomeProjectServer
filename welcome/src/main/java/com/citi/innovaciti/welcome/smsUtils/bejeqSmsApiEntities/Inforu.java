package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */

@XStreamAlias("Inforu")
public class Inforu {

    private User User;

    private Recipients Recipients;

    private Settings Settings;

    private Content Content;

    public Inforu(String username, String password, String senderPhoneNumber,
                  String recipientPhoneNumber, String message) {

        this.User = new User(username, password);
        this.Content = new Content(message);
        this.Recipients = new Recipients(recipientPhoneNumber);
        this.Settings = new Settings(senderPhoneNumber);
    }


    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public Recipients getRecipients() {
        return Recipients;
    }

    public void setRecipients(Recipients recipients) {
        Recipients = recipients;
    }

    public Settings getSettings() {
        return Settings;
    }

    public void setSettings(Settings settings) {
        Settings = settings;
    }

    public Content getContent() {
        return Content;
    }

    public void setContent(Content content) {
        Content = content;
    }
}
