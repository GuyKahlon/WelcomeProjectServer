package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.HostDao;
import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.utils.ExcelFileHostsExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/hosts")
@Controller
public class HostController {

    private final static Logger log = LoggerFactory.getLogger(HostController.class);
    private static final Object lock = new Object();

    @Autowired
    private HostDao hostDao;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> showHost(@PathVariable("id") Long id) {

        Map<String, Object> model = new HashMap<String, Object>();
        Host host = hostDao.findById(id);
        if (host == null) {

            model.put("errMsg", "Host with ID " + id + " doesn't exist");

        } else {

            model.put("host", host);
        }

        return model;
    }


    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestBody Host host) {

        Map<String, Object> model = new HashMap<String, Object>();
        long numOfExistingHostsWithPhoneNumber = hostDao.countHostsByPhoneNumber(host.getPhoneNumber());

        String errorMsg = "Failed to create a Host: Host with phoneNumber '" + host.getPhoneNumber() + "' already exists.";

        if (numOfExistingHostsWithPhoneNumber > 0) {

            log.error(errorMsg);

            model.put("ERROR", errorMsg);

            return model;

        } else {

            synchronized (lock) {

                numOfExistingHostsWithPhoneNumber = hostDao.countHostsByPhoneNumber(host.getPhoneNumber());

                if (numOfExistingHostsWithPhoneNumber > 0) {

                    log.error(errorMsg);

                    model.put("ERROR", errorMsg);

                    return model;

                } else {

                    Host dbHost = hostDao.save(host);
                    log.info("Saved Host " + host.toString() + " to the DB");
                    model.put("host", dbHost);
                }

            }
        }

        return model;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> changeHostActiveState(@PathVariable("id") Long id, @RequestParam(value = "active") boolean active) {

        Map<String, Object> model = new HashMap<String, Object>();
        int updateCount = hostDao.setHostActiveState(active, id);
        boolean isSuccessfulUpdate = updateCount > 0;
        if (!isSuccessfulUpdate) {
            model.put("errMsg", "Failed to update Host Active state");
        } else{
            model.put("Update succeeded","");
        }
        return model;
    }


    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> showActiveHosts(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "100") int size) {

        Map<String, Object> model = new HashMap<String, Object>();
        List<Host> hosts = hostDao.getActiveHosts(page, size);
        model.put("hosts", hosts);

        model.put("currentPageIndex", page);

        long hostsCount = hostDao.getActiveHostsCount();
        float numOfPages = (float) hostsCount / size;
        model.put("maxPages", (int) ((numOfPages > (int) numOfPages || numOfPages == 0.0) ? numOfPages + 1 : numOfPages));

        return model;

    }


    @RequestMapping(value = "/hostsMgmt", method = RequestMethod.GET)
    public String showHostsMgmtView(Model uiModel) {

        return "hostsMgmt";
    }

    @RequestMapping(value = "/overrideHosts", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> overrideHosts(@RequestParam("file") MultipartFile file) {
        Map<String, Object> model = new HashMap<String, Object>();

        log.info("Overriding Hosts");

        if (file.isEmpty()) {
            String errMsg = "Failed to override Hosts. uploaded file is empty";
            log.error(errMsg);
            model.put("errMsg", errMsg);
            return model;
        }


        List<Host> hostsFromFile = null;
        try {

            hostsFromFile = ExcelFileHostsExtractor.getHostsFromFile(file.getInputStream());

        } catch (Exception e) {

            String errMsg = "Failed to extract Hosts from file " + file.getOriginalFilename();
            log.error(errMsg);
            model.put("errMsg", errMsg);
        }


        if (hostsFromFile != null && hostsFromFile.size() > 0) {

            List<Host> savedHosts = hostDao.save(hostsFromFile);
            log.info("Saved " + savedHosts.size() + " new Hosts to the DB");
            model.put("hosts", savedHosts);
        }

        return model;

    }
}
