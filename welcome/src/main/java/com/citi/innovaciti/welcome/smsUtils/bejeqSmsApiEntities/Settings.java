package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    private String SenderNumber;
    private String SenderName;

    public Settings(String senderNumber, String senderName) {
        SenderNumber = senderNumber;
        SenderName = senderName;
    }

    public String getSenderNumber() {
        return SenderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        SenderNumber = senderNumber;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }
}
