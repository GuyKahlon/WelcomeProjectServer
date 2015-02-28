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

    public Settings(String senderNumber) {
        SenderNumber = senderNumber;
    }

    public String getSenderNumber() {
        return SenderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        SenderNumber = senderNumber;
    }
}
