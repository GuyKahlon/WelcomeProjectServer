package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.domain.Guest;
import org.apache.commons.codec.binary.Base64;
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


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> searchByPic(@RequestBody String picture) {

        Map<String, Object> model = new HashMap<String, Object>();

        byte[] imageByteArray = Base64.decodeBase64(picture);

        FileOutputStream imageOutFile = null;
        String fileName ="pic1.jpg";
        try {

            imageOutFile = new FileOutputStream("C:\\Users\\Liron\\Desktop\\tmp\\"+fileName);
            imageOutFile.write(imageByteArray);
            imageOutFile.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {

        }


        model.put("guest","{}");

        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestBody Guest guest) {

        Map<String, Object> model = new HashMap<String, Object>();

        guestDao.save(guest);

        model.put("guest", guest);

        return model;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
