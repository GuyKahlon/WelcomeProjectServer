package com.citi.innovaciti.welcome.web;

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


    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestParam(value = "hostId", required = true) Long hostId,
                               @RequestParam(value = "guestId", required = true) Long guestId) {

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("notification", "mock notification was sent from host "+hostId+" to guest "+guestId);

        return model;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
