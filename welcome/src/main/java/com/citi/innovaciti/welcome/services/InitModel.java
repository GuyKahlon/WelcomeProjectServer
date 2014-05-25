package com.citi.innovaciti.welcome.services;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.daos.HostDao;
import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 25/05/14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */

@Service
public class InitModel {

    @Autowired
    private HostDao hostDao;

    @Autowired
    private GuestDao guestDao;

    @PostConstruct
    private void init() {

        Host host = new Host();
        host.setFirstName("Liron");
        host.setLastName("Netzer");
        hostDao.save(host);

        Host host2 = new Host();
        host2.setFirstName("Kfir");
        host2.setLastName("Tishbi");
        hostDao.save(host2);

        Guest guest = new Guest();
        guest.setFirstName("Avi");
        guest.setLastName("Cohen");
        guest.setEmail("avi.cohen@gmail.com");
        guest.setPhoneNumber("0528967123");
        guestDao.save(guest);




    }

}
