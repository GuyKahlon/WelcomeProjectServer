package com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("Result")
public class Result {

    private int Status;

    private String Description;

    private String NumberOfRecipients;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNumberOfRecipients() {
        return NumberOfRecipients;
    }

    public void setNumberOfRecipients(String numberOfRecipients) {
        NumberOfRecipients = numberOfRecipients;
    }
}
