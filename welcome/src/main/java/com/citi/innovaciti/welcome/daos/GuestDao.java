package com.citi.innovaciti.welcome.daos;

import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Liron on 27/04/2014.
 */
@Service
public class GuestDao {

    @Autowired
    private GuestRepository guestRepository;

    public List<Guest> getGuests(int page, int size){
        return guestRepository.findAll(new PageRequest(page,size)).getContent();
    }

    public long getGuestsCount(){
        return guestRepository.count();
    }
}
