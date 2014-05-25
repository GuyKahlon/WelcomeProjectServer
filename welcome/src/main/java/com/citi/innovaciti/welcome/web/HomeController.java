package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.daos.HostDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    @Autowired
    private HostDao hostDao;

    @Autowired
    private GuestDao guestDao;



    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! the client locale is " + locale.toString());

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        long hostsCount = hostDao.getHostsCount();
        model.addAttribute("NumOfHosts", hostsCount);

        long guestsCount = guestDao.getGuestsCount();
        model.addAttribute("NumOfGuests", guestsCount);


        return "home";
    }

}
