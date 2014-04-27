package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.HostDao;
import com.citi.innovaciti.welcome.domain.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/hosts")
@Controller
public class HostController {

    @Autowired
    private HostDao hostDao;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> showHosts(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                  @RequestParam(value = "size", required = false, defaultValue = "100") int size) {

        Map<String, Object> model = new HashMap<String, Object>();
        List<Host> hosts = hostDao.getHosts(page, size);
        model.put("hosts", hosts);

        long hostsCount = hostDao.getHostsCount();
        float numOfPages = (float) hostsCount / size;
        model.put("maxPages", (int) ((numOfPages > (int) numOfPages || numOfPages == 0.0) ? numOfPages + 1 : numOfPages));

        return model;

    }
}
