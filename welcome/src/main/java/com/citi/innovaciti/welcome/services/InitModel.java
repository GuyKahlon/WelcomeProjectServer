package com.citi.innovaciti.welcome.services;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.daos.HostDao;
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

/*        Host host = new Host();
        host.setFirstName("Dana");
        host.setLastName("Adini");
        host.setPhoneNumber("05489231");
        hostDao.save(host);

        Host host2 = new Host();
        host2.setFirstName("Dudu");
        host2.setLastName("Aharon");
        host2.setPhoneNumber("0528937154");
        hostDao.save(host2);

        Host host3 = new Host();
        host3.setFirstName("Moshe");
        host3.setLastName("Peretz");
        host3.setPhoneNumber("0528936314");
        hostDao.save(host3);

        Host host4 = new Host();
        host4.setFirstName("Shlomo");
        host4.setLastName("Artzi");
        host4.setPhoneNumber("00000000");
        hostDao.save(host4);

        Host host5 = new Host();
        host5.setFirstName("Sarit");
        host5.setLastName("Hadad");
        hostDao.save(host5);

        Host host6 = new Host();
        host6.setFirstName("Yudit");
        host6.setLastName("Ravitz");
        hostDao.save(host6);*/



/*        Guest guest = new Guest();
        guest.setFirstName("Avi");
        guest.setLastName("Cohen");
        guest.setEmail("avi.cohen@gmail.com");
        guest.setPhoneNumber("0528967123");
        guestDao.save(guest);*/




    }

}
