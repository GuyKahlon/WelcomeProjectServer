package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.daos.HostDao;
import com.citi.innovaciti.welcome.daos.VisitDao;
import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.domain.Visit;
import com.citi.innovaciti.welcome.services.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 25/05/14
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */

@RequestMapping(value = "/notifications")
@Controller
public class NotificationController {

    private final static Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private GuestDao guestDao;

    @Autowired
    private HostDao hostDao;

    @Autowired
    private VisitDao visitDao;



    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestParam(value = "hostId", required = true) Long hostId,
                               @RequestParam(value = "guestId", required = true) Long guestId,
                               @RequestParam(value = "floorNum", required = false) Integer floorNum) {

        Map<String, Object> model = new HashMap<String, Object>();

        Guest guest = guestDao.findById(guestId);

        if(guest == null){
            String errMsg = "Received a guest ID that doesn't exist: "+guestId;
            logger.error(errMsg);
            model.put("errMsg",errMsg);
            return model;
        }

        Host host = hostDao.findById(hostId);

        if(host == null){
            String errMsg = "Received a Host ID that doesn't exist: "+hostId;
            logger.error(errMsg);
            model.put("errMsg",errMsg);
            return model;
        }

        //log the visit to the DB
        logger.info("Persisting visit to DB ");
        Visit visit = new Visit();
        visit.setGuest(guest);
        visit.setHost(host);
        visitDao.save(visit);

        boolean smsSentSuccessfully = smsService.sendSmsToHostRegardingWaitingGuest(host, guest, floorNum);

        if(smsSentSuccessfully){

            logger.info("An SMS was sent to host "+host.toString());
            model.put("notification", " Notification was sent to host "+hostId+" regarding guest "+guestId);

        } else{

            String errMsgPrefix = "Failed to send SMS to host ";
            logger.error(errMsgPrefix+ host.toString()+" regarding guest "+guest.toString());
            model.put("errMsg", errMsgPrefix + hostId+ " regarding guest "+guestId);
        }

        return model;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
