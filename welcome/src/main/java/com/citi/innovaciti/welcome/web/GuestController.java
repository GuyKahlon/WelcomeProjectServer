package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.domain.Guest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/guests")
@Controller
public class GuestController {


    private final static Logger log = LoggerFactory.getLogger(GuestController.class);

    private static final Object lock = new Object();

    @Autowired
    private GuestDao guestDao;


    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> showGuests(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "100") int size) {

        Map<String, Object> model = new HashMap<String, Object>();
        List<Guest> guests = guestDao.getGuests(page, size);
        model.put("guests", guests);

        long guestsCount = guestDao.getGuestsCount();
        float numOfPages = (float) guestsCount / size;
        model.put("maxPages", (int) ((numOfPages > (int) numOfPages || numOfPages == 0.0) ? numOfPages + 1 : numOfPages));

        return model;

    }



    @RequestMapping(value = "/searchByPhone", method = RequestMethod.GET, params = {"phoneNumber"})
    public
    @ResponseBody
    Map<String, Object> searchByPhoneNumber(@RequestParam String phoneNumber) {

        Map<String, Object> model = new HashMap<String, Object>();

        List<Guest> guests = guestDao.findByPhoneNumber(phoneNumber);

        if (guests == null || guests.size() == 0) {

            log.info("There is no existing guest with the following phone number " + phoneNumber);

            model.put("guest", "{}");

            return model;
        }
        if (guests.size() > 1) {
            log.error("There is more than one Guest that has the following phone number: " + phoneNumber + ". Using the first guest: " + guests.get(0).getId());

        }

        log.info("searchByPhoneNumber method is returning guest: "+guests.get(0).toString());
        model.put("guest", guests.get(0));

        return model;
    }



    private void saveBase64Picture(String base64Pic, String picFilePath) {

        byte[] imageByteArray = decodeBase64Image(base64Pic);

        FileOutputStream imageOutFile = null;
        try {

            imageOutFile = new FileOutputStream(picFilePath);
            imageOutFile.write(imageByteArray);
            imageOutFile.close();

        } catch (FileNotFoundException e) {
            log.error("Failed to find image file", e);

        } catch (IOException e) {
            log.error("Failed to find image file", e);
        } finally {

            if (imageOutFile != null) {

                try {
                    imageOutFile.close();
                } catch (IOException e) {
                    log.error("Failed to close image file", e);
                }
            }

        }
    }

    private byte[] decodeBase64Image(String base64Pic) {
        return Base64.decodeBase64(base64Pic);
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestBody Guest guest) {

        Map<String, Object> model = new HashMap<String, Object>();

        long numOfExistingGuestsWithPhoneNumber = guestDao.countGuestsByPhoneNumber(guest.getPhoneNumber());

        String errorMsg = "Failed to create a guest: guest with phoneNumber '" + guest.getPhoneNumber() + "' already exists.";

        if (numOfExistingGuestsWithPhoneNumber > 0) {

            log.error(errorMsg);

            model.put("ERROR", errorMsg);

            return model;

        } else {

            byte[] guestPicture = decodeBase64Image(guest.getBase64img());

            guest.setPicture(guestPicture);

            synchronized (lock) {

                numOfExistingGuestsWithPhoneNumber = guestDao.countGuestsByPhoneNumber(guest.getPhoneNumber());

                if (numOfExistingGuestsWithPhoneNumber > 0) {

                    log.error(errorMsg);

                    model.put("ERROR", errorMsg);

                    return model;

                } else {

                    Guest dbGuest = guestDao.save(guest);
                    log.info("Saved Guest "+guest.toString()+" to the DB");
                    dbGuest.setBase64img(null);//todo: this is a hack, so that the image will not be returned to the UI
                    dbGuest.setPicture(null);
                    model.put("guest", dbGuest);
                }

            }
        }


        return model;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
