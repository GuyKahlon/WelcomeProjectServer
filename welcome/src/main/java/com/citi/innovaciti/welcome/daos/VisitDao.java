package com.citi.innovaciti.welcome.daos;

import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.domain.Visit;
import com.citi.innovaciti.welcome.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


@Service
public class VisitDao {

    @Autowired
    private VisitRepository visitRepository;

    private static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    public Visit save(Visit visit){

        //Calendar calendar = Calendar.getInstance(gmtTimeZone);
        Calendar calendar = Calendar.getInstance();
        visit.setTime(calendar.getTimeInMillis());
        return visitRepository.save(visit);
    }

    public List<Visit> findVisitsByHost(Host host) {
        return visitRepository.findByHost(host);
    }

    public List<Visit> findVisitsByHostId(long hostId) {
        return visitRepository.findByHostId(hostId);
    }

    public List<Visit> findVisitsByGuest(Guest guest) {
        return visitRepository.findByGuest(guest);
    }



}
