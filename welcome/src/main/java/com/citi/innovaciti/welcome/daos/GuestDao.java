package com.citi.innovaciti.welcome.daos;

import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liron on 27/04/2014.
 */
@Service
public class GuestDao {

    @Autowired
    private GuestRepository guestRepository;


    public Guest save(Guest guest){
       return guestRepository.save(guest);
    }


    /**
     *
     * @param page
     * @param size
     * @return  a Guest object that is initialized only with the following fields:
     *     firstName, lastName, email and phoneNumber (the rest of the fields will be null)
     */
    public List<Guest> getGuestsMainDetails(int page, int size){

        List<Guest> guests = new ArrayList<Guest>();
        List<Object[]> guestsAsObjects = guestRepository.findAllExcludePicture(new PageRequest(page,size));
        if(guestsAsObjects == null){
            return guests;
        }

        for(Object[] rawGuest: guestsAsObjects){
            Guest guest = new Guest();
            guest.setFirstName((String) rawGuest[0]);
            guest.setLastName((String) rawGuest[1]);
            guest.setEmail((String) rawGuest[2]);
            guest.setPhoneNumber((String) rawGuest[3]);
            guests.add(guest);
        }

        return guests;
    }

    public long getGuestsCount(){
        return guestRepository.count();
    }

    public Guest findById(long id){
        return guestRepository.findOne(id);
    }

    public List<Guest> getAllGuests(){
        return guestRepository.findAll();
    }

    public List<Guest> findByPhoneNumber(String phoneNumber){
        return guestRepository.findByPhoneNumber(phoneNumber);
    }

    public long countGuestsByPhoneNumber(String phoneNumber){
        return guestRepository.countByPhoneNumber(phoneNumber);

    }
}
