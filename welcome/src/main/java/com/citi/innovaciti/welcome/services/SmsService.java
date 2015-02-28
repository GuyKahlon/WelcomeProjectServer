package com.citi.innovaciti.welcome.services;

import com.citi.innovaciti.welcome.smsUtils.bejeqSmsApiEntities.Inforu;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private static String baseUrl = "https://api.b-sms.co.il/SendMessageXml.ashx?InforuXML=";

    public static final String SMS_SERVICE_USERNAME = "Insert username here";
    public static final String SMS_SERVICE_PASSWORD = "Insert password here";

    private RestTemplate restTemplate;
    private XStream xstream;

    public SmsService() {

        this.restTemplate = new RestTemplate();
        xstream = new XStream();
        xstream.processAnnotations(Inforu.class);
    }

    public void sendSms(String recipientPhoneNumber, String message) {

        //the message will be contained in an XML, so escape it
        String escapedMessageForXml = StringEscapeUtils.escapeXml10(message);

        Inforu inforu = new Inforu(SMS_SERVICE_USERNAME, SMS_SERVICE_PASSWORD, "0528967231", recipientPhoneNumber, escapedMessageForXml);

        String inforuAsXml = xstream.toXML(inforu);

        log.info("Sending an SMS to : " + recipientPhoneNumber);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + inforuAsXml, null, String.class);

        HttpStatus httpStatus = response.getStatusCode();

        log.info("Recived the following Http status for the SMS request: "+httpStatus);


    }

}

