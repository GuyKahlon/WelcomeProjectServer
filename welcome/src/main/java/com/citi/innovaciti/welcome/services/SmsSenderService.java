package com.citi.innovaciti.welcome.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 24/08/14
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */


public class SmsSenderService {

    private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);

    private static final String BASE_URL = "http://www.textapp.net/webservice/httpservice.aspx";
    private static final String START_ARGUMENTS = "?";
    private static final String METHOD = "method";
    private static final String EQUALS = "=";
    private static final String SMS_METHOD = "sendsms";
    private static final String AND = "&";
    private static final String RETURN_CSV_STRING = "returncsvstring";
    private static final String FALSE = "false";
    private static final String EXTERNAL_LOGIN = "externallogin";
    private static final String PASSWORD = "password";
    private static final String CLIENT_BILLING_REF = "clientbillingreference";
    private static final String NA = "NA";
    private static final String CLIENT_MESSAGE_REF = "clientmessagereference";
    private static final String ORIGINATOR = "originator";
    private static final String DESTINATION = "destinations";
    private static final String BODY = "body";
    private static final String SEVENTY_TWO = "72";
    private static final String VALIDITY = "validity";
    private static final String CHARACTER_SET_ID = "charactersetid";
    private static final String ALPHA_NUMERIC = "1";
    private static final String TWO = "2";
    private static final String REPLY_METHOD_ID = "replymethodid";

    private String login;
    private String password;
    private String proxyIP;
    private int proxyPort;


    public SmsSenderService(String login, String password, String proxyIP, int proxyPort) {
        this.login = login;
        this.password = password;
        this.proxyIP = proxyIP;
        this.proxyPort = (proxyPort <= 0 ? 80 : proxyPort);
    }

    public boolean sendMessage(String message, List<String> recipients) {
        if (recipients != null && recipients.size() > 0) {
            try {
                URL url = getURL(message, recipients);
                String resp = callUrlAndGetResponse(url);

                if (!resp.contains("Transaction OK")) {
                    logger.error("Error sending SMS, response received from service:\n" + resp);
                    return false;
                }
                logger.debug("sending sms" + resp);
            } catch (Exception e) {
                logger.error("Error sending SMS", e);
                return false;
            }
        }
        return true;
    }

    private String callUrlAndGetResponse(URL url) throws IOException {
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {

            HttpURLConnection conn;
            Proxy proxy = getProxy();
            if (proxy != null) {
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("response code of: " + responseCode + " received accessing: " + url.toString());
            }
            // Read all the text returned by the server
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                result.append(str).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result.toString();
    }

    public URL getURL(String messageBody, List<String> recepientPhoneNumbers) throws UnsupportedEncodingException, MalformedURLException {
        StringBuilder urlBuffer = new StringBuilder();
        urlBuffer.append(BASE_URL).append(START_ARGUMENTS);
        urlBuffer.append(METHOD).append(EQUALS).append(SMS_METHOD).append(AND);
        urlBuffer.append(RETURN_CSV_STRING).append(EQUALS).append(FALSE).append(AND);
        urlBuffer.append(EXTERNAL_LOGIN).append(EQUALS).append(URLEncoder.encode(login, "UTF-8")).append(AND);
        urlBuffer.append(PASSWORD).append(EQUALS).append(URLEncoder.encode(password, "UTF-8")).append(AND);
        urlBuffer.append(CLIENT_BILLING_REF).append(EQUALS).append(NA).append(AND);
        urlBuffer.append(CLIENT_MESSAGE_REF).append(EQUALS).append(NA).append(AND);
        urlBuffer.append(ORIGINATOR).append(EQUALS).append(getOriginator()).append(AND);
        urlBuffer.append(DESTINATION).append(EQUALS)
                .append(URLEncoder.encode(getFormattedDestinationNumbers(recepientPhoneNumbers), "UTF-8")).append(AND);
        urlBuffer.append(BODY).append(EQUALS).append(URLEncoder.encode(messageBody, "UTF-8")).append(AND);
        urlBuffer.append(VALIDITY).append(EQUALS).append(SEVENTY_TWO).append(AND);
        urlBuffer.append(CHARACTER_SET_ID).append(EQUALS).append(TWO).append(AND);
        urlBuffer.append(REPLY_METHOD_ID).append(EQUALS).append(ALPHA_NUMERIC);
        return new URL(urlBuffer.toString());
    }

    public String getFormattedDestinationNumbers(List<String> recipientPhoneNumbers) {
        StringBuilder phonesCommaDelimited = new StringBuilder();
        for (String recipientPhoneNumber : recipientPhoneNumbers) {
            phonesCommaDelimited.append(recipientPhoneNumber.trim());
            phonesCommaDelimited.append(",");
        }
        if (phonesCommaDelimited.length() > 0) {
            phonesCommaDelimited.deleteCharAt(phonesCommaDelimited.length() - 1);
        }
        return phonesCommaDelimited.toString();
    }

    public Proxy getProxy() {
        if (proxyIP != null) {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        } else {
            return null;
        }
    }

    public String getOriginator() {
        try {
            return URLEncoder.encode("welcomeProj", "UTF-8");
        } catch (Exception e) {
            return "welcomeProj";
        }
    }

}
