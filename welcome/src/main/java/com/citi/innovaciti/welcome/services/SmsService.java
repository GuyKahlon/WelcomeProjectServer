package com.citi.innovaciti.welcome.services;

import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities.Inforu;
import com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities.Result;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 28/02/15
 * Time: 09:15
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SmsService {

    private final static Logger log = LoggerFactory.getLogger(SmsService.class);

    private static final String baseUrl = "https://api.b-sms.co.il/SendMessageXml.ashx?InforuXML=";

    private static final int SMS_SERVICE_OK_STATUS = 1;

    @Value("${sms.service.username}")
    private String smsServiceUsername;

    @Value("${sms.service.password}")
    private String smsServicePassword;

    @Value("${sms.default.sender.phone.number}")
    private String smsDefaultSenderPhoneNumber;

    @Value("${sms.default.sender.name}")
    private String smsDefaultSenderName;

    @Value("${sms.host.notification.message.template}")
    private String hostNotificationMessageTemplate;

    @Value("${sms.host.notification.message.template.with.floor.number}")
    private String hostNotificationMessageTemplateWithFloorNumber;

    private RestTemplate restTemplate;
    private XStream xstream;

    public SmsService() {

        restTemplate = new RestTemplate();
        xstream = new XStream();
        xstream.processAnnotations(Inforu.class);
        xstream.processAnnotations(Result.class);
    }


    public boolean sendSmsToHostRegardingWaitingGuest(Host host, Guest guest, Integer guestWaitingFloorNumber) {
        String message;

        if (guestWaitingFloorNumber == null) {
            message = MessageFormat.format(
                    hostNotificationMessageTemplate,
                    host.getFirstName(),
                    guest.getFullName(),
                    guest.getFirstName(),
                    guest.getPhoneNumber());
        } else {
            message = MessageFormat.format(
                    hostNotificationMessageTemplateWithFloorNumber,
                    host.getFirstName(),
                    guest.getFullName(),
                    getNumberAsOrdinalRepresentation(guestWaitingFloorNumber),
                    guest.getFirstName(),
                    guest.getPhoneNumber());
        }

        log.info("Sending SMS to host " + host.toString() + " with the following message:\n" + message);

        return sendSms(host.getPhoneNumber(), message);
    }


    private String getNumberAsOrdinalRepresentation(int number) {

        int lastTwoDigits = number % 100;
        int lastDigit = number % 10;

        if (11 <= lastTwoDigits && lastTwoDigits <= 13) {

            return number + "th";

        } else {
            switch (lastDigit) {
                case 1:
                    return number + "st";

                case 2:
                    return number + "nd";

                case 3:
                    return number + "rd";

                default:
                    return number + "th";
            }
        }

    }


    /**
     * @param recipientPhoneNumber
     * @param message
     * @return true if the sms was sent successfully, false otherwise
     */
    public boolean sendSms(String recipientPhoneNumber, String message) {

        //the message will be contained in an XML, so escape it
        String escapedMessageForXml = StringEscapeUtils.escapeXml10(message);

        Inforu inforu = new Inforu(smsServiceUsername, smsServicePassword,
                smsDefaultSenderPhoneNumber, smsDefaultSenderName, recipientPhoneNumber, escapedMessageForXml);

        String inforuAsXml = xstream.toXML(inforu);

        log.info("Sending an SMS to: " + recipientPhoneNumber);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + inforuAsXml, null, String.class);

        HttpStatus httpStatus = response.getStatusCode();

        String smsServiceResponseAsXml = response.getBody();

        if (smsServiceResponseAsXml == null) {

            log.error("Failed to send an SMS to " + recipientPhoneNumber +
                    ". Http Status is " + httpStatus + ". SMS service response is null.");
            return false;
        }

        Result smsServiceResponse = (Result) xstream.fromXML(smsServiceResponseAsXml);

        int smsServiceResponseStatus = smsServiceResponse.getStatus();

        if (smsServiceResponseStatus != SMS_SERVICE_OK_STATUS) {

            log.error("Failed to send an SMS to " + recipientPhoneNumber +
                    ". Http Status is " + httpStatus + ". SMS service response is:\n" + smsServiceResponseAsXml);
            return false;

        } else {

            log.info("Received the following Http status for the SMS request: " + httpStatus +
                    " and the following response:\n" + response.getBody());
            return true;
        }

    }

}

