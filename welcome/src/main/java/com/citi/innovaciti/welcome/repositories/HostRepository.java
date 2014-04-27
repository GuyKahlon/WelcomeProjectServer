package com.citi.innovaciti.welcome.repositories;

import com.citi.innovaciti.welcome.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Liron on 27/04/2014.
 */
public interface HostRepository extends JpaRepository<Host,Long> {

    List<Host> findByFirstName(String firstName);
}
