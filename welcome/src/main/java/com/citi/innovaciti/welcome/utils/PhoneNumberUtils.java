package com.citi.innovaciti.welcome.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 06/06/15
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public class PhoneNumberUtils {

    public static String removeIllegalCharsFromPhoneNumber(String phoneNumber) {

        if (phoneNumber == null){
            return phoneNumber;
        }

        String result = phoneNumber.replaceAll("[^+0-9]", "");
        return result;
    }


}
