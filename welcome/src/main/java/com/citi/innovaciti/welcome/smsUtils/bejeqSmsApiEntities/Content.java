package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class Content {

    private static final String DEFAULT_TYPE="sms";

    private String Message;

    @XStreamAsAttribute
    private String Type;

    public Content(String message) {
        Type = DEFAULT_TYPE;
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
