package com.citi.innovaciti.welcome.repositories;

import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit,Long> {

    public List<Visit> findByHost(Host host);

    public List<Visit> findByHostId(long hostId);

    public List<Visit> findByGuest(Guest guest);



}
