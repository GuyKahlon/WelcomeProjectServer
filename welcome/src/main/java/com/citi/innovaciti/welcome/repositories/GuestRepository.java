package com.citi.innovaciti.welcome.repositories;

import com.citi.innovaciti.welcome.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Liron on 27/04/2014.
 */
public interface GuestRepository extends JpaRepository<Guest,Long> {

    public List<Guest> findByFirstName(String firstName);

    public List<Guest> findByPhoneNumber(String phoneNumber);

    public long countByPhoneNumber(String phoneNumber);
}
