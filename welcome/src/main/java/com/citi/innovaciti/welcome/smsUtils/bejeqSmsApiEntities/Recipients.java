package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class Recipients {

    private String PhoneNumber;

    public Recipients(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
